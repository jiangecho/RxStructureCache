package com.echo.rxstructurecachetest.cache;

import android.content.Context;

import com.echo.rxstructurecachetest.dao.Cache;
import com.echo.rxstructurecachetest.dao.CacheDao;
import com.echo.rxstructurecachetest.dao.DaoMaster;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.CountQuery;
import de.greenrobot.dao.query.DeleteQuery;
import de.greenrobot.dao.query.Query;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * cache your structure data, such as news, rank list and so on
 * <p/>
 * Created by jiangecho on 16/4/3.
 */
public class RxStructureCache<T extends CacheAble> {

    private CacheDao cacheDao;
    private int cacheCapacityPerType = 20;

    private static RxStructureCache instance;

    public static synchronized RxStructureCache getInstance(Context context, int cacheCapacityPerType) {
        if (instance == null) {
            instance = new RxStructureCache(context, cacheCapacityPerType);
        }
        return instance;
    }

    private RxStructureCache(Context context, int cacheCapacityPerType) {
        this.cacheCapacityPerType = cacheCapacityPerType;
        initGreenDao(context);
    }

    private void initGreenDao(Context context) {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, "cache-db", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        cacheDao = daoMaster.newSession().getCacheDao();

    }

    /**
     * get data firstly from cache, then continue to get data from loader.
     * onNext maybe called twice, one with cached data, and the other one with remote data
     *
     * @param clazz  your model class
     * @param loader remote loader
     * @return a list of entity, latest update entity first
     */
    public Observable<List<T>> getFromCacheThenLoader(Class<T> clazz, Observable<List<T>> loader) {
        Query query = cacheDao.queryBuilder()
                .where(CacheDao.Properties.Type.eq(clazz.getName()))
                .orderDesc(CacheDao.Properties.Id)
                .build();
        final List<Cache> caches = query.list();
        List<T> datas = new ArrayList<>();
        Gson gson = new Gson();
        if (caches != null && caches.size() > 0) {
            for (Cache cache : caches) {
                T t = gson.fromJson(cache.getContent(), clazz);
                datas.add(t);
            }
        }
        return Observable.just(datas)
                .filter(new Func1<List<T>, Boolean>() {
                    @Override
                    public Boolean call(List<T> ts) {
                        return ts.size() > 0;
                    }
                }).concatWith(loader.doOnNext(new Action1<List<T>>() {
                    @Override
                    public void call(List<T> ts) {
                        cache(ts);
                    }
                }));
    }

    /**
     * get data directly from loader, and before emit the data, cache it.
     * onNext would be called only once with the remote data
     *
     * @param loader custom loader
     * @return
     */
    public Observable<List<T>> getFromLoaderAndCache(Observable<List<T>> loader) {
        return loader.filter(new Func1<List<T>, Boolean>() {
            @Override
            public Boolean call(List<T> ts) {
                return ts != null && ts.size() > 0;
            }
        }).map(new Func1<List<T>, List<T>>() {
            @Override
            public List<T> call(List<T> ts) {
                cache(ts);
                return ts;
            }
        });
    }

    /**
     * cache remote data to db
     *
     * @param datas data to be cached
     */
    private void cache(List<T> datas) {
        if (datas == null || datas.size() == 0) {
            return;
        }

        String type = datas.get(0).getClass().getName();

        List<Cache> caches = new ArrayList<Cache>();
        for (T t : datas) {
            Cache cache = toCache(t);
            caches.add(cache);
        }
        cacheDao.insertOrReplaceInTx(caches);

        CountQuery countQuery;
        countQuery = cacheDao.queryBuilder()
                .where(CacheDao.Properties.Type.eq(type))
                .buildCount();

        long count = countQuery.count();
        if (count > cacheCapacityPerType) {
            cacheDao.getDatabase().beginTransaction();
            String deleteSql = "DELETE FROM " + cacheDao.getTablename() + " WHERE _id IN (SELECT _id FROM " + cacheDao.getTablename()
                    + " ORDER BY _id ASC LIMIT " + (count - cacheCapacityPerType) + ")";
            cacheDao.getDatabase().execSQL(deleteSql);
            cacheDao.getDatabase().setTransactionSuccessful();
            cacheDao.getDatabase().endTransaction();
        }

    }

    /**
     * get one entity from cache
     *
     * @param clazz   model class
     * @param cacheId cache id
     * @return
     */
    public T get(Class<T> clazz, long cacheId) {
        Query query = cacheDao.queryBuilder()
                .where(CacheDao.Properties.CacheId.eq(cacheId))
                .where(CacheDao.Properties.Type.eq(clazz.getName()))
                .build();
        Cache cache = (Cache) query.unique();

        return fromCache(clazz, cache);
    }

    /**
     * get one entity from cache
     *
     * @param clazz   model class
     * @param cacheId
     * @return an observable which will emit the cached entity
     */
    public Observable<T> getOb(Class<T> clazz, long cacheId) {
        return Observable.just(get(clazz, cacheId));
    }

    /**
     * update entity in cache, after this operation the id of the cached entity will be update,
     * it would affect the rank position
     *
     * @param record
     */
    public void update(T record) {
        Cache cache = toCache(record);
        cacheDao.insertOrReplaceInTx(cache);
    }

    /**
     * delete one entity from cache
     *
     * @param record entity to be deleted
     */
    public void delete(T record) {
        DeleteQuery query = cacheDao.queryBuilder()
                .where(CacheDao.Properties.CacheId.eq(record.getCacheId()))
                .where(CacheDao.Properties.Type.eq(record.getClass().getName()))
                .buildDelete();
        query.executeDeleteWithoutDetachingEntities();
    }

    private Cache toCache(T record) {
        Gson gson = new Gson();
        Cache cache = new Cache();
        cache.setCacheId(record.getCacheId());
        cache.setVersion(record.getVersion());
        cache.setContent(gson.toJson(record));
        cache.setType(record.getClass().getName());
        return cache;
    }

    private T fromCache(Class<T> clazz, Cache cache) {
        if (cache == null) {
            return null;
        }
        return new Gson().fromJson(cache.getContent(), clazz);
    }

}

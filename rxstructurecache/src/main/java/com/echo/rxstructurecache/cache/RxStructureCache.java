package com.echo.rxstructurecache.cache;

import android.content.Context;

import com.echo.rxstructurecache.dao.Cache;
import com.echo.rxstructurecache.dao.CacheDao;
import com.echo.rxstructurecache.dao.DaoMaster;
import com.echo.rxstructurecache.model.User;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.CountQuery;
import de.greenrobot.dao.query.DeleteQuery;
import de.greenrobot.dao.query.Query;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by jiangecho on 16/4/3.
 */
public class RxStructureCache<T extends CacheAble> {

    private CacheDao cacheDao;
    private int cacheCapacityPerType = 20;

    public void init(Context context) {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, "cache-db", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        cacheDao = daoMaster.newSession().getCacheDao();

    }

    public Observable<List<T>> getDataFromCacheThenLoader(Class<T> clazz, Observable<List<T>> loader) {
        Query query = cacheDao.queryBuilder()
                .where(CacheDao.Properties.Type.eq(clazz.getName()))
                .orderDesc(CacheDao.Properties.Id)
                .build();
        List<Cache> caches = query.list();
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
                }).concatWith(loader);

        // reference
//        Class classData = Class.forName(tempDiskRecord.getDataClassName());
//        Type type = $Gson$Types.newParameterizedTypeWithOwner(null, DiskRecord.class, classData);
//        diskRecord = new Gson().fromJson(reader, type);
    }

    public Observable<List<T>> getDataFromLoaderAndCache(Observable<List<T>> loader) {
        return loader.filter(new Func1<List<T>, Boolean>() {
            @Override
            public Boolean call(List<T> ts) {
                return ts != null && ts.size() > 0;
            }
        }).map(new Func1<List<T>, List<T>>() {
            @Override
            public List<T> call(List<T> ts) {
                // TODO 1, check exist or not
                // TODO 2, exceed the cache capacity, 20 records per type

                Gson gson = new Gson();
                String type = ts.get(0).getClass().getName();

                List<Cache> caches = new ArrayList<Cache>();
                for (T t : ts) {
                    Cache cache = new Cache();
                    cache.setCacheId(t.getCacheId());
                    cache.setVersion(t.getVersion());
                    cache.setType(type);
                    cache.setContent(gson.toJson(t));
                    caches.add(cache);
                }
                cacheDao.insertOrReplaceInTx(caches);

                CountQuery countQuery;
                countQuery = cacheDao.queryBuilder()
                        .where(CacheDao.Properties.Type.eq(type))
                        .buildCount();

                long count = countQuery.count();
                if (count > cacheCapacityPerType) {
                    DeleteQuery deleteQuery = cacheDao.queryBuilder()
                            .where(CacheDao.Properties.Type.eq(type))
                            .orderAsc(CacheDao.Properties.Id)
                            .limit((int) (count - cacheCapacityPerType))
                            .buildDelete();
                    deleteQuery.executeDeleteWithoutDetachingEntities();
                }

                return ts;
            }
        });
    }

    Observable<User> getUser(long id) {
        return null;
    }

    void updateUser(User user) {

    }

    void deleteUser(User user) {

    }

}

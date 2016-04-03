package com.echo.rxstructurecache.cache;

import android.content.Context;

import com.echo.rxstructurecache.dao.Cache;
import com.echo.rxstructurecache.dao.CacheDao;
import com.echo.rxstructurecache.dao.DaoMaster;
import com.echo.rxstructurecache.model.User;

import java.util.List;

import de.greenrobot.dao.query.Query;
import rx.Observable;

/**
 * Created by jiangecho on 16/4/3.
 */
public class RxStructureCache {

    private CacheDao cacheDao;

    public void init(Context context) {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, "fans-db", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        cacheDao = daoMaster.newSession().getCacheDao();

    }

    // user
    Observable<List<User>> getUsersCacheThenLoader(Observable<List<User>> loader) {
        Query query = cacheDao.queryBuilder()
                .where(CacheDao.Properties.Id.eq(1))
                .build();
        List<Cache> caches = cacheDao.loadAll();
        return null;
    }

    Observable<List<User>> getUsersLoaderAndCache(Observable<List<User>> loader) {
        return null;
    }

    Observable<User> getUser(long id) {
        return null;
    }

    void updateUser(User user) {

    }

    void deleteUser(User user) {

    }

}

package com.echo.fans;

import android.app.Application;

import com.echo.fans.dao.DaoMaster;
import com.echo.fans.dao.FansDao;

/**
 * Created by jiangecho on 16/3/31.
 */
public class App extends Application {
    private FansDao fansDao;
    private static App app;
    public boolean isActivated = false;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, "fans-db", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        fansDao = daoMaster.newSession().getFansDao();
    }

    public static App getInstance() {
        return app;
    }

    public FansDao getFansDao() {
        return fansDao;
    }


}

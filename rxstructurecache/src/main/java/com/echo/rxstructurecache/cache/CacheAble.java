package com.echo.rxstructurecache.cache;

/**
 * Created by jiangecho on 16/4/4.
 */
public interface CacheAble {
    /**
     * must be unique
     *
     * @return
     */
    long getCacheId();

    int getVersion();
}

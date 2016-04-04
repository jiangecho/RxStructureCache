package com.echo.rxstructurecache.cache;

/**
 * Any model need to be cached must implement this interface
 * <p/>
 * Created by jiangecho on 16/4/4.
 */
public interface CacheAble {

    /**
     * I think any structure data must have an unique id, such as: news id, user id, tweet id and so on
     *
     * @return an unique id, can be your tweet id or some other unique id
     */
    long getCacheId();

    /**
     * the entity's version
     *
     * @return
     */
    int getVersion();
}

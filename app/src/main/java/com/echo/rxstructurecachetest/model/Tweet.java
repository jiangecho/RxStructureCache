package com.echo.rxstructurecachetest.model;

import com.echo.rxstructurecachetest.cache.CacheAble;

/**
 * Created by jiangecho on 16/4/4.
 */
public class Tweet implements CacheAble {
    private long id;
    private String content;
    private long userId;

    @Override
    public long getCacheId() {
        return id;
    }

    @Override
    public int getVersion() {
        return 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}

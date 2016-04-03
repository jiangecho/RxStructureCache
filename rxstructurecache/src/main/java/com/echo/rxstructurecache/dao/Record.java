package com.echo.rxstructurecache.dao;

/**
 * Created by jiangecho on 16/4/4.
 */
public class Record<T> {
    T data;
    private final String dataClassName;

    Record(T t) {
        this.data = t;
        dataClassName = data.getClass().getName();
    }

    public String getDataClassName() {
        return dataClassName;
    }
}

package com.echo.rxstructurecache.model;

import com.echo.rxstructurecache.cache.CacheAble;

/**
 * Created by jiangecho on 16/4/3.
 */
public class User implements CacheAble {


    /**
     * id : 100
     * name : jiangecho
     * age : 29
     * sex : male
     */

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private long id;
    private String name;
    private String age;
    private String sex;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public long getCacheId() {
        return id;
    }

    @Override
    public int getVersion() {
        return 0;
    }
}

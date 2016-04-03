package com.echo.rxstructurecache.model;

import java.util.List;

/**
 * Created by jiangecho on 16/4/3.
 */
public class Users {
    private int code;
    private List<User> users;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

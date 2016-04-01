package com.echo.fans.data.model;

import java.util.List;

/**
 * Created by jiangecho on 16/4/1.
 */
public class Token {

    /**
     * code : -1
     * data : []
     * msg : 请输入授权码
     */

    private int code;
    private String msg;
    private List<?> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }
}

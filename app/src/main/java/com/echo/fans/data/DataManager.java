package com.echo.fans.data;

import com.echo.fans.data.model.Token;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by jiangecho on 16/3/31.
 */
public class DataManager {
    private static DataManager instance;
    private RestfulService restfulService;

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    private DataManager() {
        this.restfulService = RestfulService.Creator.newAPIRestfulService();
    }

    public void checkKey(String token, Callback<Token> callback) {
        Call<Token> call = restfulService.checkToken("token", "check", token);
        call.enqueue(callback);
    }

    public void activate(String token, Callback<Token> callback) {
        Call<Token> call = restfulService.activate("token", "index", token);
        call.enqueue(callback);
    }
}

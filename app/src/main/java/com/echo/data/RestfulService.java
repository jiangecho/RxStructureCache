package com.echo.data;

import com.echo.Token;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by jiangecho on 16/3/31.
 */
public interface RestfulService {
    String API_BASE_URL = "http://101.200.76.133/dls/";

    @GET("index.php")
    Call<Token> checkToken(@Query("c") String c, @Query("a") String a, @Query("token") String token);

    @GET("index.php")
    Call<Token> activate(@Query("c") String c, @Query("a") String a, @Query("token") String token);


    /********
     * Helper class that sets up a new services
     *******/
    class Creator {

        public static RestfulService newAPIRestfulService() {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(RestfulService.API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            return retrofit.create(RestfulService.class);
        }
    }
}

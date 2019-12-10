package com.appsnipp.loginsamples.model.API;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

   // private static final String BASE_URL = "http://5d30097d28465b00146aaaaf.mockapi.io/api/";

    private static final String BASE_URL = "https://task-assignment-app.herokuapp.com/api/v1/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {

        OkHttpClient httpClient = new OkHttpClient.Builder().build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }
}

package com.appsnipp.loginsamples.model;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface  RequestAPI {

    @GET("/project")
    Call<ArrayList<ProjectModel>>  getALLProject();
}

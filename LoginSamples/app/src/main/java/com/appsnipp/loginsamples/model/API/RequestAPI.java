package com.appsnipp.loginsamples.model.API;

import com.appsnipp.loginsamples.model.Project_model.ProjectAddResponse;
import com.appsnipp.loginsamples.model.Task_model.TaskModel;
import com.appsnipp.loginsamples.model.Task_model.TaskaddResponse;
import com.appsnipp.loginsamples.model.User_model.ListUserModel;
import com.appsnipp.loginsamples.model.Login.Login;
import com.appsnipp.loginsamples.model.Project_model.ProjectListResponse;
import com.appsnipp.loginsamples.model.Task_model.TaskListResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface  RequestAPI {

    @GET("project")
    Call<ProjectListResponse>  getALLProject(
            @Header("Authorization") String token
    );


    @POST("authenticate")
    @FormUrlEncoded
    Call<Login> getuserlogin(@Field("email") String email,
                             @Field("pass") String pass
                             );

    @POST("task/get_Project_Tasks")
    @FormUrlEncoded
    Call<TaskListResponse> gettaskofproject(
            @Header("Authorization") String token,
            @Field("project_id") String project_id
    );

    @POST("task/create_task")
    @FormUrlEncoded
    Call<TaskaddResponse> addtask(
            @Header("Authorization") String token,
            @Field("name") String name,
            @Field("end_date") String end_date,
            @Field("project_id") String project_id,
            @Field("content") String content,
            @Field("user_id") String user_id
    );

    @POST("create_project")
    @FormUrlEncoded
    Call<ProjectAddResponse> addproject(
            @Header("Authorization") String token,
            @Field("name") String name,
            @Field("endDate") String end_date
    );


    @GET("users/listUsers")
    Call<ListUserModel>  getListUser(
            @Header("Authorization") String token
    );


}

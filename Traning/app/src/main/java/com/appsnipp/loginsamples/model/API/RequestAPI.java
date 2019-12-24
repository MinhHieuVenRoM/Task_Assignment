package com.appsnipp.loginsamples.model.API;

import android.text.Editable;

import com.appsnipp.loginsamples.model.Attendance.Attendance;
import com.appsnipp.loginsamples.model.Attendance.Attendance_List;
import com.appsnipp.loginsamples.model.Attendance.Attendance_checkout;
import com.appsnipp.loginsamples.model.Attendance.Attendance_list_detail;
import com.appsnipp.loginsamples.model.Attendance.Check;
import com.appsnipp.loginsamples.model.Chat.Chat_user;
import com.appsnipp.loginsamples.model.Chat.Find_Group_Chat;
import com.appsnipp.loginsamples.model.Chat.Group_chat;
import com.appsnipp.loginsamples.model.Chat.Group_chat_edit;
import com.appsnipp.loginsamples.model.Chat.Messenger;
import com.appsnipp.loginsamples.model.Location;
import com.appsnipp.loginsamples.model.Login.Register;
import com.appsnipp.loginsamples.model.Project_model.ProjectAddResponse;
import com.appsnipp.loginsamples.model.Project_model.Project_edit_model;
import com.appsnipp.loginsamples.model.Task_model.EditTaskModel;
import com.appsnipp.loginsamples.model.Task_model.NotifyTask;
import com.appsnipp.loginsamples.model.Task_model.TaskaddResponse;
import com.appsnipp.loginsamples.model.User_model.ListUserModel;
import com.appsnipp.loginsamples.model.Login.Login;
import com.appsnipp.loginsamples.model.Project_model.ProjectListResponse;
import com.appsnipp.loginsamples.model.Task_model.TaskListResponse;
import com.appsnipp.loginsamples.model.User_model.UserEditModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface  RequestAPI {

    @GET("project")
    Call<ProjectListResponse>  getALLProject(
            @Header("Authorization") String token
    );

    @POST("project/get_user_project")
    @FormUrlEncoded
    Call<ProjectListResponse>  getProjectofUser(
            @Header("Authorization") String token,
            @Field("user_id") String user_id
    );
    @POST("task/get_user_project_tasks")
    @FormUrlEncoded
    Call<TaskListResponse>  getTasktofUser(
            @Header("Authorization") String token,
            @Field("user_id") String user_id,
            @Field("project_id") String project_id
    );

    @POST("attendance/check_attendance")
    @FormUrlEncoded
    Call<Check>  checkin(
            @Header("Authorization") String token,
            @Field("date") String date
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

    @POST("task/get_nearby_task")
    @FormUrlEncoded
    Call<NotifyTask>  gettask_notify(
            @Header("Authorization") String token,
            @Field("user_id") String user_id
    );

    @POST("task/get_task_by_date")
    @FormUrlEncoded
    Call<TaskListResponse> gettask_date(
            @Header("Authorization") String token,
            @Field("date") String date
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

    @POST("users/profile")
    @FormUrlEncoded
    Call<ListUserModel> viewprofile(
            @Header("Authorization") String token,
            @Field("email") String email

    );
    @POST("location/get_location_by_name")
    @FormUrlEncoded
    Call<Location> getlocaltion(
            @Field("name") String name
    );
    @PUT("users/ad/reset_password")
    @FormUrlEncoded
    Call<Login> resetpassword(
            @Header("Authorization") String token,
            @Field("email") String email

    );

    @POST("users")
    @FormUrlEncoded
    Call<Register> adduser(
            @Field("email") String email,
            @Field("name") String name,
            @Field("password") String password,
            @Field("sex") int sex,
            @Field("dob") String dob,
            @Field("phone") String phone
    );

    @PUT("project/edit_project")
    @FormUrlEncoded
    Call<Project_edit_model> editproject(
            @Header("Authorization") String token,
            @Field("_id") String _id,
            @Field("name") String name,
            @Field("end_date") String end_date,
            @Field("status") int status
    );


    @PUT("users/edit_user")
    @FormUrlEncoded
    Call<UserEditModel> edituser(
            @Header("Authorization") String token,
            @Field("_id") String _id,
            @Field("name") String name,
            @Field("phone") String phone,
            @Field("dob") String dob ,
            @Field("sex") int sex,
            @Field("role") int role,
            @Field("status") int status
    );
    @PUT("users/edit_user")
    @FormUrlEncoded
    Call<UserEditModel> changepassword(
            @Header("Authorization") String token,
            @Field("_id") String _id,
            @Field("name") String name,
            @Field("password") String password ,
            @Field("new_password") String new_password
    );

    @PUT("task/edit_task")
    @FormUrlEncoded
    Call<EditTaskModel> edittask(
            @Header("Authorization") String token,
            @Field("user_id") String user_id,
            @Field("end_date") String end_date,
            @Field("_id") String _id ,
            @Field("content") Editable content,
            @Field("status") int status,
            @Field("name") String name
    );

    @PUT("attendance/check_in")
    @FormUrlEncoded
    Call<Attendance> Attendance(
            @Header("Authorization") String token,
            @Field("date") String date
    );
    @PUT("attendance/check_out")
    @FormUrlEncoded
    Call<Attendance_checkout> Attendancecheckout(
            @Header("Authorization") String token,
            @Field("date") String date
    );
    @POST("attendance/get_list_attendance")
    @FormUrlEncoded
    Call<Attendance_List> Attendance_list(
            @Header("Authorization") String token,
            @Field("date") String date
    );
    @POST("attendance/ad/get_attendance_by_user")
    @FormUrlEncoded
    Call<Attendance_list_detail> Attendance_list_detail(
            @Header("Authorization") String token,
            @Field("user_id") String user_id
    );
    @POST("attendance/ad/get_attendance_user_by_month")
    @FormUrlEncoded
    Call<Attendance_list_detail> Attendance_list_detail_user_month(
            @Header("Authorization") String token,
            @Field("user_id") String user_id,
            @Field("date") String date
    );
    @POST("attendance/ad/get_attendance_user_by_year")
    @FormUrlEncoded
    Call<Attendance_list_detail> Attendance_list_detail_user_year(
            @Header("Authorization") String token,
            @Field("user_id") String user_id,
            @Field("date") String date
    );

    @POST("room/search_room")
    @FormUrlEncoded
    Call<Chat_user> get_id_room_twouser(
            @Header("Authorization") String token,
            @Field("users") String[] user_id,
            @Field("is_single") String is_single
    );
    @POST("room/create_room")
    @FormUrlEncoded
    Call<Group_chat> create_group_chat(
            @Header("Authorization") String token,
            @Field("users") ArrayList<String> user_id,
            @Field("room_name") String room_name,
            @Field("user_create") String create_by
    );

    @PUT("room/edit_room")
    @FormUrlEncoded
    Call<Group_chat_edit> edit_group_chat(
            @Header("Authorization") String token,
            @Field("_id") String id_room,
            @Field("users") ArrayList<String> user_id,
            @Field("room_name") String room_name
    );

    @POST("room/get_list_group")
    @FormUrlEncoded
    Call<Find_Group_Chat> find_group_chat(
            @Header("Authorization") String token,
            @Field("user_id") String user_id
    );
    @POST("/api/v1/chat/get_history_chat")
    @FormUrlEncoded
    Call<Messenger> get_chat_history_twouser(
            @Header("Authorization") String token,
            @Field("room_id") String room_id
    );
}

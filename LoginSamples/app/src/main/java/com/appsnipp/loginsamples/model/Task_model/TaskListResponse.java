package com.appsnipp.loginsamples.model.Task_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TaskListResponse {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data_task")
    @Expose
    private ArrayList<TaskModel> data_task = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<TaskModel> getDataTask() {
        return data_task;
    }

    public void setDataTask(ArrayList<TaskModel> dataTask) {
        this.data_task = dataTask;
    }
}

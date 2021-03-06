package com.appsnipp.loginsamples.model.User_model;

import com.appsnipp.loginsamples.model.User_model.UserModelDetail;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ListUserModel {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ArrayList<UserModelDetail> data = null;

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

    public ArrayList<UserModelDetail> getData() {
        return data;
    }

    public void setData(ArrayList<UserModelDetail> data) {
        this.data = data;
    }
}

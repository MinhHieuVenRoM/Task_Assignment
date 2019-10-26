package com.appsnipp.loginsamples.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class ProjectModel implements Serializable {
    @SerializedName("isItemClicked")
    @Expose
    Boolean isItemClicked = false;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("To_Time")
    @Expose
    private String toTime;
    @SerializedName("End_Time")
    @Expose
    private String endTime;
    @SerializedName("User_Name_Create")
    @Expose
    private String userNameCreate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getUserNameCreate() {
        return userNameCreate;
    }

    public void setUserNameCreate(String userNameCreate) {
        this.userNameCreate = userNameCreate;
    }

    public Boolean getItemClicked() {
        return isItemClicked;
    }

    public void setItemClicked(Boolean itemClicked) {
        isItemClicked = itemClicked;
    }
}
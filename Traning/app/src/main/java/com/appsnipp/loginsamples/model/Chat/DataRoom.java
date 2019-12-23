package com.appsnipp.loginsamples.model.Chat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataRoom {

    @SerializedName("users")
    @Expose
    private List<String> users = null;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("room_name")
    @Expose
    private String roomName;
    @SerializedName("user_create")
    @Expose
    private String userCreate;
    @SerializedName("__v")
    @Expose
    private Integer v;

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

}

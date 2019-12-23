package com.appsnipp.loginsamples.model.Chat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataGroup implements Serializable {

    @SerializedName("users")
    @Expose
    private ArrayList<String> users = null;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("room_name")
    @Expose
    private String roomName;
    @SerializedName("created_by")
    @Expose
    private String createdBy;
    @SerializedName("__v")
    @Expose
    private Integer v;

    public String getTinnhancuoi() {
        return tinnhancuoi;
    }

    public void setTinnhancuoi(String tinnhancuoi) {
        this.tinnhancuoi = tinnhancuoi;
    }

    private String tinnhancuoi="";
    public ArrayList<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> users) {
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

}

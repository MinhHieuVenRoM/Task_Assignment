package com.appsnipp.loginsamples.model.Chat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Group_chat_edit {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private String dataRoom;

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

    public String getDataRoom() {
        return dataRoom;
    }

    public void setDataRoom(String dataRoom) {
        this.dataRoom = dataRoom;
    }


}
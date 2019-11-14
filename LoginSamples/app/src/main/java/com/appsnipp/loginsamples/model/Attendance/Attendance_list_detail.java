package com.appsnipp.loginsamples.model.Attendance;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Attendance_list_detail {



    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ArrayList<DataDetailAttendance> data = null;

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

    public ArrayList<DataDetailAttendance> getData() {
        return data;
    }

    public void setData(ArrayList<DataDetailAttendance> data) {
        this.data = data;
    }
}

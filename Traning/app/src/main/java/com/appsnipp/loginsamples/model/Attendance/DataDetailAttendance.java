package com.appsnipp.loginsamples.model.Attendance;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataDetailAttendance extends Data{

    @SerializedName("check_out_time")
    @Expose
    private String checkOutTime;

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }


}

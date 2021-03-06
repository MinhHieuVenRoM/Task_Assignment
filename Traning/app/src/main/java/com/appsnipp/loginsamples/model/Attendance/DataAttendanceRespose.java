package com.appsnipp.loginsamples.model.Attendance;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataAttendanceRespose extends Data{


    @SerializedName("check_out_time")
    @Expose
    private String checkOutTime;
    @SerializedName("user_detail")
    @Expose
    private UserDetailName userDetail;
    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public UserDetailName getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserDetailName userDetail) {
        this.userDetail = userDetail;
    }

}

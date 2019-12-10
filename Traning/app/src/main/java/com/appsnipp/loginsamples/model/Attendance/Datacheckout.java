package com.appsnipp.loginsamples.model.Attendance;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datacheckout {

    @SerializedName("attendance_date")
    @Expose
    private String attendanceDate;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("check_in")
    @Expose
    private Boolean checkIn;
    @SerializedName("check_out")
    @Expose
    private Boolean checkOut;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("check_out_time")
    @Expose
    private String checkOutTime;

    public String getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(Boolean checkIn) {
        this.checkIn = checkIn;
    }

    public Boolean getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(Boolean checkOut) {
        this.checkOut = checkOut;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

}

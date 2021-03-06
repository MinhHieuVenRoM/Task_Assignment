package com.appsnipp.loginsamples.model.Attendance;

        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class Attendance_checkout {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Datacheckout data;

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

    public Datacheckout getData() {
        return data;
    }

    public void setData(Datacheckout data) {
        this.data = data;
    }

}
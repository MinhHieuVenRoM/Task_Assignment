package com.appsnipp.loginsamples.model.Task_model;

        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

        import java.io.Serializable;
        import java.util.ArrayList;

public class NotifyTask implements Serializable {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data_task")
    @Expose
    private ArrayList<DataTasknotify> dataTask = null;

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

    public ArrayList<DataTasknotify> getDataTask() {
        return dataTask;
    }

    public void setDataTask(ArrayList<DataTasknotify> dataTask) {
        this.dataTask = dataTask;
    }

}
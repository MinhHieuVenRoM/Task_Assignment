package com.appsnipp.loginsamples.model.Task_model;

        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

        import java.io.Serializable;

public class TaskaddResponse implements Serializable {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data_task")
    @Expose
    private DataTask dataTask;

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

    public DataTask getDataTask() {
        return dataTask;
    }

    public void setDataTask(DataTask dataTask) {
        this.dataTask = dataTask;
    }

}
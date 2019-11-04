package com.appsnipp.loginsamples.model.Project_model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Project_edit_model implements Serializable {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data_project")
    @Expose
    private DataProjectedit dataProject;

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

    public DataProjectedit getDataProject() {
        return dataProject;
    }

    public void setDataProject(DataProjectedit dataProject) {
        this.dataProject = dataProject;
    }

}
package com.appsnipp.loginsamples.model.Project_model;
import com.appsnipp.loginsamples.model.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ProjectListResponse extends BaseResponse implements Serializable{
    @SerializedName("data_project")
    @Expose
    private ArrayList<ProjectModel> data_project = null;

    public ArrayList<ProjectModel> getData_project() {
        return data_project;
    }

    public void setData_project(ArrayList<ProjectModel> data_project) {
        this.data_project = data_project;
    }

}


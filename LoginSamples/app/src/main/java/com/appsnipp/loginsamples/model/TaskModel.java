package com.appsnipp.loginsamples.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class TaskModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("Project_fk")
    @Expose
    private String projectFk;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("Detail")
    @Expose
    private String detail;
    @SerializedName("Deadline")
    @Expose
    private String deadline;
    @SerializedName("User_Create")
    @Expose
    private String userCreate;
    @SerializedName("User_Receive")
    @Expose
    private String userReceive;
    @SerializedName("Status")
    @Expose
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectFk() {
        return projectFk;
    }

    public void setProjectFk(String projectFk) {
        this.projectFk = projectFk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
    }

    public String getUserReceive() {
        return userReceive;
    }

    public void setUserReceive(String userReceive) {
        this.userReceive = userReceive;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

package com.appsnipp.loginsamples.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Locationdata  implements Serializable {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("LocalX")
    @Expose
    private Double localX;
    @SerializedName("LocalY")
    @Expose
    private Double localY;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLocalX() {
        return localX;
    }

    public void setLocalX(Double localX) {
        this.localX = localX;
    }

    public Double getLocalY() {
        return localY;
    }

    public void setLocalY(Double localY) {
        this.localY = localY;
    }

}

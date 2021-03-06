package com.appsnipp.loginsamples.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DIEMDANH {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("USER_ID")
    @Expose
    private String uSERID;
    @SerializedName("NAME")
    @Expose
    private String nAME;
    @SerializedName("NGAY")
    @Expose
    private String nGAY;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUSERID() {
        return uSERID;
    }

    public void setUSERID(String uSERID) {
        this.uSERID = uSERID;
    }

    public String getNAME() {
        return nAME;
    }

    public void setNAME(String nAME) {
        this.nAME = nAME;
    }

    public String getNGAY() {
        return nGAY;
    }

    public void setNGAY(String nGAY) {
        this.nGAY = nGAY;
    }

}

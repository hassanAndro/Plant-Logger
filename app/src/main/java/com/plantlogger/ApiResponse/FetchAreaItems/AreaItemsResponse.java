package com.plantlogger.ApiResponse.FetchAreaItems;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AreaItemsResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ArrayList<AreaItemDatum> data = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<AreaItemDatum> getData() {
        return data;
    }

    public void setData(ArrayList<AreaItemDatum> data) {
        this.data = data;
    }

}
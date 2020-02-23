package com.plantlogger.ApiResponse.SelectionSets;

/**
 * Created by Hassan_Hameed on 12/5/2017.
 */

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SelectionSetsResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ArrayList<SelectionSetsDatum> data = null;

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

    public ArrayList<SelectionSetsDatum> getData() {
        return data;
    }

    public void setData(ArrayList<SelectionSetsDatum> data) {
        this.data = data;
    }

}
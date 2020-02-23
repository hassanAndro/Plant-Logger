package com.plantlogger.ApiResponse.EquipmentStatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.plantlogger.ApiResponse.AreaCheckList.AreaCheckListDatum;

import java.util.ArrayList;

/**
 * Created by Hassan_Hameed on 10/31/2017.
 */

public class EquipmentStatusResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ArrayList<EquipmentStatusDatum> data = null;

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

    public ArrayList<EquipmentStatusDatum> getData() {
        return data;
    }

    public void setData(ArrayList<EquipmentStatusDatum> data) {
        this.data = data;
    }
}

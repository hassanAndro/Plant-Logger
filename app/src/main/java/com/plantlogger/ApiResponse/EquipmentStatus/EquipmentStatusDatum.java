package com.plantlogger.ApiResponse.EquipmentStatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Hassan_Hameed on 10/31/2017.
 */

public class EquipmentStatusDatum {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("sets_idx")
    @Expose
    private String sets_idx;
    @SerializedName("sets_value")
    @Expose
    private String sets_value;
    @SerializedName("isChecked")
    @Expose
    private String isChecked;
    @SerializedName("multiple")
    @Expose
    private String multiple;

    public String getMultiple() {
        return multiple;
    }

    public void setMultiple(String multiple) {
        this.multiple = multiple;
    }

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSets_idx() {
        return sets_idx;
    }

    public void setSets_idx(String sets_idx) {
        this.sets_idx = sets_idx;
    }

    public String getSets_value() {
        return sets_value;
    }

    public void setSets_value(String sets_value) {
        this.sets_value = sets_value;
    }
}

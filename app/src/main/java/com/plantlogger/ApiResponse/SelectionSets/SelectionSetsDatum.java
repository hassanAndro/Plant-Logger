package com.plantlogger.ApiResponse.SelectionSets;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Hassan_Hameed on 12/5/2017.
 */

public class SelectionSetsDatum {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("sets_idx")
    @Expose
    private String setsIdx;
    @SerializedName("sets_value")
    @Expose
    private String setsValue;
    @SerializedName("multiple")
    @Expose
    private String multiple;

    public String getMultiple() {
        return multiple;
    }

    public void setMultiple(String multiple) {
        this.multiple = multiple;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSetsIdx() {
        return setsIdx;
    }

    public void setSetsIdx(String setsIdx) {
        this.setsIdx = setsIdx;
    }

    public String getSetsValue() {
        return setsValue;
    }

    public void setSetsValue(String setsValue) {
        this.setsValue = setsValue;
    }
}
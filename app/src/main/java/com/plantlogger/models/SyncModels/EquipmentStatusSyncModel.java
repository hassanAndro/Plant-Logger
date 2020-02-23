package com.plantlogger.models.SyncModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Hassan_Hameed on 11/3/2017.
 */

public class EquipmentStatusSyncModel {
    @SerializedName("equipment_id")
    @Expose
    private String equipment_id;
    @SerializedName("sets_idx")
    @Expose
    private String sets_idx;
    @SerializedName("sets_value")
    @Expose
    private String sets_value;
    @SerializedName("groupd_id")
    @Expose
    private String groupd_id;
    @SerializedName("email_id")
    @Expose
    private String email_id;
    @SerializedName("adminId")
    @Expose
    private String adminId;
    @SerializedName("dateTime")
    @Expose
    private String dateTime;
    @SerializedName("isShown")
    @Expose
    private String isShown;

    public String getMultiple() {
        return multiple;
    }

    public void setMultiple(String multiple) {
        this.multiple = multiple;
    }

    @SerializedName("multiple")
    @Expose
    private String multiple;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @SerializedName("ID")

    @Expose
    private String ID;
    @SerializedName("name")
    @Expose
    private String name;

    public String getQr_tag() {
        return qr_tag;
    }

    public void setQr_tag(String qr_tag) {
        this.qr_tag = qr_tag;
    }

    @SerializedName("qr_tag")
    @Expose
    private String qr_tag;

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getIsShown() {
        return isShown;
    }

    public void setIsShown(String isShown) {
        this.isShown = isShown;
    }

    public String getEquipment_id() {
        return equipment_id;
    }

    public void setEquipment_id(String equipment_id) {
        this.equipment_id = equipment_id;
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

    public String getGroupd_id() {
        return groupd_id;
    }

    public void setGroupd_id(String groupd_id) {
        this.groupd_id = groupd_id;
    }
}

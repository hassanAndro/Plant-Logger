package com.plantlogger.models.DBSyncModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.plantlogger.models.Base;

/**
 * Created by hhvm on 11/15/2017.
 */

public class EquipmentStatusDBSyncModel extends Base {


    @SerializedName("ID")
    @Expose
    private String ID;
    @SerializedName("name")
    @Expose
    private String name;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(String admin_id) {
        this.admin_id = admin_id;
    }

    public String getDate_time() {
        return date_time;
    }

    public String getQr_tag() {
        return qr_tag;
    }

    public void setQr_tag(String qr_tag) {
        this.qr_tag = qr_tag;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

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
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("admin_id")
    @Expose
    private String admin_id;
    @SerializedName("date_time")
    @Expose
    private String date_time;
    @SerializedName("qr_tag")
    @Expose
    private String qr_tag;
}

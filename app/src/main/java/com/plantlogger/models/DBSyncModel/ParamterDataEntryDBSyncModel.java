package com.plantlogger.models.DBSyncModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.plantlogger.models.Base;

/**
 * Created by hhvm on 11/15/2017.
 */

public class ParamterDataEntryDBSyncModel extends Base {

    public String getArea_item_id() {
        return area_item_id;
    }

    public void setArea_item_id(String area_item_id) {
        this.area_item_id = area_item_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSet_id() {
        return set_id;
    }

    public void setSet_id(String set_id) {
        this.set_id = set_id;
    }

    public String getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(String unit_id) {
        this.unit_id = unit_id;
    }

    public String getGroupd_id() {
        return groupd_id;
    }

    public void setGroupd_id(String groupd_id) {
        this.groupd_id = groupd_id;
    }

    public String getOptimal_range() {
        return optimal_range;
    }

    public void setOptimal_range(String optimal_range) {
        this.optimal_range = optimal_range;
    }

    public String getAbsolute_range() {
        return absolute_range;
    }

    public void setAbsolute_range(String absolute_range) {
        this.absolute_range = absolute_range;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    @SerializedName("area_item_id")
    @Expose
    private String area_item_id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("set_id")
    @Expose
    private String set_id;
    @SerializedName("unit_id")
    @Expose
    private String unit_id;
    @SerializedName("groupd_id")
    @Expose
    private String groupd_id;
    @SerializedName("optimal_range")
    @Expose
    private String optimal_range;
    @SerializedName("absolute_range")
    @Expose
    private String absolute_range;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("adminId")
    @Expose
    private String adminId;
    @SerializedName("user_name")
    @Expose
    private String user_name;
    @SerializedName("date_time")
    @Expose
    private String date_time;

    public String getQr_tag() {
        return qr_tag;
    }

    public void setQr_tag(String qr_tag) {
        this.qr_tag = qr_tag;
    }

    @SerializedName("qr_tag")
    @Expose
    private String qr_tag;
    @SerializedName("isChecked")
    @Expose
    private String isChecked;

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }
}

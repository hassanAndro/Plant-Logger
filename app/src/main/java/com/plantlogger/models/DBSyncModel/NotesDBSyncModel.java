package com.plantlogger.models.DBSyncModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.plantlogger.models.Base;

/**
 * Created by hhvm on 11/15/2017.
 */

public class NotesDBSyncModel extends Base {

    @SerializedName("groupd_id")
    @Expose
    private String groupd_id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("adminId")
    @Expose
    private String adminId;
    @SerializedName("date_time")
    @Expose
    private String date_time;
    @SerializedName("qr_tag")
    @Expose
    private String qr_tag;

    public String getQr_tag() {
        return qr_tag;
    }

    public void setQr_tag(String qr_tag) {
        this.qr_tag = qr_tag;
    }

    public String getGroupd_id() {
        return groupd_id;
    }

    public void setGroupd_id(String groupd_id) {
        this.groupd_id = groupd_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

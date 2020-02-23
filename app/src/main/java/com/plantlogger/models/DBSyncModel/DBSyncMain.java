package com.plantlogger.models.DBSyncModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.plantlogger.models.Base;

import java.util.ArrayList;

/**
 * Created by hhvm on 11/15/2017.
 */

public class DBSyncMain extends Base {
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("adminId")
    @Expose
    private String adminId;
    @SerializedName("parameter_data_entry")
    @Expose
    private ArrayList<ParamterDataEntryDBSyncModel> parameter_data_entry;
    @SerializedName("equipment_status")
    @Expose
    private ArrayList<EquipmentStatusDBSyncModel> equipment_status;
    @SerializedName("areachecklist_status")
    @Expose
    private ArrayList<AreaCheckListDBSyncModel> areachecklist_status;
    @SerializedName("notes")
    @Expose
    private ArrayList<NotesDBSyncModel> notes;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public ArrayList<ParamterDataEntryDBSyncModel> getParameter_data_entry() {
        return parameter_data_entry;
    }

    public void setParameter_data_entry(ArrayList<ParamterDataEntryDBSyncModel> parameter_data_entry) {
        this.parameter_data_entry = parameter_data_entry;
    }

    public ArrayList<EquipmentStatusDBSyncModel> getEquipment_status() {
        return equipment_status;
    }

    public void setEquipment_status(ArrayList<EquipmentStatusDBSyncModel> equipment_status) {
        this.equipment_status = equipment_status;
    }

    public ArrayList<AreaCheckListDBSyncModel> getAreachecklist_status() {
        return areachecklist_status;
    }

    public void setAreachecklist_status(ArrayList<AreaCheckListDBSyncModel> areachecklist_status) {
        this.areachecklist_status = areachecklist_status;
    }

    public ArrayList<NotesDBSyncModel> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<NotesDBSyncModel> notes) {
        this.notes = notes;
    }
}

package com.plantlogger.models.SyncModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Hassan_Hameed on 11/3/2017.
 */

public class NotesSyncModel {
    @SerializedName("row_id")
    @Expose
    private String row_id;
    @SerializedName("groupd_id")
    @Expose
    private String groupd_id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("note")
    @Expose
    private String note;

    public String getRow_id() {
        return row_id;
    }

    public void setRow_id(String row_id) {
        this.row_id = row_id;
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
}

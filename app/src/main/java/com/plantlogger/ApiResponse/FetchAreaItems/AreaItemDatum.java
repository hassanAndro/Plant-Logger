package com.plantlogger.ApiResponse.FetchAreaItems;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AreaItemDatum {

    @SerializedName("id")
    @Expose
    private String id;

    public String getaMin() {
        return aMin;
    }

    public void setaMin(String aMin) {
        this.aMin = aMin;
    }

    public String getaMax() {
        return aMax;
    }

    public void setaMax(String aMax) {
        this.aMax = aMax;
    }

    public String getoMin() {
        return oMin;
    }

    public void setoMin(String oMin) {
        this.oMin = oMin;
    }

    public String getoMax() {
        return oMax;
    }

    public void setoMax(String oMax) {
        this.oMax = oMax;
    }

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("set_id")
    @Expose
    private String setId;
    @SerializedName("unit_id")
    @Expose
    private String unitId;
    @SerializedName("absolute_range_flag")
    @Expose
    private String absoluteRangeFlag;
    @SerializedName("a_min")
    @Expose
    private String aMin;
    @SerializedName("a_max")
    @Expose
    private String aMax;
    @SerializedName("optimal_range")
    @Expose
    private String optimalRange;
    @SerializedName("o_min")
    @Expose
    private String oMin;
    @SerializedName("o_max")
    @Expose
    private String oMax;
    @SerializedName("force_note")
    @Expose
    private String forceNote;
    @SerializedName("activites")
    @Expose
    private String activites;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("admin_id")
    @Expose
    private String adminId;

    @SerializedName("groupd_id")
    @Expose
    private String groupdId;

    public String getIsFilled() {
        return isFilled;
    }

    public void setIsFilled(String isFilled) {
        this.isFilled = isFilled;
    }

    @SerializedName("isFilled")
    @Expose
    private String isFilled;

//    public String getaMin() {
//        return aMin;
//    }
//
//    public void setaMin(String aMin) {
//        this.aMin = aMin;
//    }
//
//    public String getaMax() {
//        return aMax;
//    }
//
//    public void setaMax(String aMax) {
//        this.aMax = aMax;
//    }

//    public String getoMin() {
//        return oMin;
//    }
//
//    public void setoMin(String oMin) {
//        this.oMin = oMin;
//    }
//
//    public String getoMax() {
//        return oMax;
//    }
//
//    public void setoMax(String oMax) {
//        this.oMax = oMax;
//    }

    public String getChecklist_id() {
        return checklist_id;
    }

    public void setChecklist_id(String checklist_id) {
        this.checklist_id = checklist_id;
    }

    public String getEquipment_id() {
        return equipment_id;
    }

    public void setEquipment_id(String equipment_id) {
        this.equipment_id = equipment_id;
    }

    @SerializedName("checklist_id")

    @Expose
    private String checklist_id;
    @SerializedName("equipment_id")
    @Expose
    private String equipment_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSetId() {
        return setId;
    }

    public void setSetId(String setId) {
        this.setId = setId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getAbsoluteRangeFlag() {
        return absoluteRangeFlag;
    }

    public void setAbsoluteRangeFlag(String absoluteRangeFlag) {
        this.absoluteRangeFlag = absoluteRangeFlag;
    }

    public String getAMin() {
        return aMin;
    }

    public void setAMin(String aMin) {
        this.aMin = aMin;
    }

    public String getAMax() {
        return aMax;
    }

    public void setAMax(String aMax) {
        this.aMax = aMax;
    }

    public String getOptimalRange() {
        return optimalRange;
    }

    public void setOptimalRange(String optimalRange) {
        this.optimalRange = optimalRange;
    }

    public String getOMin() {
        return oMin;
    }

    public void setOMin(String oMin) {
        this.oMin = oMin;
    }

    public String getOMax() {
        return oMax;
    }

    public void setOMax(String oMax) {
        this.oMax = oMax;
    }

    public String getForceNote() {
        return forceNote;
    }

    public void setForceNote(String forceNote) {
        this.forceNote = forceNote;
    }

    public String getActivites() {
        return activites;
    }

    public void setActivites(String activites) {
        this.activites = activites;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getGroupdId() {
        return groupdId;
    }

    public void setGroupdId(String groupdId) {
        this.groupdId = groupdId;
    }

}
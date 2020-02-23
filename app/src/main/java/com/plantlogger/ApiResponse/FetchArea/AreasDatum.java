
package com.plantlogger.ApiResponse.FetchArea;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AreasDatum {

    @SerializedName("cat_id")
    @Expose
    private String catId;
    @SerializedName("cat_name")
    @Expose
    private String catName;
    @SerializedName("cat_desc")
    @Expose
    private String catDesc;
    @SerializedName("cat_father_id")
    @Expose
    private String catFatherId;
    @SerializedName("adminId")
    @Expose
    private String adminId;
    @SerializedName("isLocked")
    @Expose
    private String isLocked;
    @SerializedName("barcode")
    @Expose
    private String barcode;
    @SerializedName("qr_string")
    @Expose
    private String qr_string;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getQr_string() {
        return qr_string;
    }

    public void setQr_string(String qr_string) {
        this.qr_string = qr_string;
    }

    public String getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(String isLocked) {
        this.isLocked = isLocked;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatDesc() {
        return catDesc;
    }

    public void setCatDesc(String catDesc) {
        this.catDesc = catDesc;
    }

    public String getCatFatherId() {
        return catFatherId;
    }

    public void setCatFatherId(String catFatherId) {
        this.catFatherId = catFatherId;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

}

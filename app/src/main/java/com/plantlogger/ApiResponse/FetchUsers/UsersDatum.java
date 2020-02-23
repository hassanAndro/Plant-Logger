
package com.plantlogger.ApiResponse.FetchUsers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UsersDatum {

    @SerializedName("adminId")
    @Expose
    private String adminId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("salt")
    @Expose
    private String salt;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("noLogins")
    @Expose
    private String noLogins;
    @SerializedName("lastTime")
    @Expose
    private String lastTime;

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    @SerializedName("company_id")
    @Expose
    private String company_id;

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNoLogins() {
        return noLogins;
    }

    public void setNoLogins(String noLogins) {
        this.noLogins = noLogins;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

}

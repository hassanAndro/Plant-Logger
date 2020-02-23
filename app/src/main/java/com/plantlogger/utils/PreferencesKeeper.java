package com.plantlogger.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Hassan_Hameed on 9/18/2017.
 */

public class PreferencesKeeper {
    Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public PreferencesKeeper(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences("com.plantlogger", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void clearAppData() {
        editor.clear();
        editor.commit();
    }

    public void setName(String name) {
        editor.putString("setName", name);
        editor.apply();
    }

    public String getName() {
        return preferences.getString("setName", null);
    }

    public void setEmail(String email) {
        editor.putString("setEmail", email);
        editor.apply();
    }

    public String getEmail() {
        return preferences.getString("setEmail", null);
    }

    public void setID(String email) {
        editor.putString("setID", email);
        editor.apply();
    }

    public String getID() {
        return preferences.getString("setID", null);
    }

    public void setCustomerId(String customer_id) {
        editor.putString("setCustomerId", customer_id);
        editor.apply();
    }

    public String getCustomerId() {
        return preferences.getString("setCustomerId", null);
    }

    public void setLoginStatus(Boolean loginStatus) {
        editor.putBoolean("setLoginStatus", loginStatus);
        editor.apply();
    }

    public Boolean getLoginStatus() {
        return preferences.getBoolean("setLoginStatus", false);
    }


    public void setGroupId(String group_id) {
        editor.putString("setGroupId", group_id);
        editor.apply();
    }

    public String getGroupId() {
        return preferences.getString("setGroupId", null);
    }

    public void setSyncTime(long time) {
        editor.putLong("setSyncTime", time);
        editor.apply();
    }

    public long getSyncTime() {
        return preferences.getLong("setSyncTime", -1);
    }
}

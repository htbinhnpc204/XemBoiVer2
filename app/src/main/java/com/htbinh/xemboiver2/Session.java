package com.htbinh.xemboiver2;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.htbinh.xemboiver2.data.model.History;

import java.util.List;

public class Session {

    private final SharedPreferences prefs;

    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setUserName(String userName) {
        prefs.edit().putString("userName", userName).commit();
    }

    public String getUserName() {
        return prefs.getString("userName","");
    }

    public void setPassword(String Password) {
        prefs.edit().putString("Password", Password).commit();
    }

    public String getPassword() {
        return prefs.getString("Password","");
    }

    public void setSaveState(String state) {
        prefs.edit().putString("state", state).commit();
    }

    public String getSaveState() {
        return prefs.getString("state","");
    }

    public void setName(String name) {
        prefs.edit().putString("name", name).commit();
    }

    public String getName() {
        return prefs.getString("name","");
    }

    public void setDob(String dob) {
        prefs.edit().putString("dob", dob).commit();
    }

    public String getDob() {
        return prefs.getString("dob","");
    }

    public void setGioitinh(String gioitinh) {
        prefs.edit().putString("gioitinh", gioitinh).commit();
    }

    public String getGioitinh() {
        return prefs.getString("gioitinh","");
    }

    public void logOut(){
        prefs.edit().putString("state", "").commit();
        prefs.edit().putString("userName", "").commit();
        prefs.edit().putString("name", "").commit();
        prefs.edit().putString("dob", "").commit();
        prefs.edit().putString("gioitinh", "").commit();
    }

    public void freshData(){
        prefs.edit().putString("name", "").commit();
        prefs.edit().putString("dob", "").commit();
        prefs.edit().putString("gioitinh", "").commit();
    }
}
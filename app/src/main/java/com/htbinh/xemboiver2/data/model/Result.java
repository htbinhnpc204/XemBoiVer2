package com.htbinh.xemboiver2.data.model;

public class Result {
    private String pName, pAge, pDoB, banMenh;

    public Result(String pName, String pAge, String pDoB, String banMenh) {
        this.pName = pName;
        this.pAge = pAge;
        this.pDoB = pDoB;
        this.banMenh = banMenh;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getpAge() {
        return pAge;
    }

    public void setpAge(String pAge) {
        this.pAge = pAge;
    }

    public String getpDoB() {
        return pDoB;
    }

    public void setpDoB(String pDoB) {
        this.pDoB = pDoB;
    }

    public String getBanMenh() {
        return banMenh;
    }

    public void setBanMenh(String banMenh) {
        this.banMenh = banMenh;
    }
}

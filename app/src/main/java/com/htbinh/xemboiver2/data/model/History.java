package com.htbinh.xemboiver2.data.model;

public class History {
    Integer id;
    String tenNguoiAy, dobNguoiAy;
    String chitiet;

    public History(Integer id, String tenNguoiAy, String dobNguoiAy, String chitiet) {
        this.id = id;
        this.tenNguoiAy = tenNguoiAy;
        this.dobNguoiAy = dobNguoiAy;
        this.chitiet = chitiet;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTenNguoiAy() {
        return tenNguoiAy;
    }

    public void setTenNguoiAy(String tenNguoiAy) {
        this.tenNguoiAy = tenNguoiAy;
    }

    public String getDobNguoiAy() {
        return dobNguoiAy;
    }

    public void setDobNguoiAy(String dobNguoiAy) {
        this.dobNguoiAy = dobNguoiAy;
    }

    public String getChitiet() {
        return chitiet;
    }

    public void setChitiet(String chitiet) {
        this.chitiet = chitiet;
    }
}

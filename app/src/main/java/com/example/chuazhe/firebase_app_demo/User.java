package com.example.chuazhe.firebase_app_demo;

public class User {
    private String Id;
    private String name;
    private int matricNo;
    private int MyCSD;
    private String email;

    public void setId(String Id) {
        this.Id = Id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMatricNo(int matricNo) {
        this.matricNo = matricNo;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMyCSD(int MyCSD) {
        this.MyCSD = MyCSD;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getMatricNo() {
        return matricNo;
    }

    public int getMyCSD() {
        return MyCSD;
    }

    public String getID() {
        return Id;
    }

}

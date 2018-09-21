package com.example.chuazhe.firebase_app_demo;

class Report {

    private String nameStudent;
    private String matricNo;
    private String ID;
    private String email;
    private String details;
    private String problemType;
    private String submitDate;
    private String submitTime;
    boolean read;

    public String getName() {
        return nameStudent;
    }

    public void setName(String studName) {
        this.nameStudent = studName;
    }

    public boolean getRead() {
        return read;
    }

    public void setRead(Boolean inputRead) {
        this.read = inputRead;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String studemail) {
        this.email = studemail;
    }

    public String getMatricNo() {
        return matricNo;
    }

    public void setMatricNo(String inputmatricNo) {
        this.matricNo = inputmatricNo;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String inputID) {
        this.details = inputID;
    }

    public String getProblemType() {
        return problemType;
    }

    public void setProblemType(String inputProblemType) {
        this.problemType = inputProblemType;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String inputSubmitTime) {
        this.submitTime = inputSubmitTime;
    }

    public String getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(String inputSubmitDate) {
        this.submitDate = inputSubmitDate;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }


}
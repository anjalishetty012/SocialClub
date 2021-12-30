package com.example.socialclub;

public class chatModel {
    private  String sms;
    private String status;
    private String userID;

    public chatModel() {
    }

    public chatModel(String sms, String status, String userID) {
        this.sms = sms;
        this.status = status;
        this.userID = userID;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}

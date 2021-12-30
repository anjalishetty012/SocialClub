package com.example.socialclub;

public class Member {
    private String fName,email,image;
    private  Member(){

    }

    public Member(String fName, String email, String image) {
        this.fName = fName;
        this.email = email;
        this.image = image;
    }



    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

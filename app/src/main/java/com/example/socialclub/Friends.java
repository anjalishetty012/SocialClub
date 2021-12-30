package com.example.socialclub;

public class Friends {
    String username,image,status;

    public Friends(String username, String image, String status) {
        this.username = username;
        this.image = image;
        this.status = status;
    }

    public Friends() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

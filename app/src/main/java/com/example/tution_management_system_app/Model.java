package com.example.tution_management_system_app;

public class Model {

    private String imageUrl,Name,Email,Grade;

    Model(){

    }

    public Model(String imageUrl,String Name,String Email,String Grade){
        this.imageUrl = imageUrl;
        this.Name = Name;
        this.Email = Email;
        this.Grade = Grade;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getGrade() {
        return Grade;
    }

    public void setGrade(String grade) {
        Grade = grade;
    }
}

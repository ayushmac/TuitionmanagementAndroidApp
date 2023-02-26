package com.example.tution_management_system_app;

public class StudentInserter {
    private String imageUrl;
    private String Name;
    private String Email;
    private String Dob;
    private String Contact_no;
    private String Contact_no_2;
    private String Grade;
    private String Address;
    private String Username;
    private String Password;
    private String Sid;

    public StudentInserter(){

    }

    public StudentInserter(String imageUrl, String Name, String Email, String Dob, String Contact_no,String Contact_no_2, String Grade, String Address, String Username, String Password,String Sid ){
        this.imageUrl = imageUrl;
        this.Name = Name;
        this.Email = Email;
        this.Dob = Dob;
        this.Contact_no = Contact_no;
        this.Contact_no_2 = Contact_no_2;
        this.Grade = Grade;
        this.Address = Address;
        this.Username = Username;
        this.Password = Password;
        this.Sid = Sid;
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

    public String getDob() {
        return Dob;
    }

    public void setDob(String dob) {
        Dob = dob;
    }

    public String getContact_no() {
        return Contact_no;
    }


    public String getContact_no_2() {
        return Contact_no_2;
    }

    public void setContact_no_2(String contact_no_2) {
        Contact_no_2 = contact_no_2;
    }

    public void setContact_no(String contact_no) {
        Contact_no = contact_no;
    }

    public String getGrade() {
        return Grade;
    }

    public void setGrade(String grade) {
        Grade = grade;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getSid() {
        return Sid;
    }

    public void setSid(String sid) {
        Sid = sid;
    }
}

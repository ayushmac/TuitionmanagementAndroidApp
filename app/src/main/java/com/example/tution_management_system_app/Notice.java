package com.example.tution_management_system_app;

public class Notice {
    private String imageUrl;
    private String notice_description;
    private String notice_title;
    private String notice_id;

    public Notice(){

    }
    public Notice(String imageUrl, String notice_description, String notice_title, String notice_id) {
        this.imageUrl = imageUrl;
        this.notice_description = notice_description;
        this.notice_title = notice_title;
        this.notice_id = notice_id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNotice_description() {
        return notice_description;
    }

    public void setNotice_description(String notice_description) {
        this.notice_description = notice_description;
    }

    public String getNotice_title() {
        return notice_title;
    }

    public void setNotice_title(String notice_title) {
        this.notice_title = notice_title;
    }

    public String getNotice_id() {
        return notice_id;
    }

    public void setNotice_id(String notice_id) {
        this.notice_id = notice_id;
    }
}

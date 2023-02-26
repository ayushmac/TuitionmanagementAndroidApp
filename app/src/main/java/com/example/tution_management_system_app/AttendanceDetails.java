package com.example.tution_management_system_app;

public class AttendanceDetails {
    String date;
    String status;
    String student_id;
    String student_name;
    String attendance_id;

    public AttendanceDetails(){

    }
    public AttendanceDetails(String attendance_id,String date, String status, String student_id, String student_name) {
        this.date = date;
        this.status = status;
        this.student_id = student_id;
        this.student_name = student_name;
        this.attendance_id = attendance_id;
    }

    public String getAttendance_id() {
        return attendance_id;
    }

    public void setAttendance_id(String attendance_id) {
        this.attendance_id = attendance_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }
}

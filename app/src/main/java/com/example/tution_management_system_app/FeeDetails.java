package com.example.tution_management_system_app;

public class FeeDetails {
    String amount_paid;
    String date_of_payment;
    String fee_id;
    String status;
    String student_id;
    String student_name;

    public FeeDetails() {

    }

    public FeeDetails(String amount_paid, String date_of_payment, String fee_id, String status, String student_id, String student_name) {
        this.amount_paid = amount_paid;
        this.date_of_payment = date_of_payment;
        this.fee_id = fee_id;
        this.status = status;
        this.student_id = student_id;
        this.student_name = student_name;
    }

    public String getAmount_paid() {
        return amount_paid;
    }

    public void setAmount_paid(String amount_paid) {
        this.amount_paid = amount_paid;
    }

    public String getDate_of_payment() {
        return date_of_payment;
    }

    public void setDate_of_payment(String date_of_payment) {
        this.date_of_payment = date_of_payment;
    }

    public String getFee_id() {
        return fee_id;
    }

    public void setFee_id(String fee_id) {
        this.fee_id = fee_id;
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

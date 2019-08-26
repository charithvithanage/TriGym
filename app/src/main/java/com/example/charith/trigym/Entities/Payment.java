package com.example.charith.trigym.Entities;

public class Payment {
    Integer id;
    Float amount;
    String type;
    Integer member_id;
    String lastPaymentDate;
    String paymentExpiryDate;
    String created_at;
    String modified_at;

    public String getLastPaymentDate() {
        return lastPaymentDate;
    }

    public void setLastPaymentDate(String lastPaymentDate) {
        this.lastPaymentDate = lastPaymentDate;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getModified_at() {
        return modified_at;
    }

    public void setModified_at(String modified_at) {
        this.modified_at = modified_at;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getMember_id() {
        return member_id;
    }

    public void setMember_id(Integer member_id) {
        this.member_id = member_id;
    }

    public String getLast_payment_date() {
        return lastPaymentDate;
    }

    public void setLast_payment_date(String lastPaymentDate) {
        this.lastPaymentDate = lastPaymentDate;
    }

    public String getPaymentExpiryDate() {
        return paymentExpiryDate;
    }

    public void setPaymentExpiryDate(String paymentExpiryDate) {
        this.paymentExpiryDate = paymentExpiryDate;
    }
}

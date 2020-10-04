package com.example.charith.trigym.Entities;

public class Member {

    Long member_id;
    String member_membership_no;
    String member_receipt_no;
    String type;
    String category;
    String member_first_name;
    String member_last_name;
    String member_surname;
    String guardian_name;
    String member_married_status;
    Integer guardian_tel;
    String guardian_relationship;
    Integer member_mobile_1;
    Integer member_mobile_2;
    String email;
    String member_nic;
    String member_dob;
    Integer member_age;
    String member_gender;
    Float member_height;
    Float member_weight;
    String member_profile_image_url;
    String member_health_condition;
    String membership_type;
    String last_payment_date;
    String membership_expiry_date;
    Boolean member_valid_status;
    Boolean member_active_status;
    String address_line_1;
    String address_line_2;
    String address_line_3;
    String city;

    boolean selected=false;

    String created_at;
    String modified_at;

    public String getAddress_line_3() {
        return address_line_3;
    }

    public void setAddress_line_3(String address_line_3) {
        this.address_line_3 = address_line_3;
    }

    public String getMembership_expiry_date() {
        return membership_expiry_date;
    }

    public String getAddress_line_1() {
        return address_line_1;
    }

    public void setAddress_line_1(String address_line_1) {
        this.address_line_1 = address_line_1;
    }

    public String getAddress_line_2() {
        return address_line_2;
    }

    public void setAddress_line_2(String address_line_2) {
        this.address_line_2 = address_line_2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getMember_id() {
        return member_id;
    }

    public void setMember_id(Long member_id) {
        this.member_id = member_id;
    }

    public String getMember_membership_no() {
        return member_membership_no;
    }

    public void setMember_membership_no(String member_membership_no) {
        this.member_membership_no = member_membership_no;
    }

    public String getMember_receipt_no() {
        return member_receipt_no;
    }

    public void setMember_receipt_no(String member_receipt_no) {
        this.member_receipt_no = member_receipt_no;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMember_first_name() {
        return member_first_name;
    }

    public void setMember_first_name(String member_first_name) {
        this.member_first_name = member_first_name;
    }

    public String getMember_last_name() {
        return member_last_name;
    }

    public void setMember_last_name(String member_last_name) {
        this.member_last_name = member_last_name;
    }

    public String getMember_surname() {
        return member_surname;
    }

    public void setMember_surname(String member_surname) {
        this.member_surname = member_surname;
    }

    public String getGuardian_name() {
        return guardian_name;
    }

    public void setGuardian_name(String guardian_name) {
        this.guardian_name = guardian_name;
    }

    public String getMember_married_status() {
        return member_married_status;
    }

    public void setMember_married_status(String member_married_status) {
        this.member_married_status = member_married_status;
    }

    public Integer getGuardian_tel() {
        return guardian_tel;
    }

    public void setGuardian_tel(Integer guardian_tel) {
        this.guardian_tel = guardian_tel;
    }

    public String getGuardian_relationship() {
        return guardian_relationship;
    }

    public void setGuardian_relationship(String guardian_relationship) {
        this.guardian_relationship = guardian_relationship;
    }

    public Integer getMember_mobile_1() {
        return member_mobile_1;
    }

    public void setMember_mobile_1(Integer member_mobile_1) {
        this.member_mobile_1 = member_mobile_1;
    }

    public Integer getMember_mobile_2() {
        return member_mobile_2;
    }

    public void setMember_mobile_2(Integer member_mobile_2) {
        this.member_mobile_2 = member_mobile_2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMember_nic() {
        return member_nic;
    }

    public void setMember_nic(String member_nic) {
        this.member_nic = member_nic;
    }

    public String getMember_dob() {
        return member_dob;
    }

    public void setMember_dob(String member_dob) {
        this.member_dob = member_dob;
    }

    public Integer getMember_age() {
        return member_age;
    }

    public void setMember_age(Integer member_age) {
        this.member_age = member_age;
    }

    public String getMember_gender() {
        return member_gender;
    }

    public void setMember_gender(String member_gender) {
        this.member_gender = member_gender;
    }

    public Float getMember_height() {
        return member_height;
    }

    public void setMember_height(Float member_height) {
        this.member_height = member_height;
    }

    public Float getMember_weight() {
        return member_weight;
    }

    public void setMember_weight(Float member_weight) {
        this.member_weight = member_weight;
    }

    public String getMember_profile_image_url() {
        return member_profile_image_url;
    }

    public void setMember_profile_image_url(String member_profile_image_url) {
        this.member_profile_image_url = member_profile_image_url;
    }

    public String getMember_health_condition() {
        return member_health_condition;
    }

    public void setMember_health_condition(String member_health_condition) {
        this.member_health_condition = member_health_condition;
    }

    public String getMembership_type() {
        return membership_type;
    }

    public void setMembership_type(String membership_type) {
        this.membership_type = membership_type;
    }

    public String getLast_payment_date() {
        return last_payment_date;
    }

    public void setLast_payment_date(String last_payment_date) {
        this.last_payment_date = last_payment_date;
    }

    public String getPaymentExpiryDate() {
        return membership_expiry_date;
    }

    public void setMembership_expiry_date(String membership_expiry_date) {
        this.membership_expiry_date = membership_expiry_date;
    }

    public Boolean getMember_valid_status() {
        return member_valid_status;
    }

    public void setMember_valid_status(Boolean member_valid_status) {
        this.member_valid_status = member_valid_status;
    }

    public Boolean getMember_active_status() {
        return member_active_status;
    }

    public void setMember_active_status(Boolean member_active_status) {
        this.member_active_status = member_active_status;
    }




    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
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
}

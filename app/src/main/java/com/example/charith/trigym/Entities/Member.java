package com.example.charith.trigym.Entities;

import org.joda.time.DateTime;

import java.util.Map;

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
    Integer member_address_id;
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

    Boolean diabetes=false;
    Boolean cholesterol=false;
    Boolean high_blood_pressure=false;
    Boolean low_blood_pressure=false;
    Boolean heart_problem=false;
    Boolean chest_pain=false;
    Boolean heart_attack=false;
    Boolean asthma=false;
    Boolean fainting_spells=false;
    Boolean back_pain=false;
    Boolean medication=false;
    Boolean other_illness=false;
    Boolean swollen=false;
    Boolean arthritis=false;
    Boolean hernia=false;

    boolean selected=false;

    String created_at;
    String modified_at;

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

    public Integer getMember_address_id() {
        return member_address_id;
    }

    public void setMember_address_id(Integer member_address_id) {
        this.member_address_id = member_address_id;
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

    public String getMembership_expiry_date() {
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

    public Boolean getDiabetes() {
        return diabetes;
    }

    public void setDiabetes(Boolean diabetes) {
        this.diabetes = diabetes;
    }

    public Boolean getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(Boolean cholesterol) {
        this.cholesterol = cholesterol;
    }

    public Boolean getHigh_blood_pressure() {
        return high_blood_pressure;
    }

    public void setHigh_blood_pressure(Boolean high_blood_pressure) {
        this.high_blood_pressure = high_blood_pressure;
    }

    public Boolean getLow_blood_pressure() {
        return low_blood_pressure;
    }

    public void setLow_blood_pressure(Boolean low_blood_pressure) {
        this.low_blood_pressure = low_blood_pressure;
    }

    public Boolean getHeart_problem() {
        return heart_problem;
    }

    public void setHeart_problem(Boolean heart_problem) {
        this.heart_problem = heart_problem;
    }

    public Boolean getChest_pain() {
        return chest_pain;
    }

    public void setChest_pain(Boolean chest_pain) {
        this.chest_pain = chest_pain;
    }

    public Boolean getHeart_attack() {
        return heart_attack;
    }

    public void setHeart_attack(Boolean heart_attack) {
        this.heart_attack = heart_attack;
    }

    public Boolean getAsthma() {
        return asthma;
    }

    public void setAsthma(Boolean asthma) {
        this.asthma = asthma;
    }

    public Boolean getFainting_spells() {
        return fainting_spells;
    }

    public void setFainting_spells(Boolean fainting_spells) {
        this.fainting_spells = fainting_spells;
    }

    public Boolean getBack_pain() {
        return back_pain;
    }

    public void setBack_pain(Boolean back_pain) {
        this.back_pain = back_pain;
    }

    public Boolean getMedication() {
        return medication;
    }

    public void setMedication(Boolean medication) {
        this.medication = medication;
    }

    public Boolean getOther_illness() {
        return other_illness;
    }

    public void setOther_illness(Boolean other_illness) {
        this.other_illness = other_illness;
    }

    public Boolean getSwollen() {
        return swollen;
    }

    public void setSwollen(Boolean swollen) {
        this.swollen = swollen;
    }

    public Boolean getArthritis() {
        return arthritis;
    }

    public void setArthritis(Boolean arthritis) {
        this.arthritis = arthritis;
    }

    public Boolean getHernia() {
        return hernia;
    }

    public void setHernia(Boolean hernia) {
        this.hernia = hernia;
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

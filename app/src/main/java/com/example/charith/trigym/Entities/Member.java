package com.example.charith.trigym.Entities;

import org.joda.time.DateTime;

import java.util.Map;

public class Member {

    Integer id;
    String membershipNo;
    String membershipRecieptNo;
    String registeredDate;
    String type;
    String category;
    String firstName;
    String lastName;
    String surName;
    String guardianName;
    String MarriedStatus;
    Integer guardianTel;
    String guardianRelationship;
    Address address;
    Integer mobile1;
    Integer mobile2;
    String email;
    String NIC;
    String DOB;
    Integer age;
    String gender;
    Float height;
    Float weight;
    String profileImage;
    String comments;
    Integer addressId;
    String membershipType;
    String lastPaymentDate;
    String membershipExpiredDate;
    Boolean validStatus;
    Boolean activeStatus;

    Boolean diabetes;
    Boolean cholesterol;
    Boolean highBloodPressure;
    Boolean lowBloodPressure;
    Boolean heartProblem;
    Boolean painInChestWhenExercising;
    Boolean heartAttackCoronaryBypass;
    Boolean anyBreathingDifficultiesAndAsthma;
    Boolean faintingSpells;
    Boolean backOrSpinePains;
    Boolean areYouOnAnySortOfMedications;
    Boolean otherSignificantIllness;
    Boolean swollen;
    Boolean arthritis;
    Boolean hernia;

    boolean selected;

    public String getCategory() {
        return category;
    }

    public Boolean getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(Boolean activeStatus) {
        this.activeStatus = activeStatus;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Boolean getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(Boolean validStatus) {
        this.validStatus = validStatus;
    }

    public String getMembershipExpiredDate() {
        return membershipExpiredDate;
    }

    public void setMembershipExpiredDate(String membershipExpiredDate) {
        this.membershipExpiredDate = membershipExpiredDate;
    }

    public String getLastPaymentDate() {
        return lastPaymentDate;
    }

    public void setLastPaymentDate(String lastPaymentDate) {
        this.lastPaymentDate = lastPaymentDate;
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

    public Boolean getHighBloodPressure() {
        return highBloodPressure;
    }

    public void setHighBloodPressure(Boolean highBloodPressure) {
        this.highBloodPressure = highBloodPressure;
    }

    public Boolean getLowBloodPressure() {
        return lowBloodPressure;
    }

    public void setLowBloodPressure(Boolean lowBloodPressure) {
        this.lowBloodPressure = lowBloodPressure;
    }

    public Boolean getHeartProblem() {
        return heartProblem;
    }

    public void setHeartProblem(Boolean heartProblem) {
        this.heartProblem = heartProblem;
    }

    public Boolean getPainInChestWhenExercising() {
        return painInChestWhenExercising;
    }

    public void setPainInChestWhenExercising(Boolean painInChestWhenExercising) {
        this.painInChestWhenExercising = painInChestWhenExercising;
    }

    public Boolean getHeartAttackCoronaryBypass() {
        return heartAttackCoronaryBypass;
    }

    public void setHeartAttackCoronaryBypass(Boolean heartAttackCoronaryBypass) {
        this.heartAttackCoronaryBypass = heartAttackCoronaryBypass;
    }

    public Boolean getAnyBreathingDifficultiesAndAsthma() {
        return anyBreathingDifficultiesAndAsthma;
    }

    public void setAnyBreathingDifficultiesAndAsthma(Boolean anyBreathingDifficultiesAndAsthma) {
        this.anyBreathingDifficultiesAndAsthma = anyBreathingDifficultiesAndAsthma;
    }

    public Boolean getFaintingSpells() {
        return faintingSpells;
    }

    public void setFaintingSpells(Boolean faintingSpells) {
        this.faintingSpells = faintingSpells;
    }

    public Boolean getBackOrSpinePains() {
        return backOrSpinePains;
    }

    public void setBackOrSpinePains(Boolean backOrSpinePains) {
        this.backOrSpinePains = backOrSpinePains;
    }

    public Boolean getAreYouOnAnySortOfMedications() {
        return areYouOnAnySortOfMedications;
    }

    public void setAreYouOnAnySortOfMedications(Boolean areYouOnAnySortOfMedications) {
        this.areYouOnAnySortOfMedications = areYouOnAnySortOfMedications;
    }

    public Boolean getOtherSignificantIllness() {
        return otherSignificantIllness;
    }

    public void setOtherSignificantIllness(Boolean otherSignificantIllness) {
        this.otherSignificantIllness = otherSignificantIllness;
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

    public String getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(String membershipType) {
        this.membershipType = membershipType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMembershipNo() {
        return membershipNo;
    }

    public void setMembershipNo(String membershipNo) {
        this.membershipNo = membershipNo;
    }

    public String getMembershipRecieptNo() {
        return membershipRecieptNo;
    }

    public void setMembershipRecieptNo(String membershipRecieptNo) {
        this.membershipRecieptNo = membershipRecieptNo;
    }

    public String getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(String registeredDate) {
        this.registeredDate = registeredDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public String getMarriedStatus() {
        return MarriedStatus;
    }

    public void setMarriedStatus(String marriedStatus) {
        MarriedStatus = marriedStatus;
    }

    public Integer getGuardianTel() {
        return guardianTel;
    }

    public void setGuardianTel(Integer guardianTel) {
        this.guardianTel = guardianTel;
    }

    public String getGuardianRelationship() {
        return guardianRelationship;
    }

    public void setGuardianRelationship(String guardianRelationship) {
        this.guardianRelationship = guardianRelationship;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Integer getMobile1() {
        return mobile1;
    }

    public void setMobile1(Integer mobile1) {
        this.mobile1 = mobile1;
    }

    public Integer getMobile2() {
        return mobile2;
    }

    public void setMobile2(Integer mobile2) {
        this.mobile2 = mobile2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNIC() {
        return NIC;
    }

    public void setNIC(String NIC) {
        this.NIC = NIC;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }
}

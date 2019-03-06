package com.example.charith.trigym.Entities;

import org.joda.time.DateTime;

import java.util.Map;

public class Member {

    int id;
    String name;
    Address address;
    Integer mobile1;
    Integer mobile2;
    String NIC;
    DateTime DOB;
    Integer age;
    String gender;
    Float height;
    Float weight;
    Map<String,String> images;
    String healthCondition;

    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getNIC() {
        return NIC;
    }

    public void setNIC(String NIC) {
        this.NIC = NIC;
    }

    public DateTime getDOB() {
        return DOB;
    }

    public void setDOB(DateTime DOB) {
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

    public Map<String, String> getImages() {
        return images;
    }

    public void setImages(Map<String, String> images) {
        this.images = images;
    }

    public String getHealthCondition() {
        return healthCondition;
    }

    public void setHealthCondition(String healthCondition) {
        this.healthCondition = healthCondition;
    }
}

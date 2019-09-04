package com.example.charith.trigym.Entities;

public class HealthCondition {
    Long health_condition_id;
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


    String created_at;
    String modified_at;

    public Long getHealth_condition_id() {
        return health_condition_id;
    }

    public void setHealth_condition_id(Long health_condition_id) {
        this.health_condition_id = health_condition_id;
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

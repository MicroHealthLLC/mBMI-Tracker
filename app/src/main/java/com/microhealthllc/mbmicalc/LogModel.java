package com.microhealthllc.mbmicalc;

/**
 * Created by dan on 3/20/17.
 */


public class LogModel {
    private String bmi, weight, datetime;

    public LogModel() {
    }

    public LogModel(String bmi, String weight, String datetime) {
        this.bmi= bmi;
        this.weight = weight;
        this.datetime = datetime;
    }

    public String getBmi() {
        return  bmi;
    }

    public void setBMi(String bmi) {
        this.bmi = bmi;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
package com.microhealthllc.mbmicalc.DB;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ubuntuadmin on 3/13/17.
 */

public class BmiLogs {
    private int id;
    private String bmi;
    private String weight;
    private String datetime;

    public BmiLogs() {
    }

    public BmiLogs(int id,String bmi,String weight,String datetime)
    {
        this.id=id;
        this.bmi=bmi;
        this.weight=weight;
        this.datetime =datetime;
    }

    public BmiLogs(String bmi,String weight, String datetime)
    {

        this.bmi=bmi;
        this.weight=weight;
        this.datetime =datetime;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setBmi(String bmi) {
        this.bmi = bmi;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
    public void setDateTime(String datetime){
        this.datetime = datetime;
    }
    public int getId() {
        return id;
    }
    public String getBmi() {
        return bmi;
    }
    public String getWeight() {
        return weight;
    }
    public String getDateTime() {
       return datetime;
    }
}


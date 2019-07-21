package com.asmaa.googlecalendertask.model;

public class Events  {

    String EVENT ,TIME,DARE ,MONTH,YEAR ;

    public Events(String EVENT, String TIME, String DARE, String MONTH, String YEAR) {
        this.EVENT = EVENT;
        this.TIME = TIME;
        this.DARE = DARE;
        this.MONTH = MONTH;
        this.YEAR = YEAR;
    }


    public String getEVENT() {
        return EVENT;
    }

    public void setEVENT(String EVENT) {
        this.EVENT = EVENT;
    }

    public String getTIME() {
        return TIME;
    }

    public void setTIME(String TIME) {
        this.TIME = TIME;
    }

    public String getDATE() {
        return DARE;
    }

    public void setDARE(String DARE) {
        this.DARE = DARE;
    }

    public String getMONTH() {
        return MONTH;
    }

    public void setMONTH(String MONTH) {
        this.MONTH = MONTH;
    }

    public String getYEAR() {
        return YEAR;
    }

    public void setYEAR(String YEAR) {
        this.YEAR = YEAR;
    }
}

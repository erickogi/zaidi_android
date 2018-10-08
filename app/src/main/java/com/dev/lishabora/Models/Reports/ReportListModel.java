package com.dev.lishabora.Models.Reports;

import java.io.Serializable;

public class ReportListModel implements Serializable {
    private String collectionCode;
    private String day;
    private String date;
    private String value1;
    private String value2;

    private String valueMilkAm;
    private String valueMilkPm;

    public ReportListModel(String collectionCode, String day, String date, String value1, String value2) {
        this.collectionCode = collectionCode;
        this.day = day;
        this.date = date;
        this.value1 = value1;
        this.value2 = value2;
    }

    public ReportListModel(String collectionCode, String day, String date, String value1, String value2, String valueMilkAm, String valueMilkPm) {
        this.collectionCode = collectionCode;
        this.day = day;
        this.date = date;
        this.value1 = value1;
        this.value2 = value2;
        this.valueMilkAm = valueMilkAm;
        this.valueMilkPm = valueMilkPm;
    }

    public String getValueMilkAm() {
        return valueMilkAm;
    }

    public void setValueMilkAm(String valueMilkAm) {
        this.valueMilkAm = valueMilkAm;
    }

    public String getValueMilkPm() {
        return valueMilkPm;
    }

    public void setValueMilkPm(String valueMilkPm) {
        this.valueMilkPm = valueMilkPm;
    }

    public String getCollectionId() {
        return collectionCode;
    }

    public void setCollectionId(String collectionCode) {
        this.collectionCode = collectionCode;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }
}

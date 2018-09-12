package com.dev.lishabora.Models.Reports;

import java.io.Serializable;

public class ReportListModel implements Serializable {
    private int collectionId;
    private String day;
    private String date;
    private String value1;
    private String value2;

    public ReportListModel(int collectionId, String day, String date, String value1, String value2) {
        this.collectionId = collectionId;
        this.day = day;
        this.date = date;
        this.value1 = value1;
        this.value2 = value2;
    }

    public int getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(int collectionId) {
        this.collectionId = collectionId;
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

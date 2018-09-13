package com.dev.lishabora.Models;

public class MilkCollectionsModel {
    private String date;
    private String amValue;
    private String pmValue;

    public MilkCollectionsModel(String date, String amValue, String pmValue) {
        this.date = date;
        this.amValue = amValue;
        this.pmValue = pmValue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmValue() {
        return amValue;
    }

    public void setAmValue(String amValue) {
        this.amValue = amValue;
    }

    public String getPmValue() {
        return pmValue;
    }

    public void setPmValue(String pmValue) {
        this.pmValue = pmValue;
    }
}

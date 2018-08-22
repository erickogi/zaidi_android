package com.dev.lishabora.Models;

public class MonthsDates {
    private String monthName;
    private String monthStartDate;
    private String monthEndDate;


    public MonthsDates(String monthName, String monthStartDate, String monthEndDate) {
        this.monthName = monthName;
        this.monthStartDate = monthStartDate;
        this.monthEndDate = monthEndDate;
    }

    public MonthsDates() {
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public String getMonthStartDate() {
        return monthStartDate;
    }

    public void setMonthStartDate(String monthStartDate) {
        this.monthStartDate = monthStartDate;
    }

    public String getMonthEndDate() {
        return monthEndDate;
    }

    public void setMonthEndDate(String monthEndDate) {
        this.monthEndDate = monthEndDate;
    }
}

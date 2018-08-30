package com.dev.lishabora.Models;

import java.io.Serializable;

public class DayCollectionModel implements Serializable {


    private int collectionIdAm;
    private int collectionIdPm;


    private int payoutNumber;

    private String day;
    private String date;

    private String milkAm;
    private String milkPm;

    private String loanAm;
    private String loanPm;

    private String orderAm;
    private String orderPm;


    private String amPmLoan;
    private String amPmOrders;

    public String getAmPmLoan() {
        String amPm = String.valueOf(Double.valueOf(loanAm) + Double.valueOf(loanPm));
        return amPm;
    }

    public void setAmPmLoan(String amPmLoan) {
        this.amPmLoan = amPmLoan;
    }

    public String getAmPmOrders() {
        String amPm = String.valueOf(Double.valueOf(orderAm) + Double.valueOf(orderPm));
        return amPm;
    }

    public void setAmPmOrders(String amPmOrders) {
        this.amPmOrders = amPmOrders;
    }

    public DayCollectionModel() {
    }

    public DayCollectionModel(int payoutNumber, String day, String date, String milkAm, String milkPm,
                              String loanAm, String loanPm, String orderAm, String orderPm, int collectionIdAm, int collectionIdPm) {
        this.payoutNumber = payoutNumber;
        this.day = day;
        this.date = date;
        this.milkAm = milkAm;
        this.milkPm = milkPm;
        this.loanAm = loanAm;
        this.loanPm = loanPm;
        this.orderAm = orderAm;
        this.orderPm = orderPm;
        this.collectionIdAm = collectionIdAm;
        this.collectionIdPm = collectionIdPm;

    }


    public int getCollectionIdAm() {
        return collectionIdAm;
    }

    public void setCollectionIdAm(int collectionIdAm) {
        this.collectionIdAm = collectionIdAm;
    }

    public int getCollectionIdPm() {
        return collectionIdPm;
    }

    public void setCollectionIdPm(int collectionIdPm) {
        this.collectionIdPm = collectionIdPm;
    }

    public int getPayoutNumber() {
        return payoutNumber;
    }

    public void setPayoutNumber(int payoutNumber) {
        this.payoutNumber = payoutNumber;
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

    public String getMilkAm() {
        return milkAm;
    }

    public void setMilkAm(String milkAm) {
        this.milkAm = milkAm;
    }

    public String getMilkPm() {
        return milkPm;
    }

    public void setMilkPm(String milkPm) {
        this.milkPm = milkPm;
    }

    public String getLoanAm() {
        return loanAm;
    }

    public void setLoanAm(String loanAm) {
        this.loanAm = loanAm;
    }

    public String getLoanPm() {
        return loanPm;
    }

    public void setLoanPm(String loanPm) {
        this.loanPm = loanPm;
    }

    public String getOrderAm() {
        return orderAm;
    }

    public void setOrderAm(String orderAm) {
        this.orderAm = orderAm;
    }

    public String getOrderPm() {
        return orderPm;
    }

    public void setOrderPm(String orderPm) {
        this.orderPm = orderPm;
    }
}

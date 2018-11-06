package com.dev.lishabora.Models;

import java.io.Serializable;

public class DayCollectionModel implements Serializable {


    private String collectionCode;


    private String payoutCode;

    private String day;
    private String date;

    private String milkAm;
    private String milkPm;


    private String milkAmLtrs;
    private String milkAmKsh;
    private String milkPmKsh;
    private String milkPmLtrs;


    private MilkModel milkModelAm;
    private MilkModel milkModelPm;

    private String loan;
    private LoanModel loanModel;

    private String order;
    private OrderModel orderModel;

    private int payoutStatus;
    private int collectionStatus;


    public DayCollectionModel(String payoutCode, String day, String date, String milkAm, String milkPm
            , String collectionCode,
                              MilkModel milkModelAm,
                              MilkModel milkModelPm, String loan, String order,
                              String loanCollectionCode, LoanModel loanModel, String orderCollectionCode, OrderModel orderModel, int payoutStatus) {
        this.payoutCode = payoutCode;
        this.day = day;
        this.date = date;
        this.milkAm = milkAm;
        this.milkPm = milkPm;

        this.collectionCode = collectionCode;

        this.milkModelAm = milkModelAm;

        this.milkModelPm = milkModelPm;

        this.loan = loan;
        this.order = order;

        this.loanModel = loanModel;
        this.orderModel = orderModel;
        this.payoutStatus = payoutStatus;



    }

    public DayCollectionModel(String payoutCode, String day, String date, String milkAm, String milkPm
            , String collectionCode,
                              MilkModel milkModelAm,
                              MilkModel milkModelPm, String loan, String order,
                              String loanCollectionCode, LoanModel loanModel, String orderCollectionCode, OrderModel orderModel,
                              int payoutStatus, String milkAmLtrs, String milkAmKsh, String milkPmLtrs, String milkPmKsh, int collectionStatus) {
        this.payoutCode = payoutCode;
        this.day = day;
        this.date = date;
        this.milkAm = milkAm;
        this.milkPm = milkPm;

        this.collectionCode = collectionCode;

        this.milkModelAm = milkModelAm;

        this.milkModelPm = milkModelPm;

        this.loan = loan;
        this.order = order;

        this.loanModel = loanModel;
        this.orderModel = orderModel;
        this.payoutStatus = payoutStatus;


        this.milkAmLtrs = milkAmLtrs;
        this.milkAmKsh = milkAmKsh;

        this.milkPmLtrs = milkPmLtrs;
        this.milkPmKsh = milkPmKsh;
        this.collectionStatus = collectionStatus;


    }

    public int getCollectionStatus() {
        return collectionStatus;
    }

    public void setCollectionStatus(int collectionStatus) {
        this.collectionStatus = collectionStatus;
    }

    public String getMilkAmLtrs() {
        return milkAmLtrs;
    }

    public void setMilkAmLtrs(String milkAmLtrs) {
        this.milkAmLtrs = milkAmLtrs;
    }

    public String getMilkAmKsh() {
        return milkAmKsh;
    }

    public void setMilkAmKsh(String milkAmKsh) {
        this.milkAmKsh = milkAmKsh;
    }

    public String getMilkPmKsh() {
        return milkPmKsh;
    }

    public void setMilkPmKsh(String milkPmKsh) {
        this.milkPmKsh = milkPmKsh;
    }

    public String getMilkPmLtrs() {
        return milkPmLtrs;
    }

    public void setMilkPmLtrs(String milkPmLtrs) {
        this.milkPmLtrs = milkPmLtrs;
    }

    public int getPayoutStatus() {
        return payoutStatus;
    }

    public void setPayoutStatus(int payoutStatus) {
        this.payoutStatus = payoutStatus;
    }

    public MilkModel getMilkModelAm() {
        return milkModelAm;
    }

    public void setMilkModelAm(MilkModel milkModelAm) {
        this.milkModelAm = milkModelAm;
    }


    public MilkModel getMilkModelPm() {
        return milkModelPm;
    }

    public void setMilkModelPm(MilkModel milkModelPm) {
        this.milkModelPm = milkModelPm;
    }



    public DayCollectionModel() {
    }


    public String getCollectionCode() {
        return collectionCode;
    }

    public void setCollectionCode(String collectionCode) {
        this.collectionCode = collectionCode;
    }

    public String getPayoutCode() {
        return payoutCode;
    }

    public void setPayoutCode(String payoutCode) {
        this.payoutCode = payoutCode;
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


    public String getLoan() {
        return loan;
    }

    public void setLoan(String loan) {
        this.loan = loan;
    }

    public LoanModel getLoanModel() {
        return loanModel;
    }

    public void setLoanModel(LoanModel loanModel) {
        this.loanModel = loanModel;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public OrderModel getOrderModel() {
        return orderModel;
    }

    public void setOrderModel(OrderModel orderModel) {
        this.orderModel = orderModel;
    }
}

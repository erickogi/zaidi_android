package com.dev.lishabora.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "collectiontransactions")

public class Collection {
    @PrimaryKey(autoGenerate = true)
    private int id;


    private String farmerCode;
    private String farmerName;

    private String cycleCode;
    private String unitCode;


    private String dayName;
    private String dayDate;
    private String dayDateTime;


    private String payoutnumber;
    private String cycleId;

    private String cycleStartedOn;
    private String cycleEndsOn;


    private String milkCollected;
    private String milkCollectedPrice;
    private String milkCollectedId;//as in milk in table
    private String timeOfDay;//AM , PM
    private String unitOfCollection;//AM , PM


    private String loanAmountGivenOutPrice;//
    private String loanId;//as in loans out tables


    private String orderGivenOutPrice;
    private String orderId;//as in orders out table
    private String productId;//as in orders out table as in product table


    private String transactionTime;
    private int synced;
    private boolean isSynced;
    private String syncedId;//As in sync tble
    private String synctime;//As in sync tble


    private int approved;

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFarmerCode() {
        return farmerCode;
    }

    public void setFarmerCode(String farmerCode) {
        this.farmerCode = farmerCode;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public String getCycleCode() {
        return cycleCode;
    }

    public void setCycleCode(String cycleCode) {
        this.cycleCode = cycleCode;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getDayDate() {
        return dayDate;
    }

    public void setDayDate(String dayDate) {
        this.dayDate = dayDate;
    }

    public String getDayDateTime() {
        return dayDateTime;
    }

    public void setDayDateTime(String dayDateTime) {
        this.dayDateTime = dayDateTime;
    }

    public String getPayoutnumber() {
        return payoutnumber;
    }

    public void setPayoutnumber(String payoutnumber) {
        this.payoutnumber = payoutnumber;
    }

    public String getCycleStartedOn() {
        return cycleStartedOn;
    }

    public void setCycleStartedOn(String cycleStartedOn) {
        this.cycleStartedOn = cycleStartedOn;
    }

    public String getCycleEndsOn() {
        return cycleEndsOn;
    }

    public void setCycleEndsOn(String cycleEndsOn) {
        this.cycleEndsOn = cycleEndsOn;
    }

    public String getMilkCollected() {
        return milkCollected;
    }

    public void setMilkCollected(String milkCollected) {
        this.milkCollected = milkCollected;
    }

    public String getMilkCollectedPrice() {
        return milkCollectedPrice;
    }

    public void setMilkCollectedPrice(String milkCollectedPrice) {
        this.milkCollectedPrice = milkCollectedPrice;
    }

    public String getMilkCollectedId() {
        return milkCollectedId;
    }

    public void setMilkCollectedId(String milkCollectedId) {
        this.milkCollectedId = milkCollectedId;
    }

    public String getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(String timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public String getUnitOfCollection() {
        return unitOfCollection;
    }

    public void setUnitOfCollection(String unitOfCollection) {
        this.unitOfCollection = unitOfCollection;
    }

    public String getLoanAmountGivenOutPrice() {
        return loanAmountGivenOutPrice;
    }

    public void setLoanAmountGivenOutPrice(String loanAmountGivenOutPrice) {
        this.loanAmountGivenOutPrice = loanAmountGivenOutPrice;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getOrderGivenOutPrice() {
        return orderGivenOutPrice;
    }

    public void setOrderGivenOutPrice(String orderGivenOutPrice) {
        this.orderGivenOutPrice = orderGivenOutPrice;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public int getSynced() {
        return synced;
    }

    public void setSynced(int synced) {
        this.synced = synced;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean synced) {
        isSynced = synced;
    }

    public String getSyncedId() {
        return syncedId;
    }

    public void setSyncedId(String syncedId) {
        this.syncedId = syncedId;
    }

    public String getSynctime() {
        return synctime;
    }

    public void setSynctime(String synctime) {
        this.synctime = synctime;
    }

    public String getCycleId() {
        return cycleId;
    }

    public void setCycleId(String cycleId) {
        this.cycleId = cycleId;
    }
}

package com.dev.lishabora.Models.Trader;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "famersOrders")

public class FarmerOrdersTable {

    @PrimaryKey(autoGenerate = true)


    private int id;
    @Ignore
    private String traderCode;

    private String collectionCode;
    private String payoutCode;
    private String farmerCode;

    private String orderAmount;
    private String orderAmountPaid;
    private String installmentAmount;
    private String installmentNo;

    private int status;
    private String timestamp;

    private String code;

    public FarmerOrdersTable(String collectionId, String payoutId, String farmerCode, String orderAmount, String orderAmountPaid, String installmentAmount, String installmentNo, int status, String timestamp) {
        this.collectionCode = collectionId;
        this.collectionCode = payoutId;
        this.farmerCode = farmerCode;
        this.orderAmount = orderAmount;
        this.orderAmountPaid = orderAmountPaid;
        this.installmentAmount = installmentAmount;
        this.installmentNo = installmentNo;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

//    public FarmerOrdersTable(int collectionId, int payoutId, String farmerCode, String orderAmount, String installmentAmount, String installmentNo, int status, String timestamp) {
//        this.collectionId = collectionId;
//        this.payoutId = payoutId;
//        this.farmerCode = farmerCode;
//        this.orderAmount = orderAmount;
//        this.installmentAmount = installmentAmount;
//        this.installmentNo = installmentNo;
//        this.status = status;
//        this.timestamp = timestamp;
//    }


    public String getTraderCode() {
        return traderCode;
    }

    public void setTraderCode(String traderCode) {
        this.traderCode = traderCode;
    }

    public String getOrderAmountPaid() {
        return orderAmountPaid;
    }

    public void setOrderAmountPaid(String orderAmountPaid) {
        this.orderAmountPaid = orderAmountPaid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getFarmerCode() {
        return farmerCode;
    }

    public void setFarmerCode(String farmerCode) {
        this.farmerCode = farmerCode;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getInstallmentAmount() {
        return installmentAmount;
    }

    public void setInstallmentAmount(String installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    public String getInstallmentNo() {
        return installmentNo;
    }

    public void setInstallmentNo(String installmentNo) {
        this.installmentNo = installmentNo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

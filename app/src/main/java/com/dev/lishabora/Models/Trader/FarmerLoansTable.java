package com.dev.lishabora.Models.Trader;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "famersLoans")

public class FarmerLoansTable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @Ignore
    private String traderCode;

    private int collectionId;
    private int payoutId;
    private String farmerCode;

    private String loanAmount;

    private String loanAmountPaid;

    private String installmentAmount;
    private String installmentNo;

    private int status;
    private String timestamp;

//    public FarmerLoansTable(int collectionId, int payoutId, String farmerCode, String loanAmount, String installmentAmount, String installmentNo, int status, String timestamp) {
//        this.collectionId = collectionId;
//        this.payoutId = payoutId;
//        this.farmerCode = farmerCode;
//        this.loanAmount = loanAmount;
//        this.installmentAmount = installmentAmount;
//        this.installmentNo = installmentNo;
//        this.status = status;
//        this.timestamp = timestamp;
//    }


    public FarmerLoansTable() {
    }

    public FarmerLoansTable(int collectionId, int payoutId, String farmerCode, String loanAmount, String loanAmountPaid, String installmentAmount, String installmentNo, int status, String timestamp) {
        this.collectionId = collectionId;
        this.payoutId = payoutId;
        this.farmerCode = farmerCode;
        this.loanAmount = loanAmount;
        this.loanAmountPaid = loanAmountPaid;
        this.installmentAmount = installmentAmount;
        this.installmentNo = installmentNo;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getTraderCode() {
        return traderCode;
    }

    public void setTraderCode(String traderCode) {
        this.traderCode = traderCode;
    }

    public String getLoanAmountPaid() {
        return loanAmountPaid;
    }

    public void setLoanAmountPaid(String loanAmountPaid) {
        this.loanAmountPaid = loanAmountPaid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(int collectionId) {
        this.collectionId = collectionId;
    }

    public int getPayoutId() {
        return payoutId;
    }

    public void setPayoutId(int payoutId) {
        this.payoutId = payoutId;
    }

    public String getFarmerCode() {
        return farmerCode;
    }

    public void setFarmerCode(String farmerCode) {
        this.farmerCode = farmerCode;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
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

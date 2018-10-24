package com.dev.lishabora.Models.Trader;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "famersLoans", indices = {@Index(value = {"id", "code"}, unique = true)})

public class FarmerLoansTable implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String code;
    @Ignore
    private String traderCode;

    private String collectionCode;
    private String payoutCode;
    private String farmerCode;

    private String loanAmount;

    private String loanAmountPaid;

    private String installmentAmount;

    private String installmentNo;

    private int status;
    private String timestamp;


    public FarmerLoansTable(String code, String collectionCode, String payoutCode, String farmerCode, String loanAmount, String loanAmountPaid, String installmentAmount, String installmentNo, int status, String timestamp) {
        this.code = code;
        this.collectionCode = collectionCode;
        this.payoutCode = payoutCode;
        this.farmerCode = farmerCode;
        this.loanAmount = loanAmount;
        this.loanAmountPaid = loanAmountPaid;
        this.installmentAmount = installmentAmount;
        this.installmentNo = installmentNo;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getCode() {
        return code;
    }

    public FarmerLoansTable() {
    }

    public void setCode(String code) {
        this.code = code;
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

package com.dev.lishabora.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "approvalRegister", indices = {@Index(value = {"farmerCode", "payoutCode"}, unique = true)})

public class ApprovalRegisterModel {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String farmerCode;
    private String payoutCode;

    private String loanPaymentCode;
    private String orderPaymentCode;

    private String approvedOn;


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

    public String getPayoutCode() {
        return payoutCode;
    }

    public void setPayoutCode(String payoutCode) {
        this.payoutCode = payoutCode;
    }

    public String getLoanPaymentCode() {
        return loanPaymentCode;
    }

    public void setLoanPaymentCode(String loanPaymentCode) {
        this.loanPaymentCode = loanPaymentCode;
    }

    public String getOrderPaymentCode() {
        return orderPaymentCode;
    }

    public void setOrderPaymentCode(String orderPaymentCode) {
        this.orderPaymentCode = orderPaymentCode;
    }

    public String getApprovedOn() {
        return approvedOn;
    }

    public void setApprovedOn(String approvedOn) {
        this.approvedOn = approvedOn;
    }
}

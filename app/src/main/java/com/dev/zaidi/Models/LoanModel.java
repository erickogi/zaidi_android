package com.dev.zaidi.Models;

import java.io.Serializable;

public class LoanModel implements Serializable {
    private String loanAmount;
    private String installmentAmount;
    private String installmentsNo;
    private String collectionCode;


    public String getLoanAmount() {
        return loanAmount;
    }

    public String getCollectionCode() {
        return collectionCode;
    }

    public void setCollectionCode(String collectionCode) {
        this.collectionCode = collectionCode;
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

    public String getInstallmentsNo() {
        return installmentsNo;
    }

    public void setInstallmentsNo(String installmentsNo) {
        this.installmentsNo = installmentsNo;
    }
}


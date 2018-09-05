package com.dev.lishabora.Models;

public class LoanModel {
    private String loanAmount;
    private String installmentAmount;
    private String installmentsNo;

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

    public String getInstallmentsNo() {
        return installmentsNo;
    }

    public void setInstallmentsNo(String installmentsNo) {
        this.installmentsNo = installmentsNo;
    }
}


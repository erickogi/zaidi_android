package com.dev.lishabora.Models.Trader;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.dev.lishabora.Utils.DateTimeUtils;

@Entity(tableName = "farmerBalance")

public class FarmerBalance {
    @PrimaryKey(autoGenerate = true)
    private int id;


    private String farmerCode;

    private String balanceToPay;

    private String balanceOwed;


    private String lastUpdated;

    public FarmerBalance() {
    }

    public FarmerBalance(String farmerCode, String balanceToPay, String balanceOwed, String lastUpdated) {

        this.farmerCode = farmerCode;
        this.balanceToPay = balanceToPay;
        this.balanceOwed = balanceOwed;
        this.lastUpdated = lastUpdated;
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

    public String getBalanceToPay() {
        if (balanceToPay != null) {
            return balanceToPay;
        } else {
            return "0";
        }
    }

    public void setBalanceToPay(String balanceToPay) {
        this.balanceToPay = balanceToPay;
    }

    public String getBalanceOwed() {
        if (balanceOwed != null) {
            return balanceOwed;
        } else {
            return "0";
        }
    }

    public void setBalanceOwed(String balanceOwed) {
        this.balanceOwed = balanceOwed;
    }

    public String getLastUpdated() {
        if (lastUpdated != null) {
            return lastUpdated;
        } else {
            return DateTimeUtils.Companion.getToday();
        }
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}

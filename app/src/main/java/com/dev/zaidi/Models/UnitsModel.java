package com.dev.zaidi.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "units",indices = {@Index(value = {"code"}, unique = true)})

public class UnitsModel implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String code;
    private String unit;
    private String unitprice;
    private String unitcapacity;
    private String transactiontime;
    private String transactedby;
    private String status;

    @Ignore
    private String traderCode;

    public String getTraderCode() {
        return traderCode;
    }

    public void setTraderCode(String traderCode) {
        this.traderCode = traderCode;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnitprice() {
        if (unitprice != null && !unitprice.equals("")) {
            return unitprice;
        } else {
            return "0.0";
        }
    }

    public void setUnitprice(String unitprice) {
        this.unitprice = unitprice;
    }

    public String getUnitcapacity() {
        if (unitcapacity != null) {
            return unitcapacity;
        } else {
            return "0";
        }
    }

    public void setUnitcapacity(String unitcapacity) {
        this.unitcapacity = unitcapacity;
    }

    public String getTransactiontime() {
        return transactiontime;
    }

    public void setTransactiontime(String transactiontime) {
        this.transactiontime = transactiontime;
    }

    public String getTransactedby() {
        return transactedby;
    }

    public void setTransactedby(String transactedby) {
        this.transactedby = transactedby;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

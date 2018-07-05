package com.dev.lishaboramobile.Trader.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
@Entity(tableName = "routes",indices = {@Index(value = {"code"}, unique = true)})

public class RoutesModel {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String code;
    private String route;
    private String transactiontime;
    private String transactedby;
    private String synctime;
    private String transactioncode;
    private String status;

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
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

    public String getSynctime() {
        return synctime;
    }

    public void setSynctime(String synctime) {
        this.synctime = synctime;
    }

    public String getTransactioncode() {
        return transactioncode;
    }

    public void setTransactioncode(String transactioncode) {
        this.transactioncode = transactioncode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

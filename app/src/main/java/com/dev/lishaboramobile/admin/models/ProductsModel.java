package com.dev.lishaboramobile.admin.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "products", indices = {@Index(value = {"names", "id"}, unique = true)})

public class ProductsModel implements Serializable {
    @PrimaryKey(autoGenerate = true)

    private int id;
    private int code;
    private String names;
    private String costprice;
    private String sellingprice;
    private String transactiontime;
    private String transactedby;
    private String subscribed;
    private int status;
    private boolean isSelected = false;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getCostprice() {
        return costprice;
    }

    public void setCostprice(String costprice) {
        this.costprice = costprice;
    }

    public String getSellingprice() {
        return sellingprice;
    }

    public void setSellingprice(String sellingprice) {
        this.sellingprice = sellingprice;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(String subscribed) {
        this.subscribed = subscribed;
    }
}

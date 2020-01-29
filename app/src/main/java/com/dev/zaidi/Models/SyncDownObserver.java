package com.dev.zaidi.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "sycnDownObserver", indices = {@Index(value = {"id"}, unique = true)})
public class SyncDownObserver implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String transactiontime;
    private int status;
    private String response;

    public SyncDownObserver(int id, String transactiontime, int status, String response) {
        this.id = id;
        this.transactiontime = transactiontime;
        this.status = status;
        this.response = response;
    }

    public SyncDownObserver() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTransactiontime() {
        return transactiontime;
    }

    public void setTransactiontime(String transactiontime) {
        this.transactiontime = transactiontime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}

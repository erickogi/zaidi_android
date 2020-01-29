package com.dev.zaidi.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "notifications", indices = {@Index(value = {"code"}, unique = true)})
public class Notifications implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String date;
    private String title;
    private String message;
    private String code;
    private int viewd;
    private String payoutCode;
    private int type;
    private int actionId;

    public Notifications(int id, String date, String title, String message, String code, int viewd, int type, int actionId, String payoutCode) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.message = message;
        this.code = code;
        this.viewd = viewd;
        this.type = type;
        this.actionId = actionId;
        this.payoutCode = payoutCode;
    }

    public String getPayoutCode() {
        return payoutCode;
    }

    public void setPayoutCode(String payoutCode) {
        this.payoutCode = payoutCode;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getActionId() {
        return actionId;
    }

    public void setActionId(int actionId) {
        this.actionId = actionId;
    }

    public int getViewd() {
        return viewd;
    }

    public void setViewd(int viewd) {
        this.viewd = viewd;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

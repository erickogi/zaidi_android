package com.dev.lishaboramobile.Farmer.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "farmers",indices = {@Index(value = {"compositecode"}, unique = true)})

public class FamerModel implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String route;

    private String compositecode;

    private String code;
    private String entitycode;
    private String entity;
    private String names;
    private String mobile;
    private String apikey;
    private String firebasetoken;
    private String cyclecode;
    private String cyclename;
    private String routecode;
    private String routename;
    private String unitcode;
    private String unitname;
    private String unitcapacity;
    private String unitprice;
    private String totalmilkcollection;
    private String totalorders;
    private String totalloans;
    private String totalbalance;
    private String transactedby;
    private String transactiontime;
    private String synctime;
    private String transactioncode;
    private int deleted;
    private int archived;
    private String status;
    private int dummy;


    @Ignore
    private String resultDescription;
    @Ignore
    private int responseCode;

    @Ignore
    public String getResultDescription() {
        return resultDescription;
    }

    @Ignore
    public void setResultDescription(String resultDescription) {
        this.resultDescription = resultDescription;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getApikey() {
        return apikey;
    }

    public String getTotalbalance() {
        return totalbalance;
    }

    public void setTotalbalance(String totalbalance) {
        this.totalbalance = totalbalance;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getFirebasetoken() {
        return firebasetoken;
    }

    public void setFirebasetoken(String firebasetoken) {
        this.firebasetoken = firebasetoken;
    }

    public String getCyclename() {
        return cyclename;
    }

    public void setCyclename(String cyclename) {
        this.cyclename = cyclename;
    }

    public String getRoutecode() {
        return routecode;
    }

    public void setRoutecode(String routecode) {
        this.routecode = routecode;
    }

    public String getRoutename() {
        return routename;
    }

    public void setRoutename(String routename) {
        this.routename = routename;
    }

    public String getUnitname() {
        return unitname;
    }

    public void setUnitname(String unitname) {
        this.unitname = unitname;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public int getArchived() {
        return archived;
    }

    public void setArchived(int archived) {
        this.archived = archived;
    }

    public int getDummy() {
        return dummy;
    }

    public void setDummy(int dummy) {
        this.dummy = dummy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompositecode() {
        return compositecode;
    }

    public void setCompositecode(String compositecode) {
        this.compositecode = compositecode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEntitycode() {
        return entitycode;
    }

    public void setEntitycode(String entitycode) {
        this.entitycode = entitycode;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCyclecode() {
        return cyclecode;
    }

    public void setCyclecode(String cyclecode) {
        this.cyclecode = cyclecode;
    }

    public String getUnitcode() {
        return unitcode;
    }

    public void setUnitcode(String unitcode) {
        this.unitcode = unitcode;
    }

    public String getUnitcapacity() {
        return unitcapacity;
    }

    public void setUnitcapacity(String unitcapacity) {
        this.unitcapacity = unitcapacity;
    }

    public String getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(String unitprice) {
        this.unitprice = unitprice;
    }

    public String getTotalmilkcollection() {
        return totalmilkcollection;
    }

    public void setTotalmilkcollection(String totalmilkcollection) {
        this.totalmilkcollection = totalmilkcollection;
    }

    public String getTotalorders() {
        return totalorders;
    }

    public void setTotalorders(String totalorders) {
        this.totalorders = totalorders;
    }

    public String getTotalloans() {
        return totalloans;
    }

    public void setTotalloans(String totalloans) {
        this.totalloans = totalloans;
    }

    public String getTransactedby() {
        return transactedby;
    }

    public void setTransactedby(String transactedby) {
        this.transactedby = transactedby;
    }


    public String getTransactiontime() {
        return transactiontime;
    }

    public void setTransactiontime(String transactiontime) {
        this.transactiontime = transactiontime;
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

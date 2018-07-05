package com.dev.lishaboramobile.Trader.Models;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity(tableName = "traders",indices = {@Index(value = {"code"}, unique = true)})

public class TraderModel {
    @PrimaryKey(autoGenerate = true)
    private   int id ;

    private   String code ;
    private   String entity ;
    private   String entitycode ;
    private String transactioncode;
    @NonNull
    private   String names ;
    private   String mobile ;
    private   String password ;
    @NonNull
    private   String apikey ;
    private   String firebasetoken ;
    @NonNull
    private   String status ;
    private   String transactiontime ;
    private   String synctime ;
    @NonNull
    private   String transactedby ;

    private   boolean isarchived ;
    private   boolean isdeleted ;
    private   boolean issynced ;
    private   boolean isdummy ;


    private   int archived ;
    private   int deleted ;
    private   int synced ;
    private   int dummy ;


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

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getEntitycode() {
        return entitycode;
    }

    public void setEntitycode(String entitycode) {
        this.entitycode = entitycode;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getApikey() {
        return apikey;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getTransactedby() {
        return transactedby;
    }

    public void setTransactedby(String transactedby) {
        this.transactedby = transactedby;
    }

    public boolean isIsarchived() {
        return isarchived;
    }

    public void setIsarchived(boolean isarchived) {
        this.isarchived = isarchived;
    }

    public boolean isIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(boolean isdeleted) {
        this.isdeleted = isdeleted;
    }

    public boolean isIssynced() {
        return issynced;
    }

    public void setIssynced(boolean issynced) {
        this.issynced = issynced;
    }

    public boolean isIsdummy() {
        return isdummy;
    }

    public void setIsdummy(boolean isdummy) {
        this.isdummy = isdummy;
    }

    public int getArchived() {
        return archived;
    }

    public void setArchived(int archived) {
        this.archived = archived;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public int getSynced() {
        return synced;
    }

    public void setSynced(int synced) {
        this.synced = synced;
    }

    public int getDummy() {
        return dummy;
    }

    public void setDummy(int dummy) {
        this.dummy = dummy;
    }

    public String getTransactioncode() {
        return transactioncode;
    }

    public void setTransactioncode(String transactioncode) {
        this.transactioncode = transactioncode;
    }
}

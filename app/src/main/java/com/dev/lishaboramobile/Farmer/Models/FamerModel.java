package com.dev.lishaboramobile.Farmer.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.dev.lishaboramobile.Global.Utils.DateTimeUtils;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

@Entity(tableName = "farmers", indices = {@Index(value = {"compositecode", "mobile", "apikey"}, unique = true)})

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
    private String cycleStartDay;
    private String cycleStartEndDay;
    private String cycleStartDate;
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


    public static Comparator<FamerModel> farmerNameComparator = (s1, s2) -> {
        String FamerModelName1 = s1.getNames().toUpperCase();
        String FamerModelName2 = s2.getNames().toUpperCase();

        //ascending order
        return FamerModelName1.compareTo(FamerModelName2);

        //descending order
        //return FamerModelName2.compareTo(FamerModelName1);
    };
    public static Comparator<FamerModel> farmerDateComparator = (s1, s2) -> {
        String FamerModelStringDate1 = s1.getTransactiontime();
        String FamerModelStringDate2 = s2.getTransactiontime();
        Date FamerModelDate1 = DateTimeUtils.Companion.conver2Date(FamerModelStringDate1, DateTimeUtils.Companion.getFormat());
        Date FamerModelDate2 = DateTimeUtils.Companion.conver2Date(FamerModelStringDate2, DateTimeUtils.Companion.getFormat());

        //ascending order
        if (FamerModelDate1 != null) {
            return FamerModelDate1.compareTo(FamerModelDate2);
        } else return 0;

        //descending order
        //return FamerModelName2.compareTo(FamerModelName1);
    };
    /*Comparator for sorting the list by position no*/
    public static Comparator<FamerModel> farmerPosComparator = (s1, s2) -> {

        int position1 = s1.getPosition();
        int position2 = s2.getPosition();

        /*For ascending order*/
        return position1 - position2;

        /*For descending order*/
        //rollno2-rollno1;
    };




    private boolean isSynched = false;


    @Ignore
    private String resultDescription;
    @Ignore
    private int responseCode;
    private int syncstatus;
    private int position;
    private String loanbalance, milkbalance, orderbalance;
    @Ignore
    private boolean show;

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public int getSyncstatus() {
        return syncstatus;
    }

    public void setSyncstatus(int syncstatus) {
        this.syncstatus = syncstatus;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getLoanbalance() {
        return loanbalance;
    }

    public void setLoanbalance(String loanbalance) {
        this.loanbalance = loanbalance;
    }

    public String getMilkbalance() {
        return milkbalance;
    }

    public boolean isSynched() {
        return isSynched;
    }

    public void setSynched(boolean synched) {
        isSynched = synched;
    }

    public String getCycleStartDay() {
        return cycleStartDay;
    }

    public void setCycleStartDay(String cycleStartDay) {
        this.cycleStartDay = cycleStartDay;
    }

    public String getCycleStartEndDay() {
        return cycleStartEndDay;
    }

    public void setCycleStartEndDay(String cycleStartEndDay) {
        this.cycleStartEndDay = cycleStartEndDay;
    }

    public String getCycleStartDate() {
        return cycleStartDate;
    }

    public void setCycleStartDate(String cycleStartDate) {
        this.cycleStartDate = cycleStartDate;
    }

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

    public void setMilkbalance(String milkbalance) {
        this.milkbalance = milkbalance;
    }

    public String getOrderbalance() {
        return orderbalance;
    }

    public void setOrderbalance(String orderbalance) {
        this.orderbalance = orderbalance;
    }

    @Override
    public String toString() {
        return "[ rollno=" + id + ", name=" + names + ", age=" + code + "]";
    }
}

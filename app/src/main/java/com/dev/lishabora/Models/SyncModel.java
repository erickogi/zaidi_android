package com.dev.lishabora.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.dev.lishabora.Utils.DateTimeUtils;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Entity(tableName = "sync", indices = {@Index(value = {"id"}, unique = true)})

public class SyncModel {
    @PrimaryKey(autoGenerate = true)
    private int id;


    private int actionType;
    private String actionTypeName;
    private int entityType;
    private String entityTypeName;

    @Ignore
    private Object objectData;


    private String object;

    private int dataType;

    @Ignore
    private List<Object> objectsData;


    private String objects;



    private String timeStamp;
    private String syncTime;

    private String syncStatus;

    @Ignore
    private String traderCode;

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public List<Object> getObjectsData() {
        return objectsData;
    }

    public void setObjectsData(List<Object> objectsData) {
        this.objectsData = objectsData;
    }

    public String getObjects() {
        return objects;
    }

    public void setObjects(String objects) {
        this.objects = objects;
    }

    public String getTraderCode() {
        return traderCode;
    }

    public void setTraderCode(String traderCode) {
        this.traderCode = traderCode;
    }


    public SyncModel() {
    }

    public SyncModel(int id, int actionType, int entityType, Object objectData, String object, String timeStamp, String syncTime, String syncStatus) {
        this.id = id;
        this.actionType = actionType;
        this.entityType = entityType;
        this.objectData = objectData;
        this.object = object;
        this.timeStamp = timeStamp;
        this.syncTime = syncTime;
        this.syncStatus = syncStatus;
    }

    public static Comparator<SyncModel> farmerDateComparatorDesc = (s1, s2) -> {
        String FamerModelStringDate1 = s1.getTimeStamp();
        String FamerModelStringDate2 = s2.getTimeStamp();
        Date FamerModelDate1 = DateTimeUtils.Companion.conver2Date(FamerModelStringDate1, DateTimeUtils.Companion.getFormat());
        Date FamerModelDate2 = DateTimeUtils.Companion.conver2Date(FamerModelStringDate2, DateTimeUtils.Companion.getFormat());


        if (FamerModelDate1 != null) {
            return FamerModelDate2.compareTo(FamerModelDate1);
        } else {
            return 0;
        }
    };

    public String getActionTypeName() {
        return actionTypeName;
    }

    public void setActionTypeName(String actionTypeName) {
        this.actionTypeName = actionTypeName;
    }

    public String getEntityTypeName() {
        return entityTypeName;
    }

    public void setEntityTypeName(String entityTypeName) {
        this.entityTypeName = entityTypeName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }

    public Object getObjectData() {
        return objectData;
    }

    public void setObjectData(Object objectData) {
        this.objectData = objectData;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;

    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSyncTime() {
        return syncTime;
    }

    public void setSyncTime(String syncTime) {
        this.syncTime = syncTime;
    }

    public String getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }
}

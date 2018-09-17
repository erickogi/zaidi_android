package com.dev.lishabora.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

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
    private String timeStamp;
    private String syncTime;
    private int syncStatus;

    public SyncModel() {
    }

    public SyncModel(int id, int actionType, int entityType, Object objectData, String object, String timeStamp, String syncTime, int syncStatus) {
        this.id = id;
        this.actionType = actionType;
        this.entityType = entityType;
        this.objectData = objectData;
        this.object = object;
        this.timeStamp = timeStamp;
        this.syncTime = syncTime;
        this.syncStatus = syncStatus;
    }

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

    public int getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(int syncStatus) {
        this.syncStatus = syncStatus;
    }
}

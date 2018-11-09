package com.dev.lishabora.Models;

import com.dev.lishabora.Utils.SystemInfo;

import java.util.List;

public class SyncHolderModel {
    private String entityCode;
    private int entityType;
    private int syncType;
    private String time;
    private List<SyncModel> syncModels;
    private SystemInfo systemInfo;

    public SystemInfo getSystemInfo() {
        return systemInfo;
    }

    public void setSystemInfo(SystemInfo systemInfo) {
        this.systemInfo = systemInfo;
    }

    public int getSyncType() {
        return syncType;
    }

    public void setSyncType(int syncType) {
        this.syncType = syncType;
    }

    public String getEntityCode() {
        return entityCode;
    }

    public void setEntityCode(String entityCode) {
        this.entityCode = entityCode;
    }

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<SyncModel> getSyncModels() {
        return syncModels;
    }

    public void setSyncModels(List<SyncModel> syncModels) {
        this.syncModels = syncModels;
    }
}

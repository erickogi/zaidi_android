package com.dev.lishabora.Models;

import java.util.List;

public class SyncHolderModel {
    private String entityCode;
    private int entityType;
    private String time;
    private List<SyncModel> syncModels;


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

package com.dev.lishaboramobile.Admin.Models;

import java.util.List;

public class ChartModel {
    int id;
    private String title;
    private String description;
    private List<LVModel> lvModels;
    private String total;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<LVModel> getLvModels() {
        return lvModels;
    }

    public void setLvModels(List<LVModel> lvModels) {
        this.lvModels = lvModels;
    }
}

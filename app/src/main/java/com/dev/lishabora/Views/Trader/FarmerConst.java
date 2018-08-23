package com.dev.lishabora.Views.Trader;

import com.dev.lishabora.Models.FamerModel;

import java.util.LinkedList;

public class FarmerConst {
    private static FamerModel famerModel;
    private static int createFarmerIntentType = 0;

    public static final int ACTIVE = 0, ARCHIVED = 1, DELETED = 2, DUMMY = 3, ALL = 4;
    private static LinkedList<FamerModel> famerModels;
    private static LinkedList<FamerModel> filteredFamerModels;
    private static LinkedList<FamerModel> sortedFamerModels;
    private static LinkedList<FamerModel> searchFamerModels;

    public static void setListToShow(LinkedList<FamerModel> famerModels) {
        if (getSortedFamerModels() != null) {
            setSortedFamerModels(famerModels);
        }
    }

    public static LinkedList<FamerModel> getSearchFamerModels() {
        return searchFamerModels;
    }

    public static void setSearchFamerModels(LinkedList<FamerModel> searchFamerModels) {
        FarmerConst.searchFamerModels = searchFamerModels;
    }

    public static FamerModel getFamerModel() {
        return famerModel;
    }

    public static void setFamerModel(FamerModel famerModel) {
        FarmerConst.famerModel = famerModel;
    }

    public static int getCreateFarmerIntentType() {
        return createFarmerIntentType;
    }

    public static void setCreateFarmerIntentType(int createFarmerIntentType) {
        FarmerConst.createFarmerIntentType = createFarmerIntentType;
    }

    public static LinkedList<FamerModel> getFamerModels() {
        return famerModels;
    }

    public static void setFamerModels(LinkedList<FamerModel> famerModels) {
        FarmerConst.famerModels = famerModels;
    }

    public static LinkedList<FamerModel> getFilteredFamerModels() {
        return filteredFamerModels;
    }

    public static void setFilteredFamerModels(LinkedList<FamerModel> filteredFamerModels) {
        FarmerConst.filteredFamerModels = filteredFamerModels;
    }

    public static LinkedList<FamerModel> getSortedFamerModels() {
        return sortedFamerModels;
    }

    public static void setSortedFamerModels(LinkedList<FamerModel> sortedFamerModels) {
        FarmerConst.sortedFamerModels = sortedFamerModels;
    }
}

package com.dev.lishabora.Views.Trader;

import com.dev.lishabora.Models.FamerModel;

import java.util.List;

public class FarmerConst {
    private static FamerModel famerModel;
    private static int createFarmerIntentType = 0;

    public static final int ACTIVE = 0, ARCHIVED = 1, DELETED = 2, DUMMY = 3, ALL = 4;
    private static List<FamerModel> famerModels;
    private static List<FamerModel> filteredFamerModels;
    private static List<FamerModel> sortedFamerModels;
    private static List<FamerModel> searchFamerModels;

    public static void setListToShow(List<FamerModel> famerModels) {
        if (getSortedFamerModels() != null) {
            setSortedFamerModels(famerModels);
        }
    }

    public static List<FamerModel> getSearchFamerModels() {
        return searchFamerModels;
    }

    public static void setSearchFamerModels(List<FamerModel> searchFamerModels) {
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

    public static List<FamerModel> getFamerModels() {
        return famerModels;
    }

    public static void setFamerModels(List<FamerModel> famerModels) {
        FarmerConst.famerModels = famerModels;
    }

    public static List<FamerModel> getFilteredFamerModels() {
        return filteredFamerModels;
    }

    public static void setFilteredFamerModels(List<FamerModel> filteredFamerModels) {
        FarmerConst.filteredFamerModels = filteredFamerModels;
    }

    public static List<FamerModel> getSortedFamerModels() {
        return sortedFamerModels;
    }

    public static void setSortedFamerModels(List<FamerModel> sortedFamerModels) {
        FarmerConst.sortedFamerModels = sortedFamerModels;
    }
}

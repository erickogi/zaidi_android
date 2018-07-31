package com.dev.lishaboramobile.Views.Farmer;

import com.dev.lishaboramobile.Farmer.Models.FamerModel;

public class FarmerConst {
    private static FamerModel famerModel;

    public static FamerModel getFamerModel() {
        return famerModel;
    }

    public static void setFamerModel(FamerModel famerModel) {
        FarmerConst.famerModel = famerModel;
    }
}

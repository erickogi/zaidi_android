package com.dev.lishabora.Models;

import android.arch.persistence.room.Embedded;

public class FarmerRouteBalance {

    @Embedded(prefix = "farmer")
    FamerModel famerModel;

    @Embedded(prefix = "route")
    RoutesModel routesModel;

    @Embedded(prefix = "balance")
    FarmerBalance farmerBalance;


    public FamerModel getFamerModel() {
        return famerModel;
    }

    public void setFamerModel(FamerModel famerModel) {
        this.famerModel = famerModel;
    }

    public RoutesModel getRoutesModel() {
        return routesModel;
    }

    public void setRoutesModel(RoutesModel routesModel) {
        this.routesModel = routesModel;
    }

    public FarmerBalance getFarmerBalance() {
        return farmerBalance;
    }

    public void setFarmerBalance(FarmerBalance farmerBalance) {
        this.farmerBalance = farmerBalance;
    }
}

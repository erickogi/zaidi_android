package com.dev.zaidi.Models.Trader

import com.dev.zaidi.Models.*
import com.dev.zaidi.Models.Collection
import java.io.Serializable

class Data : Serializable {


    var routeModels: List<RoutesModel>? = null
    var productModels: List<ProductsModel>? = null
    var farmerModels: List<FamerModel>? = null


    var orderModels: List<FarmerOrdersTable>? = null
    var loanModels: List<FarmerLoansTable>? = null

    var loanPaymentModels: List<LoanPayments>? = null
    var orderPaymentModels: List<OrderPayments>? = null

    var collectionModels: List<Collection>? = null
    var payoutModels: List<Payouts>? = null
    //  private List<Payouts> payoutModels;


    var cycleModels: List<Cycles>? = null
    var unitModels: List<UnitsModel>? = null
    var notificationModels: List<Notifications>? = null

    var balanceModels: List<FarmerBalance>? = null
    var traderModel: TraderModel? = null


    var resultCode: String? = null
    var resultDescription: String? = null

    fun getUnitsModels(): List<UnitsModel>? {
        return unitModels
    }

    fun setUnitsModels(unitsModels: List<UnitsModel>) {
        this.unitModels = unitsModels
    }
}

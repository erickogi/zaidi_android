package com.dev.lishabora.Utils

import com.dev.lishabora.Models.PayoutFarmersCollectionModel

interface ApproveFarmerPayCardListener {
    fun onApprove(farmerBalance: Double, model: PayoutFarmersCollectionModel, totalKshToPay: Double?, toLoanInstallmentPayment: Double?, toOrderInstallmentPayment: Double?)

    fun onApprovePayLoan(farmerBalance: Double, model: PayoutFarmersCollectionModel, totalKshToPay: Double?, toLoanInstallmentPayment: Double?)

    fun onApprovePayOrder(farmerBalance: Double, model: PayoutFarmersCollectionModel, totalKshToPay: Double?, toOrderInstallmentPayment: Double?)

    fun onApprove(farmerBalance: Double, model: PayoutFarmersCollectionModel, totalKshToPay: Double?)

    fun onApprove(farmerBalance: Double, model: PayoutFarmersCollectionModel)

    fun onApproveError(error: String)

    fun onApproveDismiss()

}

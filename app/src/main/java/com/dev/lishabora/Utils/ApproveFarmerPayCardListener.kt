package com.dev.lishabora.Utils

import com.dev.lishabora.Models.PayoutFarmersCollectionModel

interface ApproveFarmerPayCardListener {
    fun onApprove(model: PayoutFarmersCollectionModel, totalKshToPay: Double?, toLoanInstallmentPayment: Double?, toOrderInstallmentPayment: Double?)

    fun onApprovePayLoan(model: PayoutFarmersCollectionModel, totalKshToPay: Double?, toLoanInstallmentPayment: Double?)

    fun onApprovePayOrder(model: PayoutFarmersCollectionModel, totalKshToPay: Double?, toOrderInstallmentPayment: Double?)

    fun onApprove(model: PayoutFarmersCollectionModel, totalKshToPay: Double?)

    fun onApprove(model: PayoutFarmersCollectionModel)

    fun onApproveError(error: String)

    fun onApproveDismiss()
}

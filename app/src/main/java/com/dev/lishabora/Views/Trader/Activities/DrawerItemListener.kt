package com.dev.lishabora.Views.Trader.Activities

import com.dev.lishabora.Application

/**
 * Created by Eric on 12/13/2017.
 */

interface DrawerItemListener {
    fun logOutClicked()

    fun helpClicked()

    fun profileSettingsClicked()

    fun routesSettingsClicked()
    fun notificationsClicked()


    fun appSettingsClicked()

    fun homeClicked()

    fun analyticsReportsTransactionsClicked()

    fun payoutsClicked()
    fun productsClicked()
    fun syncWorksClicked()
    fun loansAndOrders()
    fun wrongTime()
    fun syncDue(hs: Application.hasSynced)


}

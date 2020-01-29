package com.dev.zaidi.UseCases

import com.dev.zaidi.Utils.DateTimeUtils
import com.dev.zaidi.ViewModels.Trader.PayoutsVewModel
import org.joda.time.Interval

class CanCollectBasedOnPayoutUseCase {
    companion object{
        fun canCollectBasedOnPayout(payoutsVewModel: PayoutsVewModel,cycleCode: String): Boolean {
            val p = payoutsVewModel.getLastPayout(cycleCode)
            if (p != null) {
                val endDate = p.endDate
                val startDate = p.startDate


                val interval = Interval(DateTimeUtils.conver2Date(startDate), DateTimeUtils.conver2Date(endDate))

                return if (interval.containsNow() || DateTimeUtils.conver2Date(endDate)?.let { DateTimeUtils.isTodayN(it) }!!) {
                    p.status != 1
                } else {
                    true
                }

            } else {
                return true
            }
        }
    }
}
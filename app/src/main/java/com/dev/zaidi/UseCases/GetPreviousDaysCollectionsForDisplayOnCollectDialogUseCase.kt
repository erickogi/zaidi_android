package com.dev.zaidi.UseCases

import com.dev.zaidi.Models.collectMod
import com.dev.zaidi.Utils.DateTimeUtils
import com.dev.zaidi.ViewModels.Trader.TraderViewModel

class GetPreviousDaysCollectionsForDisplayOnCollectDialogUseCase {

    companion object {
        fun execute(mViewModel: TraderViewModel, code: String,collectModListener: CollectModListener): collectMod {
            Thread().run {
                val collections = mViewModel.getCollectionsBetweenDatesOne(DateTimeUtils.getLongDate(DateTimeUtils.getDatePrevious(4)), DateTimeUtils.getLongDate(DateTimeUtils.getToday()), code)
                val mod = collectMod()
                if (collections != null) {
                    for (c in collections) {
                        if (c.dayDate.contains(DateTimeUtils.getDatePrevious(3))) {
                            if (c.milkCollectedAm != null && c.milkCollectedAm != "0.0" && c.milkCollectedAm != "0") {
                                mod.day1Ams = c.milkCollectedAm
                            }
                            if (c.milkCollectedPm != null && c.milkCollectedPm != "0.0" && c.milkCollectedPm != "0") {
                                mod.day1pms = c.milkCollectedPm

                            }
                        } else if (c.dayDate.contains(DateTimeUtils.getDatePrevious(2))) {
                            if (c.milkCollectedAm != null && c.milkCollectedAm != "0.0" && c.milkCollectedAm != "0") {
                                // day2am.setText(c.getMilkCollectedAm());
                                mod.day2Ams = c.milkCollectedAm

                            }
                            if (c.milkCollectedPm != null && c.milkCollectedPm != "0.0" && c.milkCollectedPm != "0") {
                                // day2pm.setText(c.getMilkCollectedPm());
                                mod.day2pms = c.milkCollectedPm

                            }
                        } else if (c.dayDate.contains(DateTimeUtils.getDatePrevious(1))) {
                            if (c.milkCollectedAm != null && c.milkCollectedAm != "0.0" && c.milkCollectedAm != "0") {
                                // day3am.setText(c.getMilkCollectedAm());
                                mod.day3Ams = c.milkCollectedAm


                            }
                            if (c.milkCollectedPm != null && c.milkCollectedPm != "0.0" && c.milkCollectedPm != "0") {
                                // day3pm.setText(c.getMilkCollectedPm());
                                mod.day3pms = c.milkCollectedPm


                            }
                        } else if (c.dayDate.contains(DateTimeUtils.getToday())) {
                            mod.todaysCollection = c
                            if (c.milkCollectedAm != null && c.milkCollectedAm != "0.0" && c.milkCollectedAm != "0") {

                                mod.day4Ams = c.milkCollectedAm

                            }
                            if (c.milkCollectedPm != null && c.milkCollectedPm != "0.0" && c.milkCollectedPm != "0") {
                                mod.day4pms = c.milkCollectedPm


                            }
                        }

                    }
                }

                collectModListener.onCollectionModelComputed(mod)
                return mod
            }
        }

         interface CollectModListener {
            fun onCollectionModelComputed(colMod: collectMod )

            fun error(error: String)
        }
    }


}
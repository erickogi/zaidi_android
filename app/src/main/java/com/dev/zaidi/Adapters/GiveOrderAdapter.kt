package com.dev.zaidi.Adapters

import android.content.Context
import android.os.Bundle
import android.support.annotation.IntRange
import android.support.v4.app.FragmentManager
import com.dev.zaidi.Views.Trader.Fragments.FragmentCompleteGiveOrder
import com.dev.zaidi.Views.Trader.Fragments.FragmentGiveOrder
import com.stepstone.stepper.Step
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter
import com.stepstone.stepper.viewmodel.StepViewModel

class GiveOrderAdapter(fm: FragmentManager, context: Context) : AbstractFragmentStepAdapter(fm, context) {

    override fun createStep(position: Int): Step? {
        when (position) {
            0 -> {
                val step1 = FragmentGiveOrder()
                val b1 = Bundle()
                b1.putInt(CURRENT_STEP_POSITION_KEY, position)

                step1.arguments = b1
                return step1
            }
            1 -> {
                val step2 = FragmentCompleteGiveOrder()
                val b2 = Bundle()
                b2.putInt(CURRENT_STEP_POSITION_KEY, position)

                step2.arguments = b2
                return step2
            }


        }
        return null
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getViewModel(@IntRange(from = 0) position: Int): StepViewModel {
        //Override this method to set Step title for the Tabs, not necessary for other stepper types
        when (position) {
            0 -> return StepViewModel.Builder(context)

                    .setTitle("Add Products") //can be a CharSequence instead
                    .create()
            1 -> return StepViewModel.Builder(context)
                    .setTitle("Set-Up payment") //can be a CharSequence instead
                    .create()


        }
        return StepViewModel.Builder(context).create()
    }

    companion object {
        private val CURRENT_STEP_POSITION_KEY = "messageResourceId"
    }


}

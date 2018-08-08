package com.dev.lishabora.Adapters

import android.content.Context
import android.os.Bundle
import android.support.annotation.IntRange
import android.support.v4.app.FragmentManager
import com.dev.lishabora.Views.Trader.Fragments.FragmentBasicDetails
import com.dev.lishabora.Views.Trader.Fragments.FragmentCycleDetails
import com.dev.lishabora.Views.Trader.Fragments.FragmentRoutesUnitDetails

import com.stepstone.stepper.Step
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter
import com.stepstone.stepper.viewmodel.StepViewModel

class FarmetRecruitAdapter(fm: FragmentManager, context: Context) : AbstractFragmentStepAdapter(fm, context) {

    override fun createStep(position: Int): Step? {
        when (position) {
            0 -> {
                val step1 = FragmentBasicDetails()
                val b1 = Bundle()
                b1.putInt(CURRENT_STEP_POSITION_KEY, position)

                step1.arguments = b1
                return step1
            }
            1 -> {
                val step2 = FragmentRoutesUnitDetails()
                val b2 = Bundle()
                b2.putInt(CURRENT_STEP_POSITION_KEY, position)

                step2.arguments = b2
                return step2
            }
            2 -> {
                val step3 = FragmentCycleDetails()
                val b3 = Bundle()
                b3.putInt(CURRENT_STEP_POSITION_KEY, position)


                return step3
            }

        }
        return null
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getViewModel(@IntRange(from = 0) position: Int): StepViewModel {
        //Override this method to set Step title for the Tabs, not necessary for other stepper types
        when (position) {
            0 -> return StepViewModel.Builder(context)

                    .setTitle("Basic Details ") //can be a CharSequence instead
                    .create()
            1 -> return StepViewModel.Builder(context)
                    .setTitle("Route & Units ") //can be a CharSequence instead
                    .create()
            2 -> return StepViewModel.Builder(context)
                    .setTitle("Cycle Info") //can be a CharSequence instead
                    .create()

        }
        return StepViewModel.Builder(context).create()
    }

    companion object {
        private val CURRENT_STEP_POSITION_KEY = "messageResourceId"
    }


}

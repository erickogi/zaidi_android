package com.dev.zaidi.Adapters

import android.content.Context
import android.os.Bundle
import android.support.annotation.IntRange
import android.support.v4.app.FragmentManager
import com.dev.zaidi.Views.Admin.Fragments.TraderBasicInfoFragment
import com.dev.zaidi.Views.Admin.Fragments.TraderCyclesInfoFragment
import com.dev.zaidi.Views.Admin.Fragments.TraderPasswordInfoFragment
import com.stepstone.stepper.Step
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter
import com.stepstone.stepper.viewmodel.StepViewModel

class RegisterTraderAdapter(fm: FragmentManager, context: Context) : AbstractFragmentStepAdapter(fm, context) {

    override fun createStep(position: Int): Step? {
        when (position) {
            0 -> {
                val step1 = TraderBasicInfoFragment()
                val b1 = Bundle()
                b1.putInt(CURRENT_STEP_POSITION_KEY, position)

                step1.arguments = b1
                return step1
            }
            1 -> {
                val step = TraderPasswordInfoFragment()
                val b = Bundle()
                b.putInt(CURRENT_STEP_POSITION_KEY, position)

                step.arguments = b
                return step
            }
            2 -> {
                val step2 = TraderCyclesInfoFragment()
                val b2 = Bundle()
                b2.putInt(CURRENT_STEP_POSITION_KEY, position)

                step2.arguments = b2
                return step2
            }
//            2 -> {
//                val step3 = TraderRoutesInfoFragment()
//                val b3 = Bundle()
//                b3.putInt(CURRENT_STEP_POSITION_KEY, position)
//
//
//                return step3
//            }
//            3 -> {
//                val step4 = TraderProductsnfoFragment()
//                val b4 = Bundle()
//                b4.putInt(CURRENT_STEP_POSITION_KEY, position)
//
//
//                return step4
//            }

        }
        return null
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getViewModel(@IntRange(from = 0) position: Int): StepViewModel {
        when (position) {
            0 -> return StepViewModel.Builder(context)
                    .setTitle("Basic Details ") //can be a CharSequence instead
                    .create()
            1 -> return StepViewModel.Builder(context)
                    .setTitle("Cycles") //can be a CharSequence instead
                    .create()
            2 -> return StepViewModel.Builder(context)
                    .setTitle("Products") //can be a CharSequence instead
                    .create()

        }
        return StepViewModel.Builder(context).create()
    }

    companion object {
        private val CURRENT_STEP_POSITION_KEY = "messageResourceId"
    }


}

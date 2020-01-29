package com.dev.zaidi.Utils

import android.app.Activity
import android.view.View
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import java.io.Serializable


class MaterialIntro {
    companion object {
        fun showIntro(context: Activity, infoText: String, target: View, usageId: String = infoText) {

//            try {
//                MaterialIntroView.Builder(context)
//                        .dismissOnTouch(true)
//                        .enableDotAnimation(true)
//                        .enableIcon(true)
//                        .setFocusGravity(FocusGravity.CENTER)
//                        .setFocusType(Focus.MINIMUM)
//                        .setDelayMillis(500)
//                        .enableFadeAnimation(true)
//                        .performClick(true)
//                        .setInfoText(infoText)
//                        .setTarget(target)
//                        .setUsageId(usageId)
//                        .show()
//                //  BadgeView(context).bindTarget(inbox).badgeText = "2"
//
//            } catch (xc: Exception) {
//                xc.printStackTrace()
//            }
        }

        fun showIntro(context: Activity, infoText: String, target: View) {
            var usageId = GeneralUtills(context).getRandon().toString()

//            try {
//                MaterialIntroView.Builder(context)
//                        .dismissOnTouch(true)
//                        .enableDotAnimation(true)
//                        .enableIcon(true)
//                        .setFocusGravity(FocusGravity.CENTER)
//                        .setFocusType(Focus.MINIMUM)
//                        .setDelayMillis(500)
//                        .enableFadeAnimation(true)
//                        .performClick(true)
//                        .setInfoText(infoText)
//                        .setTarget(target)
//                        .setUsageId(usageId)
//                        .show()
//                //  BadgeView(context).bindTarget(inbox).badgeText = "2"
//
//            } catch (xc: Exception) {
//                xc.printStackTrace()
//            }
        }

        fun createTargets(context: Activity, targets: List<Target>) {

            val listTargets: MutableList<TapTarget> = ArrayList()
            for (target in targets) {
                listTargets.add(TapTarget.forView(target.view, target.infoText).transparentTarget(true).cancelable(false).id(target.id))
            }

            showIntroSequence(context, listTargets)
        }


        fun showIntroSequence(context: Activity, targets: List<TapTarget>) {


            TapTargetSequence(context)
                    .targets(targets)
                    .listener(object : TapTargetSequence.Listener {
                        override fun onSequenceStep(lastTarget: TapTarget?, targetClicked: Boolean) {

                        }

                        // This listener will tell us when interesting(tm) events happen in regards
                        // to the sequence
                        override fun onSequenceFinish() {
                            // Yay
                        }

                        fun onSequenceStep(lastTarget: TapTarget) {
                            // Perfom action for the current target
                        }

                        override fun onSequenceCanceled(lastTarget: TapTarget) {
                            // Boo
                        }
                    }).start()
        }

    }

    class Target(var infoText: String, var view: View, var id: Int) : Serializable
}
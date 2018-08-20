package com.dev.lishabora.Views.Trader.Activities

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.widget.Toolbar
import com.dev.lishaboramobile.R

import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem

class DrawerClass {

    private fun getBitmap(activity: Activity, img: String): Bitmap {


        val thumnail = BitmapFactory.decodeResource(activity.resources, R.drawable.ic_person_black_24dp)

        try {

            return thumnail

        } catch (nm: Exception) {
            return thumnail
        }


    }

    companion object {

        fun getDrawer(email: String?, name: String?, activity: Activity, toolbar: Toolbar, itemListener: DrawerItemListener) {
            lateinit var result: Drawer
            val drawerEmptyItem = PrimaryDrawerItem().withIdentifier(0).withName("")
            drawerEmptyItem.withEnabled(false)


            val home = PrimaryDrawerItem().withIdentifier(1)
                    .withName("Home").withTextColorRes(R.color.white)
                    .withIcon(R.drawable.ic_home)


//            val profile = PrimaryDrawerItem().withIdentifier(2)
//                    .withName("Profile").withTextColorRes(R.color.white).withIcon(R.drawable.ic_account)
            val routes = PrimaryDrawerItem().withIdentifier(3)
                    .withName("Routes").withTextColorRes(R.color.white).withIcon(R.drawable.ic_route)

            val products = PrimaryDrawerItem().withIdentifier(20)
                    .withName("Products").withTextColorRes(R.color.white).withIcon(R.drawable.ic_add_white_24dp)


            val payout = PrimaryDrawerItem().withIdentifier(21)
                    .withName("Payouts").withTextColorRes(R.color.white).withIcon(R.drawable.ic_route)


            val notifications = PrimaryDrawerItem().withIdentifier(5)
                    .withName("Notifications").withTextColorRes(R.color.white)
                    .withIcon(R.drawable.ic_notifications_black_24dp).withBadge("3")


            val transactions = PrimaryDrawerItem().withIdentifier(6)
                    .withName("Reports & Transactions").withTextColorRes(R.color.white).withIcon(R.drawable.ic_timeline)


            val logout = SecondaryDrawerItem().withIdentifier(7).withIcon(R.drawable.ic_logout)
                    .withName("Log Out").withTextColorRes(R.color.white)
            //.withIcon(R.drawable.ic_exit_to_app_black_24dp);

            val settings = PrimaryDrawerItem().withIdentifier(8).withIcon(R.drawable.ic_settings)
                    .withName("Settings").withTextColorRes(R.color.white)

            val account = SecondaryDrawerItem().withIdentifier(9)
                    .withName("Profile").withTextColorRes(R.color.white)
                    .withIcon(R.drawable.ic_account_circle_black_24dp)

            val help = PrimaryDrawerItem().withIdentifier(10).withIcon(R.drawable.ic_help)
                    .withName("Help").withTextColorRes(R.color.white)


            val headerResult = AccountHeaderBuilder()
                    .withActivity(activity)
                    .withOnProfileClickDrawerCloseDelay(2)
                    .withTextColorRes(R.color.white)
                    .withSelectionListEnabledForSingleProfile(false)
                    .withDividerBelowHeader(true)

                    //  .withHeaderBackground(R.drawable.dvf)
                    .addProfiles(
                            ProfileDrawerItem().withName("0$name").withEmail(email)

                                    //.withSelectedTextColorRes(R.color.colorPrimaryDark)
                            // .withIcon(R.drawable.ic_launcher)


                    )
                    .withOnAccountHeaderListener { view, profile, currentProfile -> false }
                    .build()


            result = DrawerBuilder()

                    .withAccountHeader(headerResult)
                    .withActivity(activity)
                    //.withFooter(R.layout.footer)

                    //.withFooter(R.layout.footer)

                    //.withGenerateMiniDrawer(true)
                    .withFooterDivider(false)

                    .withSelectedItem(1)

                    .withToolbar(toolbar)
                    .withSliderBackgroundColorRes(R.color.colorPrimary)

                    // .withSliderBackgroundDrawableRes(R.drawable.headermain)

                    //.withAccountHeader(headerResult)
                    .withActionBarDrawerToggle(true)
                    .withActionBarDrawerToggleAnimated(true)
                    .withCloseOnClick(true)
                    //.withSelectedItem(-1)
                    .addDrawerItems(
                            home,
                            routes, products, payout, notifications, transactions,
                            DividerDrawerItem(),
                            account,
                            // settings,


                            // invalidate,
                            DividerDrawerItem(),
                            //logout,
                            //share
                            help,
                            logout
                            // about


                    )
                    .withFooterClickable(true)
                    //.withStickyFooter(R.layout.footer)


                    .withOnDrawerItemClickListener { view, position, drawerItem ->

                        when (drawerItem.identifier.toInt()) {
                            1 -> {
                                itemListener.homeClicked()
                                result.closeDrawer()
                            }

                            2 -> {

                            }

                            3 -> {
                                itemListener.routesSettingsClicked()
                                result.closeDrawer()
                            }
                            4 -> {
                                itemListener.notificationsClicked()
                                result.closeDrawer()
                            }
                            5 -> {
                                itemListener.notificationsClicked()
                                result.closeDrawer()
                            }
                            6 -> {
                                itemListener.profileSettingsClicked()
                                result.closeDrawer()
                            }

                            7 -> {
                                itemListener.logOutClicked()
                                result.closeDrawer()
                            }

                            10 -> {
                                itemListener.helpClicked()
                                result.closeDrawer()
                            }


                            20 -> {
                                itemListener.productsClicked()
                                result.closeDrawer()
                            }
                            21 -> {
                                itemListener.payoutsClicked()
                                result.closeDrawer()
                            }

                        }
                        true
                    }

                    .build()
        }
    }
}
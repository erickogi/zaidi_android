package com.dev.lishaboramobile.Admin.Utills;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.Toolbar;

import com.dev.lishaboramobile.Admin.Models.AdminModel;
import com.dev.lishaboramobile.R;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;

/**
 * Created by Eric on 12/13/2017.
 */

public class AdminDrawer {
    static Drawer result;
    static AccountHeader headerResult;
    private Activity activity;

    private static Bitmap getBitmap(Activity activity, String img) {


        Bitmap thumnail = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_launcher_background);


        return  thumnail;


    }

    public static void getDrawer(final Activity activity, Toolbar toolbar, int id, AdminModel userModel, String log, DrawerItemListener itemListener) {
        //if you want to update the items at a later time it is recommended to keep it in a variable



        PrimaryDrawerItem drawerEmptyItem = new PrimaryDrawerItem().withIdentifier(0).withName("");
        drawerEmptyItem.withEnabled(false);


        PrimaryDrawerItem home = new PrimaryDrawerItem().withIdentifier(1)
                .withName("Home").withTextColorRes(R.color.white)
                .withIcon(R.drawable.ic_home_black_24dp);


        PrimaryDrawerItem account = new PrimaryDrawerItem().withIdentifier(2)
                .withName("Profile").withTextColorRes(R.color.white).withIcon(R.drawable.ic_account_circle_black_24dp);



        PrimaryDrawerItem messages = new PrimaryDrawerItem().withIdentifier(3)
                .withName("Messages").withTextColorRes(R.color.white)//.withIcon(R.drawable.common_google_signin_btn_icon_dark_normal)
                ;
//        PrimaryDrawerItem favorites = new PrimaryDrawerItem().withIdentifier(4)
//                .withName("Favorites").withIcon(R.drawable.ic_favorite_black_24dp)
//                .withBadge("4");


        PrimaryDrawerItem invalidate = new PrimaryDrawerItem().withIdentifier(5)
                .withName("Clear Data").withTextColorRes(R.color.drawertext);//.withIcon(R.drawable.ic_attach_money_black_24dp);


        PrimaryDrawerItem settings = new PrimaryDrawerItem().withIdentifier(6)
                .withIcon(R.drawable.ic_settings_black_24dp)
                .withName("Settings").withTextColorRes(R.color.drawertext);

        ExpandableDrawerItem records = new ExpandableDrawerItem().withIdentifier(88)
                .withArrowColor(activity.getResources().getColor(R.color.black))
                .withName("Pending")
                .withTextColorRes(R.color.drawertext)
                .withSelectable(false)
                .withSubItems(
                        new SecondaryDrawerItem()
                                .withName("Pending")
                                .withTextColorRes(R.color.drawertext)
                                .withIdentifier(801),

                        new SecondaryDrawerItem()
                                .withName("All")
                                .withTextColorRes(R.color.drawertext)
                                .withIdentifier(802)
                );

//        PrimaryDrawerItem upcoming = new PrimaryDrawerItem().withIdentifier(10)
//                .withIcon(R.drawable.ic_move_to_inbox_black_24dp)
//                .withName("Upcoming Lessons")
//                // .withBadge("4");
//                ;

        SecondaryDrawerItem logout = new SecondaryDrawerItem().withIdentifier(7)
                .withName(log).withTextColorRes(R.color.white).withIcon(R.drawable.ic_exit_to_app_black_24dp);

        PrimaryDrawerItem stories = new PrimaryDrawerItem().withIdentifier(11)
                .withName("Photo Stories").withTextColorRes(R.color.drawertext).withIcon(R.drawable.ic_photo_library_black_24dp);

        PrimaryDrawerItem help = new PrimaryDrawerItem().withIdentifier(8)
                .withName("Help").withTextColorRes(R.color.drawertext);
        SecondaryDrawerItem share = new SecondaryDrawerItem().withIdentifier(9)
                .withName("Share").withTextColorRes(R.color.white).withIcon(R.drawable.ic_share_black_24dp);

        SecondaryDrawerItem dev = new SecondaryDrawerItem().withIdentifier(12)
                .withName("Developer").withTextColorRes(R.color.white).withIcon(R.drawable.ic_developer_mode_black_24dp);


//

        headerResult = new AccountHeaderBuilder()
                .withActivity(activity)
                .withOnProfileClickDrawerCloseDelay(2)
                .withTextColorRes(R.color.white)
                .withSelectionListEnabledForSingleProfile(false)
                .withDividerBelowHeader(true)
                // .withHeaderBackground(R.drawable.zuri)
                .addProfiles(
                        new ProfileDrawerItem().withName(userModel.getNames())

                                .withEmail(userModel.getEmail())

                                .withSelectedTextColorRes(R.color.white)
                                .withIcon(R.drawable.ic_launcher)


                )
                .withOnAccountHeaderListener((view, profile, currentProfile) -> false)
                .build();


        result = new DrawerBuilder()

                .withActivity(activity)
                .withSliderBackgroundColorRes(R.color.colorPrimary)
                .withFooterDivider(false)


                .withToolbar(toolbar)

                .withAccountHeader(headerResult)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .withCloseOnClick(true)
                .withSelectedItem(-1)
                .addDrawerItems(
                        // favorites,
                        //drawerEmptyItem,
                        home,// payments, //new DividerDrawerItem(),
                        //messages,
                        // stories,
                        //new DividerDrawerItem(),
                        account,

                        //settings,
                        // invalidate,
                        new DividerDrawerItem(),
                        logout,
                        share
                        //, dev
                        // help
                        // about


                )
                .withFooterClickable(true)
                //.withStickyFooter(R.layout.footer)


                .withOnDrawerItemClickListener((view, position, drawerItem) -> {

                            switch ((int) drawerItem.getIdentifier()) {
                                case 1:
                                    itemListener.homeClicked();
                                    result.closeDrawer();
                                    break;

                                case 2:
                                    itemListener.profileClicked();
                                    result.closeDrawer();
                                    break;

                                case 7:
                                    itemListener.logOutClicked();
                                    result.closeDrawer();
                                    break;
                                case 8:
                                    itemListener.helpClicked();
                                    result.closeDrawer();
                                    break;
                                case 9:
                                    itemListener.shareClicked();
                                    result.closeDrawer();
                                    break;
                                case 11:
                                    itemListener.storiesClicked();
                                    result.closeDrawer();
                                    break;
                                case 12:
                                    itemListener.storiesClicked();
                                    result.closeDrawer();
                                    break;





                            }
                            return true;
                        }
                )

                .build();
    }

    public static void update(String login) {
        try {
            result.updateName(7, new StringHolder(login));
        } catch (Exception nm) {
            nm.printStackTrace();
        }

        //headerResult.updateProfileByIdentifier();
        //result.u(7,new StringHolder(login));
    }
}

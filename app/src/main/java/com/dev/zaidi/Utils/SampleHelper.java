package com.dev.zaidi.Utils;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;

import com.dev.zaidi.R;
import com.vansuita.materialabout.builder.AboutBuilder;
import com.vansuita.materialabout.views.AboutView;


public class SampleHelper implements View.OnClickListener {

    private Activity activity;
    private int theme = R.style.AppTheme;

    private SampleHelper(Activity activity) {
        this.activity = activity;
    }

    public static SampleHelper with(Activity activity) {
        return new SampleHelper(activity);
    }

    public SampleHelper init() {
        activity.setTheme(theme);

        activity.findViewById(R.id.dark).setOnClickListener(this);
        activity.findViewById(R.id.light).setOnClickListener(this);
        activity.findViewById(R.id.custom).setOnClickListener(this);

        return this;
    }

    public void loadAbout() {
        final FrameLayout flHolder = activity.findViewById(R.id.about);

        AboutBuilder builder = AboutBuilder.with(activity)
                .setAppIcon(R.mipmap.ic_launcher)
                .setAppName(R.string.app_name)
                //.setPhoto(R.mipmap.ic_launcher)
                //.setCover(R.mipmap.profile_cover)
                .setLinksAnimated(true)
                .setDividerDashGap(13)
                //.setName("Eric Kogi Kimani")
                //.setSubTitle("Mobile Developer")
                //.setLinksColumnsCount(4)
                //.setBrief("I love building great apps.")
                //.addGooglePlayStoreLink("")
                //.addGitHubLink("erickogi")
                //.addBitbucketLink("jrvansuita")
                //.addFacebookLink("LishaBoraKenya")
                //.addTwitterLink("lishaborakenya")
                //.addInstagramLink("lishaborakenya")
                //.addGooglePlusLink("+JuniorVansuita")
                //.addYoutubeChannelLink("UChPT2CEkO00HaKesO34_-tA")
                //.addDribbbleLink("user")
                //.addLinkedInLink("lishabora-ltd")
                // .addEmailLink("vansuita.jr@gmail.com")
                // .addWhatsappLink("Eric Kogi", "+254714406984")
                //.addSkypeLink("user")
                //.addGoogleLink("user")
                //.addAndroidLink("user")
                .addWebsiteLink("https://www.zaiditechnologies.com/")
                .addFiveStarsAction()
                //.addMoreFromMeAction("Vansuita")
                .setVersionNameAsAppSubTitle()
                .addShareAction(R.string.app_name)
                .addUpdateAction()
                .setActionsColumnsCount(2)
                .addFeedbackAction("erickogi14@gmail.com")

                //.setName("Eric Kogi Kimani")
                .addPrivacyPolicyAction("https://www.zaiditechnologies.com/privacypolicy")
                //.addIntroduceAction((Intent) null)
                //.addHelpAction((Intent) null)
                //.addChangeLogAction((Intent) null)
                // .addRemoveAdsAction((Intent) null)
                // .addDonateAction((Intent) null)
                .setWrapScrollView(true)
                .setShowAsCard(true);

        AboutView view = builder.build();

        flHolder.addView(view);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            default:
                break;
        }
    }
}

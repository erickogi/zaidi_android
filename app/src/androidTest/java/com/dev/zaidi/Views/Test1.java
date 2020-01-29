package com.dev.zaidi.Views;


import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.dev.lishaboramobile.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class Test1 {

    @Rule
    public ActivityTestRule<SplashActivity> mActivityTestRule = new ActivityTestRule<>(SplashActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.READ_SMS");

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    @Test
    public void test1() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textInputEditText = onView(
                allOf(withId(R.id.edt_phone),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.til_phone),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText.perform(click());

        ViewInteraction textInputEditText2 = onView(
                allOf(withId(R.id.edt_phone),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.til_phone),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText2.perform(replaceText("744 406 984"), closeSoftKeyboard());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textInputEditText3 = onView(
                allOf(withId(R.id.edt_phone), withText("744 406 984"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.til_phone),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText3.perform(click());

        ViewInteraction textInputEditText4 = onView(
                allOf(withId(R.id.edt_phone), withText("744 406 984"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.til_phone),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText4.perform(replaceText("714 406 984"));

        ViewInteraction textInputEditText5 = onView(
                allOf(withId(R.id.edt_phone), withText("714 406 984"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.til_phone),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText5.perform(closeSoftKeyboard());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.btn_next_phone_view), withText("Next"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.card_phone_view),
                                        0),
                                3),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction textInputEditText6 = onView(
                allOf(withId(R.id.edt_new_password),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.til_password),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText6.perform(replaceText("qw"), closeSoftKeyboard());

        ViewInteraction checkableImageButton = onView(
                allOf(withId(R.id.text_input_password_toggle), withContentDescription("Show password"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.til_password),
                                        0),
                                1),
                        isDisplayed()));
        checkableImageButton.perform(click());

        ViewInteraction textInputEditText7 = onView(
                allOf(withId(R.id.edt_new_password), withText("qw"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.til_password),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText7.perform(replaceText("qwerty"));

        ViewInteraction textInputEditText8 = onView(
                allOf(withId(R.id.edt_new_password), withText("qwerty"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.til_password),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText8.perform(closeSoftKeyboard());

        ViewInteraction textInputEditText9 = onView(
                allOf(withId(R.id.edt_new_confirm_password),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.til__confirm_password),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText9.perform(replaceText("qwerty"), closeSoftKeyboard());

        ViewInteraction checkableImageButton2 = onView(
                allOf(withId(R.id.text_input_password_toggle), withContentDescription("Show password"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.til__confirm_password),
                                        0),
                                1),
                        isDisplayed()));
        checkableImageButton2.perform(click());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.btn_new_pass_next), withText("Next"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                2)));
        materialButton2.perform(scrollTo(), click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatButton = onView(
                allOf(withId(android.R.id.button1), withText("Continue"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.buttonPanel),
                                        0),
                                3)));
        appCompatButton.perform(scrollTo(), click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        pressBack();

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.key5), withText("5"),
                        childAtPosition(
                                allOf(withId(R.id.key5Container),
                                        childAtPosition(
                                                withId(R.id.numberKeyboard),
                                                4)),
                                0)));
        appCompatTextView.perform(scrollTo(), click());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.btn_positive), withText("Save"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layout_custom_alert_button),
                                        1),
                                3),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.AppBarLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.material_drawer_recycler_view),
                        childAtPosition(
                                withId(R.id.material_drawer_slider_layout),
                                1)));
        recyclerView.perform(actionOnItemAtPosition(2, click()));

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withContentDescription("Open"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.AppBarLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.material_drawer_recycler_view),
                        childAtPosition(
                                withId(R.id.material_drawer_slider_layout),
                                1)));
        recyclerView2.perform(actionOnItemAtPosition(1, click()));

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(16);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(R.id.key3), withText("3"),
                        childAtPosition(
                                allOf(withId(R.id.key3Container),
                                        childAtPosition(
                                                withId(R.id.numberKeyboard),
                                                2)),
                                0)));
        appCompatTextView2.perform(scrollTo(), click());

        ViewInteraction appCompatTextView3 = onView(
                allOf(withId(R.id.key6), withText("6"),
                        childAtPosition(
                                allOf(withId(R.id.key6Container),
                                        childAtPosition(
                                                withId(R.id.numberKeyboard),
                                                5)),
                                0)));
        appCompatTextView3.perform(scrollTo(), click());

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.btn_positive), withText("Save"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layout_custom_alert_button),
                                        1),
                                3),
                        isDisplayed()));
        materialButton4.perform(click());

        ViewInteraction materialButton5 = onView(
                allOf(withId(R.id.btn_positive), withText("Save"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layout_custom_alert_button),
                                        1),
                                3),
                        isDisplayed()));
        materialButton5.perform(click());

        ViewInteraction appCompatTextView4 = onView(
                allOf(withId(R.id.key2), withText("2"),
                        childAtPosition(
                                allOf(withId(R.id.key2Container),
                                        childAtPosition(
                                                withId(R.id.numberKeyboard),
                                                1)),
                                0)));
        appCompatTextView4.perform(scrollTo(), click());

        ViewInteraction materialButton6 = onView(
                allOf(withId(R.id.btn_positive), withText("Save"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layout_custom_alert_button),
                                        1),
                                3),
                        isDisplayed()));
        materialButton6.perform(click());

        ViewInteraction materialButton7 = onView(
                allOf(withId(R.id.btn_positive), withText("Save"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layout_custom_alert_button),
                                        1),
                                3),
                        isDisplayed()));
        materialButton7.perform(click());
    }
}

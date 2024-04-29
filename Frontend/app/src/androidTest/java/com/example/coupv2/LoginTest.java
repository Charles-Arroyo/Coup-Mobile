package com.example.coupv2;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule = new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void normalLogin() {
        String email = "pizza";
        String password = "123";

        onView(withId(R.id.login_email_edt)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.login_password_edt)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.login_login_btn)).perform(click());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {}

        onView(withId(R.id.logoff_btn)).check(matches(isClickable())).perform(click());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {}

        onView(withText("Yes")).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {}

        onView(withId(R.id.main_login_btn)).check(matches(isClickable())).perform(click());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {}

    }


    @Test
    public void adminLogin() {
        String email = "ADMIN";
        String password = "ADMIN";

        onView(withId(R.id.login_email_edt)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.login_password_edt)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.login_login_btn)).perform(click());

        //Wait a brief momemnt so next activity can Process
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {}



    }

    @Test
    public void noPasswordLogin() {
        String email = "bpd@email.com";
        onView(withId(R.id.login_email_edt)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.login_login_btn)).perform(click());

        // Check for the specific error handling here, e.g., Toast message
    }

    @Test
    public void noEmailLogin() {
        String password = "password123";
        onView(withId(R.id.login_password_edt)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.login_login_btn)).perform(click());

        onView(withId(R.id.login_login_btn)).perform(click());

        // Check for the specific error handling here, e.g., Toast message
    }
}

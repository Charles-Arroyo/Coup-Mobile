package com.example.coupv2;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SignUpTest {

    @Rule
    public ActivityScenarioRule<SignupActivity> activityRule = new ActivityScenarioRule<>(SignupActivity.class);


    @Test
    public void successfulSignup() {
        String name = "Mitra";
        String email = "Mitra@iastate.edu";
        String password = "Mitra";

        onView(withId(R.id.signup_username_edt)).perform(typeText(name), closeSoftKeyboard());
        onView(withId(R.id.signup_email_edt)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.signup_password_edt)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.signup_signup_btn)).perform(click());

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {}


    }


    @Test
    public void invalidEmail() {
        String name = "Mitra";
        String email = "Mitra";
        String password = "Mitra";

        onView(withId(R.id.signup_username_edt)).perform(typeText(name), closeSoftKeyboard());
        onView(withId(R.id.signup_email_edt)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.signup_password_edt)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.signup_signup_btn)).perform(click());

        //Wait a brief momemnt so next activity can Process
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {}
    }


    @Test
    public void noPassword() {
        String name = "Mitra";
        String email = "Mitra";

        onView(withId(R.id.signup_username_edt)).perform(typeText(name), closeSoftKeyboard());
        onView(withId(R.id.signup_email_edt)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.signup_signup_btn)).perform(click());

        //Wait a brief momemnt so next activity can Process
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {}
    }


    @Test
    public void noName() {
        String email = "Mitra";
        String password = "Mitra";

        onView(withId(R.id.signup_email_edt)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.signup_password_edt)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.signup_signup_btn)).perform(click());

        //Wait a brief momemnt so next activity can Process
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {}
    }

    @Test
    public void noEmail() {
        String name = "Mitra";
        String password = "Mitra";

        onView(withId(R.id.signup_username_edt)).perform(typeText(name), closeSoftKeyboard());
        onView(withId(R.id.signup_password_edt)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.signup_signup_btn)).perform(click());

        //Wait a brief momemnt so next activity can Process
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {}
    }
}

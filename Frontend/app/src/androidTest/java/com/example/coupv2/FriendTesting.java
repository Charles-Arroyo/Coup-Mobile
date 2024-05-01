package com.example.coupv2;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class FriendTesting {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule = new ActivityScenarioRule<>(LoginActivity.class);

    public String email = "pizza";
    public String password = "123";

    public String email2 = "billy";
    public String password2 = "123";

    public void LoginUser1() {
        onView(withId(R.id.login_email_edt)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.login_password_edt)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.login_login_btn)).perform(click());


    }
    public void LoginUser2() {

        onView(withId(R.id.login_email_edt)).perform(typeText(email2), closeSoftKeyboard());
        onView(withId(R.id.login_password_edt)).perform(typeText(password2), closeSoftKeyboard());
        onView(withId(R.id.login_login_btn)).perform(click());

    }

    @Test
    public void testRule1(){
        LoginUser1();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {}
        onView(withId(R.id.rules_btn)).check(matches(isClickable())).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {}
        onView(withId(R.id.assassin)).check(matches(isClickable())).perform(click());

    }

    @Test
    public void testRule2(){
        LoginUser2();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {}
        onView(withId(R.id.rules_btn)).check(matches(isClickable())).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {}
        onView(withId(R.id.captain)).check(matches(isClickable())).perform(click());

    }

    @Test
    public void testRank(){
        LoginUser1();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {}
        onView(withId(R.id.ranking_btn)).check(matches(isClickable())).perform(click());

    }

    @Test
    public void testStats1(){

        LoginUser1();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {}
        onView(withId(R.id.stats_btn)).check(matches(isClickable())).perform(click());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {}
        onView(withId(R.id.back_stats)).check(matches(isClickable())).perform(click());

    }
    @Test
    public void testStats2(){

        LoginUser2();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {}
        onView(withId(R.id.stats_btn)).check(matches(isClickable())).perform(click());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {}
        onView(withId(R.id.back_stats)).check(matches(isClickable())).perform(click());

    }
}

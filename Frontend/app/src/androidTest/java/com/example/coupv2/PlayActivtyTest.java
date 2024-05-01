//package com.example.coupv2;
//
//import static androidx.test.espresso.Espresso.onView;
//import static androidx.test.espresso.matcher.ViewMatchers.withId;
//
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//import androidx.test.rule.ActivityTestRule;
//
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//@RunWith(AndroidJUnit4.class)
//public class PlayActivtyTest {
//
//    @Rule
//    public ActivityTestRule<PlayActivity> activityTestRule = new ActivityTestRule<>(PlayActivity.class, true, false);
//
//    @Test
//    public void testOnWebSocketMessage() {
//        // Manually launch the activity
//        PlayActivity activity = activityTestRule.launchActivity(null);
//
//        // Since the WebSocket callback happens on a background thread,
//        // you need to make sure to run the test on the UI thread.
//        activity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                // This simulates receiving a message from WebSocket.
//                String jsonString = "{"
//                        + "    \"Game\": {"
//                        + "        \"deck\": \"Deck: [Contessa, Ambassador, Duke, Contessa, Duke, Assassin, Assassin]\","
//                        + "        \"lastCharacterMove\": null,"
//                        + "        \"currentPlayer\": {"
//                        + "            \"userEmail\": \"rat\","
//                        + "            \"cardOne\": \"Ambassador\","
//                        + "            \"cardTwo\": \"Captain\","
//                        + "            \"coins\": 2,"
//                        + "            \"turn\": true,"
//                        + "            \"lives\": 2,"
//                        + "            \"turnNumber\": 1"
//                        + "        },"
//                        + "        \"playerArrayList\": ["
//                        + "            {"
//                        + "                \"userEmail\": \"rat\","
//                        + "                \"cardOne\": \"Ambassador\","
//                        + "                \"cardTwo\": \"Captain\","
//                        + "                \"coins\": 2,"
//                        + "                \"turn\": true,"
//                        + "                \"lives\": 2,"
//                        + "                \"turnNumber\": 1"
//                        + "            },"
//                        + "            {"
//                        + "                \"userEmail\": \"cat\","
//                        + "                \"cardOne\": \"Ambassador\","
//                        + "                \"cardTwo\": \"Duke\","
//                        + "                \"coins\": 2,"
//                        + "                \"turn\": false,"
//                        + "                \"lives\": 2,"
//                        + "                \"turnNumber\": 2"
//                        + "            },"
//                        + "            {"
//                        + "                \"userEmail\": \"bat\","
//                        + "                \"cardOne\": \"Captain\","
//                        + "                \"cardTwo\": \"Assassin\","
//                        + "                \"coins\": 2,"
//                        + "                \"turn\": false,"
//                        + "                \"lives\": 2,"
//                        + "                \"turnNumber\": 3"
//                        + "            },"
//                        + "            {"
//                        + "                \"userEmail\": \"tat\","
//                        + "                \"cardOne\": \"Contessa\","
//                        + "                \"cardTwo\": \"Captain\","
//                        + "                \"coins\": 2,"
//                        + "                \"turn\": false,"
//                        + "                \"lives\": 2,"
//                        + "                \"turnNumber\": 4"
//                        + "            }"
//                        + "        ]"
//                        + "    }"
//                        + "}";
//
//                activity.onWebSocketMessage(jsonString);
//                // Here you can check if the UI was updated correctly.
////                onView(withId(R.id.emailTextView)).check(matches(withText("rat")));
//            }
//        });
//
//        // Use Espresso to verify UI changes, if any
//        // For example, if a TextView should display the user email from the message:
//        // onView(withId(R.id.userEmailTextView)).check(matches(withText("expected email")));
//    }
//}


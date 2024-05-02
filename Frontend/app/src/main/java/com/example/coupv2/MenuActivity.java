package com.example.coupv2;


import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.coupv2.utils.Const;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MenuActivity extends AppCompatActivity implements WebSocketListener {

    /*
        LINKS
        -----------------------------------
        RANKINGS

        private static final String URL_RANKINGS = "http://coms-309-023.class.las.iastate.edu:8443/getListUserRanking";

        ------------------------------------
        GLOBAL CHAT FEATURE

        private String BASE_URL = "ws://coms-309-023.class.las.iastate.edu:8443/chat/";
        private String BASE_URL = "ws://10.0.2.2:8443/chat/";
        private String BASE_URL = "ws://10.29.182.205:8443/chat/";

     */
    private static final String URL_RANKINGS = "http://coms-309-023.class.las.iastate.edu:8443/getListUserRanking";
    private final String BASE_URL = "ws://coms-309-023.class.las.iastate.edu:8443/chat/";
    private ImageButton backButton, msgButton, logoffButton, settingsButton, leaderboardButton, themeButton;
    private EditText msg;
    private LinearLayout layoutMessages;
    private ScrollView scrollViewMessages;
    private BottomSheetDialog bottomSheetDialog;

    /**
     *  List of messages that that are dynamically added to the linear layout
     */
    private final ArrayList<String> messagesList = new ArrayList<>();
    private final String user = Const.getCurrentEmail();
    private Button sendBtn;
    private TextView playButton, friendsButton,  statsButton, rulesButton;

    private ImageView icon;
    private int speed = 100;
    private int Themes;


    /**
     *
     * Our main file in which our Main Menu will be displayed
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        fetchTheme();
//        Const.setCurrentTheme(Themes);
//        setTheme(Const.getCurrentTheme());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu); // Set the layout for this activity

        Toast.makeText(this, "WELCOME!!!", Toast.LENGTH_SHORT).show();

        // Initialize UI elements
        playButton = findViewById(R.id.game);
        friendsButton = findViewById(R.id.friends_button);
        settingsButton = findViewById(R.id.settings_btn);
        statsButton = findViewById(R.id.stats_btn);
        rulesButton = findViewById(R.id.rules_btn);
        logoffButton = findViewById(R.id.logoff_btn);
        leaderboardButton = findViewById(R.id.ranking_btn);
        msgButton = findViewById(R.id.msg_btn);
//        themeButton = findViewById(R.id.theme_menu);

        icon = findViewById(R.id.icon);
        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.hyperspace_jump);
        Animation slideInAnimation = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        Animation slideOutAnimation = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);

        icon.startAnimation(fadeInAnimation);


        icon.setOnClickListener(v -> {
            icon.startAnimation(fadeInAnimation);
            fadeInAnimation.setDuration(speed);
            speed += 30;

        });

        // Play Button
        playButton.setOnClickListener(v -> {
            // Start the looby activity
            Intent intent = new Intent(MenuActivity.this, LobbyActivity.class);
            //for going straight to PlayActivity
//                Intent intent = new Intent(MenuActivity.this, PlayActivity.class);
            startActivity(intent);
        });
        // Friends Button
        friendsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, FriendsActivity.class);
            startActivity(intent);
        });
        // Settings Button
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, SettingActivity.class);
            startActivity(intent);
        });
        // Stats Button
        statsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, StatsActivity.class);
            intent.putExtra("USER", user);
            startActivity(intent);
        });
        // Return Button
        logoffButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
            builder.setTitle("Confirm Logoff");
            builder.setMessage("Are you sure you want to log off?");

            builder.setPositiveButton("Yes", (dialog, which) -> {
                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                startActivity(intent);
                WebSocketManager2.getInstance().disconnectWebSocket();
                finish();
            });
            builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();
        });
        // Rules Button
        rulesButton.setOnClickListener(v -> showRules());
        // Message Button
        msgButton.setOnClickListener(v -> showGlbChat());
        // Ranking Button
        leaderboardButton.setOnClickListener(v -> showRankingPopup());

//
//        themeButton.setOnClickListener(v -> {
//            // Creating the PopupMenu
//            PopupMenu popup = new PopupMenu(MenuActivity.this, themeButton);
//            popup.getMenuInflater().inflate(R.menu.theme_menu, popup.getMenu());
//
//            popup.setOnMenuItemClickListener(item -> {
//                int id = item.getItemId();
//
//                if (id == R.id.action_dark_purple) {
//                    updateUserTheme(R.style.DarkThemePurple);
//                    fetchTheme();
//                    Const.setCurrentTheme(Themes);
//                    setTheme(Const.getCurrentTheme());
//                } else if (id == R.id.action_light_purple) {
//                    updateUserTheme(R.style.DarkThemePurple);
//                    fetchTheme();
//                    Const.setCurrentTheme(Themes);
//                    setTheme(Const.getCurrentTheme());
//                } else if (id == R.id.action_dark_amber) {
//                    updateUserTheme(R.style.DarkThemePurple);
//                    fetchTheme();
//                    Const.setCurrentTheme(Themes);
//                    setTheme(Const.getCurrentTheme());
//                } else if (id == R.id.action_light_amber) {
//                    updateUserTheme(R.style.DarkThemePurple);
//                    fetchTheme();
//                    Const.setCurrentTheme(Themes);
//                    setTheme(Const.getCurrentTheme());
//                } else if (id == R.id.action_dark_turquoise) {
//                    updateUserTheme(R.style.DarkThemePurple);
//                    fetchTheme();
//                    Const.setCurrentTheme(Themes);
//                    setTheme(Const.getCurrentTheme());
//                } else if (id == R.id.action_light_turquoise) {
//                    updateUserTheme(R.style.DarkThemePurple);
//                    fetchTheme();
//                    Const.setCurrentTheme(Themes);
//                    setTheme(Const.getCurrentTheme());
//                }
//                // Apply the theme change by recreating the current activity
//                recreate();
//
//                return true;
//            });
//
//            // Showing the popup
//            popup.show();
//        });


    }


    /**
     * Popup function to show our ranking layout with all its features
     */

    private void showRankingPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup_ranking, null);
        builder.setView(view);

        LinearLayout rankingLayout = view.findViewById(R.id.ranking_layout);
        fetchRankings(rankingLayout);

        builder.setCancelable(true);

        AlertDialog dialog = builder.create();

        Button closeBtn = view.findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    /**
     * parsing the list of people inside the rankingLayout view
     *
     * @param rankingLayout view where the list of players are
     */

    private void fetchRankings(final LinearLayout rankingLayout) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL_RANKINGS, null,
                response -> {
                    try {
                        JSONArray rankingsArray = response.getJSONArray("rankings");
                        for (int i = 0; i < rankingsArray.length(); i++) {
                            JSONObject rankingObject = rankingsArray.getJSONObject(i);
                            int rank = rankingObject.getInt("rank");
                            String username = rankingObject.getString("username");
                            int score = rankingObject.getInt("score");

                            addUserToRanking(rankingLayout, username, score, rank);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing ranking data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Error fetching rankings: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    /**
     *  Adds users to the ranking, similar to AddMessageLayout
     * <p>
     *  Also if user ranked top 3, users background tint will change ba
     *
     * @param rankingLayout the area of layout which display the list of players
     * @param username add username to know ranking
     * @param score sees score to track
     * @param rank placement in the leaderboard
     */

    private void addUserToRanking(LinearLayout rankingLayout, String username, int score, int rank) {
        View rankingItemView = getLayoutInflater().inflate(R.layout.rank_item, rankingLayout, false);

        TextView tvRank = rankingItemView.findViewById(R.id.tvRank);
        Button btnUsername = rankingItemView.findViewById(R.id.btnUsername);
        TextView tvScore = rankingItemView.findViewById(R.id.tvScore);

        tvRank.setText(String.valueOf(rank));
        btnUsername.setText(username);
        tvScore.setText(String.valueOf(score));
        if (rank == 1) {
            btnUsername.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.gold)));
        } else if (rank == 2) {
            btnUsername.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.silver)));
        } else if (rank == 3) {
            btnUsername.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.bronze)));
        } else {
            btnUsername.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.defaultBackground))); // Default background color
        }


        btnUsername.setOnClickListener(v -> {
            showUserStats(username);
        });

        rankingLayout.addView(rankingItemView);
    }

    /**
     * When pressing the users button, display the user stats
     *
     * @param username requires the
     */

    private void showUserStats(String username) {
        Toast.makeText(this, "user: " + username, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, StatsActivity.class);
        intent.putExtra("USER", username);

//         Launch StatsActivity as a dialog
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }
    /**
     * Sets a bottom dialog for a global messaging activity which connects from everyone in the game
     */

    private void showGlbChat() {
        bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.activity_glb_msg, null);
        bottomSheetDialog.setContentView(view);

        msg = view.findViewById(R.id.msg);
        scrollViewMessages = view.findViewById(R.id.scrollViewMessages);
        layoutMessages = view.findViewById(R.id.layoutMessages);
        sendBtn = view.findViewById(R.id.send_btn);
        backButton = view.findViewById(R.id.back_btn);

        sendBtn.setOnClickListener(v -> {
            String messageToSend = msg.getText().toString().trim();
            if (!messageToSend.isEmpty()) {
                WebSocketManager.getInstance().sendMessage(messageToSend);
                msg.setText("");
            }
        });
        connectToWebSocket();

        backButton.setOnClickListener(v -> bottomSheetDialog.dismiss());

        bottomSheetDialog.setOnDismissListener(dialogInterface -> WebSocketManager.getInstance().disconnectWebSocket());

        bottomSheetDialog.show();

    }

    /**
     * Basic call method to help connect to the server
     */

    private void connectToWebSocket() {
        String serverUrl = BASE_URL + user;
        WebSocketManager.getInstance().connectWebSocket(serverUrl);
        WebSocketManager.getInstance().setWebSocketListener(this);
    }

    /**
     * Used to write the message, with code to help parse the username from message
     *
     * @param fullMessage The received WebSocket message.
     */

    public void onWebSocketMessage(String fullMessage) {
        runOnUiThread(() -> {
            int colonIndex = fullMessage.indexOf(":");

            if (colonIndex != -1) {
                String username = fullMessage.substring(0, colonIndex).trim();
                String message = fullMessage.substring(colonIndex + 1).trim();

                addMessageToLayout(username, message);
            } else {
                addMessageToLayout("Server", fullMessage);
            }
        });
    }

    /**
     * Method to help dynamically add messages in the scrollview
     *
     * @param username adds username as a button
     * @param message websocket receives messages
     */

    private void addMessageToLayout(String username, String message) {
        View messageView = getLayoutInflater().inflate(R.layout.message_item, layoutMessages, false);

        TextView textView = messageView.findViewById(R.id.placement);
        Button usernameButton = messageView.findViewById(R.id.btnUsername);

        textView.setText(message);
        usernameButton.setText(username);

        usernameButton.setOnClickListener(v -> showUserStats(username));

        layoutMessages.addView(messageView);
        scrollViewMessages.post(() -> scrollViewMessages.fullScroll(ScrollView.FOCUS_DOWN));
    }

    /**
     * connects websocket
     *
     * @param handshakedata Information about the server handshake.
     */

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
    }


    /**
     * Sends a message if server was closed
     *
     * @param code   The status code indicating the reason for closure.
     * @param reason A human-readable explanation for the closure.
     * @param remote Indicates whether the closure was initiated by the remote endpoint.
     */
    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        runOnUiThread(() -> {
            String source = remote ? "server" : "local";
            String message = "Connection closed by " + source + ". Reason: " + reason;
            messagesList.add(message);
        });
    }

    /**
     * Checks and sends user message of error
     *
     * @param ex The exception that describes the error.
     */
    @Override
    public void onWebSocketError(Exception ex) {
        runOnUiThread(() -> {
            messagesList.add("WebSocket error: " + ex.getMessage());
        });
    }

    /**
     * Pops up a bottom dialog, to show up the rules, and cards of the game
     */

    private void showRules() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.activity_rules, null);
        bottomSheetDialog.setContentView(view);

        // Set up for the assassin
        ImageButton assassinButton = view.findViewById(R.id.assassin);
        assassinButton.setOnClickListener(v -> showImagePopup(R.drawable.assassin1));

        // Set up for the captain
        ImageButton captainButton = view.findViewById(R.id.captain);
        captainButton.setOnClickListener(v -> showImagePopup(R.drawable.captain1));

        // Set up for the duke
        ImageButton dukeButton = view.findViewById(R.id.duke);
        dukeButton.setOnClickListener(v -> showImagePopup(R.drawable.duke1));

        // Set up for the contra
        ImageButton contraButton = view.findViewById(R.id.contra);
        contraButton.setOnClickListener(v -> showImagePopup(R.drawable.contra1));


        // Set up for the ambassador
        ImageButton ambassadorButton = view.findViewById(R.id.ambassador);
        ambassadorButton.setOnClickListener(v -> showImagePopup(R.drawable.ambassador1));

        // Close button inside the BottomSheetDialog
        Button closeButton = view.findViewById(R.id.close_rules_coup_overlay_button);
        closeButton.setOnClickListener(v -> bottomSheetDialog.dismiss());

        bottomSheetDialog.show();
    }

    /**
     * shows the image in a dismissible popup
     *
     * @param imageResource resource image in our directory.
     */

    private void showImagePopup(int imageResource) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogLayout = getLayoutInflater().inflate(R.layout.popup_image, null);
        ImageView imageView = dialogLayout.findViewById(R.id.popup_image);

        imageView.setImageResource(imageResource);
        builder.setView(dialogLayout);
        AlertDialog dialog = builder.create();

        dialogLayout.setOnClickListener(v -> dialog.dismiss());

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        dialog.show();
    }

    /**
     * Websocket method to reconnect the user when leaving activity
     */

    @Override
    protected void onResume() {
        super.onResume();
        WebSocketManager.getInstance().setWebSocketListener(this);
    }

    /**
     * Websocket method to pause websocket connection when leaving activity
     */

    @Override
    protected void onPause() {
        super.onPause();
        WebSocketManager.getInstance().removeWebSocketListener();
    }

    /**
     * Method to disconnect when server breaks in the Web Socket
     */

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WebSocketManager.getInstance().removeWebSocketListener();
    }


//    private void fetchTheme() {
//        String url = "http://coms-309-023.class.las.iastate.edu:8443/themes/" + user;
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, null, null,
//                response -> {
//                    Log.d("ThemeActivity", "Response received: " + response.toString()); // Debug log
//                    try {
//                        Themes = response.getInt("theme");
//
//                    } catch (JSONException e) {
//                        Toast.makeText(MenuActivity.this, "Error parsing JSON data", Toast.LENGTH_SHORT).show();
//                        e.printStackTrace();
//                    }
//                },
//                error -> {
//                    Log.e("StatsActivity", "Volley error: " + error.toString());
//                    Toast.makeText(getApplicationContext(), "Failed to load stats", Toast.LENGTH_SHORT).show();
//                    if (error.networkResponse != null) {
//                        Log.e("StatsActivity", "Error Response body: " + new String(error.networkResponse.data));
//                    }
//                }
//        );
//
//        RequestQueue requestQueue = AppController.getInstance().getRequestQueue();
//        requestQueue.add(jsonObjectRequest);
//    }
//
//    private void updateUserTheme(int theme) {
//        String url = "http://coms-309-023.class.las.iastate.edu:8443/themes/" + user;
//
//        JSONObject jsonRequest = new JSONObject();
//        try {
//            jsonRequest.put("theme", theme); // newEmail is the updated email provided by the user
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Toast.makeText(MenuActivity.this, "Error creating update request", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        RequestQueue requestQueue = AppController.getInstance().getRequestQueue();
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonRequest,
//                response -> {
//                    try {
//                        boolean success = response.getBoolean("success");
//                        if (success) {
//                            Toast.makeText(MenuActivity.this, "Theme updated successfully", Toast.LENGTH_SHORT).show();
//                            Const.setCurrentTheme(theme);
//                        } else {
//                            Toast.makeText(MenuActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        Toast.makeText(MenuActivity.this, "Invalid response from server", Toast.LENGTH_SHORT).show();
//                    }
//                },
//                error -> {
//                    if (error.networkResponse != null) {
//                        String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
//                        Log.e("MenuActivity", "Network Response Body: " + responseBody);
//                        try {
//                            new JSONObject(responseBody);
//                        } catch (JSONException e) {
//                            Log.e("MenuActivity", "Response is not valid JSON", e);
//                        }
//                    } else {
//                        Log.e("MenuActivity", "Network Error: " + error.toString());
//                    }
//                    Toast.makeText(MenuActivity.this, "Network Error: " + error.toString(), Toast.LENGTH_LONG).show();
//                }
//
//        );
//
//        requestQueue.add(jsonObjectRequest);
//    }
}


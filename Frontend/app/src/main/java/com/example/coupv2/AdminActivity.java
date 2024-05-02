package com.example.coupv2;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
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
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.coupv2.utils.Const;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity implements WebSocketListener  {


    /*
        LINKS
        -----------------------------------
        RANKINGS

        private static final String URL_RANKINGS = "http://coms-309-023.class.las.iastate.edu:8445/getListUserRanking";
        private static final String URL_RANKINGS = "http://coms-309-023.class.las.iastate.edu:8O80/getListUserRanking";
        private static final String URL_RANKINGS = "http://coms-309-023.class.las.iastate.edu:8O80/getListUserRanking";

        ------------------------------------
        GLOBAL CHAT FEATURE

        private String BASE_URL = "ws://coms-309-023.class.las.iastate.edu:8445/chat/";
        private String BASE_URL = "ws://10.0.2.2:8443/chat/";
        private String BASE_URL = "ws://10.29.182.205:8443/chat/";

     */


    private static final String URL_RANKINGS = "http://coms-309-023.class.las.iastate.edu:8443/getListUserRanking";
    private final String BASE_URL = "ws://coms-309-023.class.las.iastate.edu:8443/chat/";
    private ImageButton backButton, msgButton, logoffButton, leaderboardButton;
    private EditText msg;
    private LinearLayout layoutMessages;
    private ScrollView scrollViewMessages;
    private BottomSheetDialog bottomSheetDialog;

    /**
     *  List of messages that that are dynamically added to the linear layout
     */
    private final ArrayList<String> messagesList = new ArrayList<>();
    private final String user = Const.getCurrentEmail();
    private Button sendBtn, playButton, listButton,  statsButton, rulesButton;


    /**
     *
     * Our main file in which our Main Menu will be displayed
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.DarkThemeRed);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu); // Set the layout for this activity

        // Initialize UI elements
        playButton = findViewById(R.id.game);
        listButton = findViewById(R.id.friends_button);
        statsButton = findViewById(R.id.stats_btn);
        rulesButton = findViewById(R.id.rules_btn);
        logoffButton = findViewById(R.id.logoff_btn);
        leaderboardButton = findViewById(R.id.ranking_btn);
        msgButton = findViewById(R.id.msg_btn);

        // Play Button
        playButton.setOnClickListener(v -> {
            // Start the play activity
            Intent intent = new Intent(AdminActivity.this, LobbyActivity.class);
            startActivity(intent);
        });
        // Friends Button
        listButton.setOnClickListener(v -> {
            // Start the friends activity
            Intent intent = new Intent(AdminActivity.this, ListActivity.class);
            startActivity(intent);
        });
        // Stats Button
        statsButton.setOnClickListener(v -> showGlobalStatsPopup());
        // Return Button
        logoffButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
            builder.setTitle("Confirm Logoff");
            builder.setMessage("Are you sure you want to log off?");

            builder.setPositiveButton("Yes", (dialog, which) -> {
                Intent intent = new Intent(AdminActivity.this, MainActivity.class);
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


        btnUsername.setOnClickListener(v -> showUserStats(username));

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

    private void showGlobalStatsPopup() {
//        String statsUrl = "https://3a856af0-b6ac-48f3-a93a-06d2cd454e01.mock.pstmn.io/GLB";
        String statsUrl = "http://coms-309-023.class.las.iastate.edu:8443/globalStat";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, statsUrl, null,
                response -> {
                    try {
                        int totalPlayers = response.getInt("total_players");
                        int activePlayers = response.getInt("active_players");
                        int inactivePlayers = response.getInt("not_active_players");

                        // Update the UI using the fetched data
                        updateGlobalStatsUI(totalPlayers, activePlayers, inactivePlayers);
                    } catch (JSONException e) {
                        Toast.makeText(AdminActivity.this, "Error parsing statistics: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(AdminActivity.this, "Error fetching statistics: " + error.toString(), Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    private void updateGlobalStatsUI(int totalPlayers, int activePlayers, int inactivePlayers) {
        View view = getLayoutInflater().inflate(R.layout.popup_global_stats, null);

        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);

        TextView TotalPlayers = view.findViewById(R.id.glb_stats_total_players);
        TextView ActivePlayers = view.findViewById(R.id.glb_stats_active_players);
        TextView InactivePlayers = view.findViewById(R.id.glb_stats_inactive_players);
        Button closeButton = view.findViewById(R.id.gbl_stats_back_btn);

        TotalPlayers.setText("Total Players: " + totalPlayers);
        ActivePlayers.setText("Active Players: " + activePlayers);
        InactivePlayers.setText("Inactive Players: " + inactivePlayers);

        closeButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }


}

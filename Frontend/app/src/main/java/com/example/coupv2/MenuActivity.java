package com.example.coupv2;

import android.content.Intent;
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

import com.example.coupv2.utils.Const;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.java_websocket.handshake.ServerHandshake;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity implements WebSocketListener {


//    private String BASE_URL = "ws://coms-309-023.class.las.iastate.edu:8080/chat/";
//    private String BASE_URL = "ws://10.0.2.2:8080/chat/";
    private String BASE_URL = "ws://10.29.182.205:8080/chat/";

    private ImageButton backButton, msgButton;
    private EditText msg;
    private LinearLayout layoutMessages;
    private ScrollView scrollViewMessages;
    private BottomSheetDialog bottomSheetDialog;

    private ArrayList<String> messagesList = new ArrayList<>();
    private String user = Const.getCurrentEmail();
    private Button sendBtn, playButton, friendsButton, settingsButton, statsButton, rulesButton, backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu); // Set the layout for this activity

        // Initialize UI elements
        playButton = findViewById(R.id.play_btn);
        friendsButton = findViewById(R.id.friends_btn);
        settingsButton = findViewById(R.id.settings_btn);
        statsButton = findViewById(R.id.stats_btn);
        rulesButton = findViewById(R.id.rules_btn);
        backBtn = findViewById(R.id.backBtn);
        msgButton = findViewById(R.id.msg_btn);

        // Play Button
        playButton.setOnClickListener(v -> {
            // Start the play activity
            Intent intent = new Intent(MenuActivity.this, LobbyActivity.class);
            startActivity(intent);
        });
        // Friends Button
        friendsButton.setOnClickListener(v -> {
            // Start the friends activity
            Intent intent = new Intent(MenuActivity.this, FriendsActivity.class);
            startActivity(intent);
        });
        // Settings Button
        settingsButton.setOnClickListener(v -> {
            // Start the settings activity
            Intent intent = new Intent(MenuActivity.this, SettingActivity.class);
            startActivity(intent);
        });
        // Stats Button
        statsButton.setOnClickListener(v -> {
            // Start the statistics activity
            Intent intent = new Intent(MenuActivity.this, StatsActivity.class);
            startActivity(intent);
        });
        backBtn.setOnClickListener(v -> {
            // Start the rules activity
            Intent intent = new Intent(MenuActivity.this, MainActivity.class);
            startActivity(intent);
        });
        // Rules Button
        rulesButton.setOnClickListener(v -> showRules());
        // Message Button
        msgButton.setOnClickListener(v -> {
            showGlbChat();
        });

    }

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

        bottomSheetDialog.setOnDismissListener(dialogInterface -> {
            WebSocketManager.getInstance().disconnectWebSocket();
        });

        bottomSheetDialog.show();

    }

    private void connectToWebSocket() {
        String serverUrl = BASE_URL + user;
        WebSocketManager.getInstance().connectWebSocket(serverUrl);
        WebSocketManager.getInstance().setWebSocketListener(this);
    }

    public void onWebSocketMessage(String fullMessage) {
        runOnUiThread(() -> {
            int colonIndex = fullMessage.indexOf(":"); 
            if (colonIndex != -1) {
                String username = fullMessage.substring(0, colonIndex).trim();
                String message = fullMessage.substring(colonIndex + 1).trim();

                addMessageToLayout(username, message);
            } else {
                // This else block can handle messages that do not fit the "username: message" format
                addMessageToLayout("Server", fullMessage);
            }
        });
    }


    private void addMessageToLayout(String username, String message) {
        View messageView = getLayoutInflater().inflate(R.layout.message_item, layoutMessages, false);

        TextView textView = messageView.findViewById(R.id.tvMessage);
        Button usernameButton = messageView.findViewById(R.id.btnUsername);

        textView.setText(message);
        usernameButton.setText(username);

        usernameButton.setOnClickListener(v -> showUserPopup(username));

        layoutMessages.addView(messageView);
        scrollViewMessages.post(() -> scrollViewMessages.fullScroll(ScrollView.FOCUS_DOWN));
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        runOnUiThread(() -> {
            messagesList.add("Connection closed by " + (remote ? "server" : "local") + ". Reason: " + reason);
        });
    }

    @Override
    public void onWebSocketError(Exception ex) {
        runOnUiThread(() -> {
            messagesList.add("WebSocket error: " + ex.getMessage());
        });
    }

    private void showUserPopup(String username) {
        Toast.makeText(this, "Clicked on user: " + username, Toast.LENGTH_SHORT).show();

        // Setup popup to load the main menu
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(username);
        builder.setMessage("More info about " + username);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showRules() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.activity_rules, null);
        bottomSheetDialog.setContentView(view);

        // Set up the click listener for the assassin ImageButton
        ImageButton assassinButton = view.findViewById(R.id.assassin);
        assassinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePopup(R.drawable.assassin);
            }
        });

        // Set up the click listener for the captain ImageButton
        ImageButton captainButton = view.findViewById(R.id.captain);
        captainButton.setOnClickListener(v -> showImagePopup(R.drawable.captain));

        // Set up the click listener for the duke ImageButton
        ImageButton dukeButton = view.findViewById(R.id.duke);
        dukeButton.setOnClickListener(v -> showImagePopup(R.drawable.duke));

//        // Set up the click listener for the ambassador ImageButton
//        ImageButton ambassadorButton = view.findViewById(R.id.ambassador);
//        ambassadorButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showImagePopup(R.drawable.ambassador); // Make sure this drawable resource exists
//            }
//        });
//
//        // Set up the click listener for the contra ImageButton
//        ImageButton contraButton = view.findViewById(R.id.contra);
//        contraButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showImagePopup(R.drawable.contra); // Make sure this drawable resource exists
//            }
//        });

        // Close button inside the BottomSheetDialog
        Button closeButton = view.findViewById(R.id.close_rules_coup_overlay_button);
        closeButton.setOnClickListener(v -> bottomSheetDialog.dismiss());

        bottomSheetDialog.show();
    }

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
}

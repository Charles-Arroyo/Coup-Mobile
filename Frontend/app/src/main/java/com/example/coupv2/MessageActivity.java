package com.example.coupv2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coupv2.utils.Const;

import org.java_websocket.handshake.ServerHandshake;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity implements WebSocketListener {

    private LinearLayout layoutMessages;
    private EditText msg;
    private TextView title;
    private String user;
    private ScrollView scrollViewMessages;
//    private String BASE_URL = "ws://coms-309-023.class.las.iastate.edu:8080/chatFriend/";
    //    private String BASE_URL = "ws://10.0.2.2:8080/chat/";
    private String BASE_URL = "ws://coms-309-023.class.las.iastate.edu:8080/chatFriend/";

    private ArrayList<String> messagesList = new ArrayList<>();
    private String selectedFriendEmail; // Moved the initialization to onCreate


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Const.getCurrentTheme());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_msg);

        // Initialize selectedFriendEmail here
        Intent intent = getIntent();
        selectedFriendEmail = intent.getStringExtra("friend");

        user = Const.getCurrentEmail();

        BASE_URL = BASE_URL  + user + "/" + selectedFriendEmail;

        WebSocketManager.getInstance().connectWebSocket(BASE_URL);
        WebSocketManager.getInstance().setWebSocketListener(this);


        msg = findViewById(R.id.msg);
        title = findViewById(R.id.tittle);
        scrollViewMessages = findViewById(R.id.scrollViewMessages);
        layoutMessages = findViewById(R.id.layoutMessages);
        Button sendBtn = findViewById(R.id.send_btn);
        ImageButton backButton = findViewById(R.id.back_btn);


        title.setText(selectedFriendEmail);
        sendBtn.setOnClickListener(v -> {
            String messageToSend = msg.getText().toString().trim();
            if (!messageToSend.isEmpty()) {
                WebSocketManager.getInstance().sendMessage(messageToSend);
                msg.setText("");
            }
        });

        backButton.setOnClickListener(v -> {
            Intent backIntent = new Intent(MessageActivity.this, FriendsActivity.class);
            startActivity(backIntent);
            WebSocketManager.getInstance().disconnectWebSocket();
        });
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
        View messageView = getLayoutInflater().inflate(R.layout.friends_msg_item, layoutMessages, false);

        TextView textView = messageView.findViewById(R.id.placement);
        Button usernameButton = messageView.findViewById(R.id.btnUsername);

        textView.setText(message);
        usernameButton.setText(username);


        layoutMessages.addView(messageView);
        scrollViewMessages.post(() -> scrollViewMessages.fullScroll(ScrollView.FOCUS_DOWN));
    }
    private void showUserPopup(String username) {
        Intent intent = new Intent(MessageActivity.this, StatsActivity.class);
        intent.putExtra("USER", user);
        startActivity(intent);
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


}

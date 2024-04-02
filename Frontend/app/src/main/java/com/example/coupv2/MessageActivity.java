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
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
    private String BASE_URL = "ws://coms-309-023.class.las.iastate.edu:8080/chat/";
    //    private String BASE_URL = "ws://10.0.2.2:8080/chat/";
    private ArrayList<String> messagesList = new ArrayList<>();
    private String selectedFriendEmail; // Moved the initialization to onCreate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_msg);

        // Initialize selectedFriendEmail here
        Intent intent = getIntent();
        selectedFriendEmail = intent.getStringExtra("friend");

        user = Const.getCurrentEmail();
        msg = findViewById(R.id.msg);
        title = findViewById(R.id.tittle);
        scrollViewMessages = findViewById(R.id.scrollViewMessages);
        layoutMessages = findViewById(R.id.layoutMessages);
        Button sendBtn = findViewById(R.id.send_btn);
        ImageButton backButton = findViewById(R.id.back_btn);


        String serverUrl = BASE_URL + user;
        WebSocketManager.getInstance().connectWebSocket(serverUrl);
        WebSocketManager.getInstance().setWebSocketListener(MessageActivity.this);

        title.setText(selectedFriendEmail);
        sendBtn.setOnClickListener(v -> {
            String messageToSend = msg.getText().toString().trim();
            if (!messageToSend.isEmpty()) {
                sendMessage(messageToSend); // Use sendMessage method here
                msg.setText("");
            }
        });

        backButton.setOnClickListener(v -> {
            Intent backIntent = new Intent(MessageActivity.this, FriendsActivity.class);
            startActivity(backIntent);
            WebSocketManager.getInstance().disconnectWebSocket();
        });
    }

    private void sendMessage(String message) {
        String fullMessage = selectedFriendEmail != null ? "@" + selectedFriendEmail + " " + message : message;
        WebSocketManager.getInstance().sendMessage(fullMessage);
    }

    @Override
    public void onWebSocketMessage(String fullMessage) {
        runOnUiThread(() -> {
            // Directly display the message as all messages in this activity are part of the conversation
            int colonIndex = fullMessage.indexOf(":");
            if (colonIndex != -1) {
                String username = fullMessage.substring(0, colonIndex).trim();
                String message = fullMessage.substring(colonIndex + 1).trim();

                addMessageToLayout(username, message);
            }
        });
    }

    private void addMessageToLayout(String username, String message) {
        View messageView = getLayoutInflater().inflate(R.layout.friends_msg_item, layoutMessages, false);

        TextView textView = messageView.findViewById(R.id.placement);
        Button usernameButton = messageView.findViewById(R.id.btnUsername);

        textView.setText(message);
        usernameButton.setText(username);

        usernameButton.setOnClickListener(v -> {
            // For example, show a toast or open a user profile
            showUserPopup(username);
        });

        layoutMessages.addView(messageView);
        scrollViewMessages.post(() -> scrollViewMessages.fullScroll(ScrollView.FOCUS_DOWN));
    }

    private void showUserPopup(String username) {
        // Create and display a popup with user information, or perform any other action
        Toast.makeText(this, "Clicked on user: " + username, Toast.LENGTH_SHORT).show();

        // Here you could launch a dialog or a bottom sheet dialog to show user details
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(username);
        builder.setMessage("More info about " + username);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
        // Implement your logic here
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

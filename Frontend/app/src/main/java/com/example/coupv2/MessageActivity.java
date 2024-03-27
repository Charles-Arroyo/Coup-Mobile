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

import androidx.appcompat.app.AppCompatActivity;

import com.example.coupv2.utils.Const;

import org.java_websocket.handshake.ServerHandshake;

import java.util.ArrayList;


public class MessageActivity extends AppCompatActivity implements WebSocketListener {

    private LinearLayout layoutMessages;
    private EditText msg;
    private String user;
    private ScrollView scrollViewMessages;
    private String BASE_URL = "ws://coms-309-023.class.las.iastate.edu:8080/chat/";
    //    private String BASE_URL = "ws://10.0.2.2:8080/chat/";
    private ArrayList<String> messagesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glb_msg);

        user = Const.getCurrentEmail();
        msg = findViewById(R.id.msg);
        scrollViewMessages = findViewById(R.id.scrollViewMessages);
        layoutMessages = findViewById(R.id.layoutMessages);
        Button sendBtn = findViewById(R.id.send_btn);
        ImageButton backButton = findViewById(R.id.back_btn);

        String serverUrl = BASE_URL + user;
        WebSocketManager.getInstance().connectWebSocket(serverUrl);
        WebSocketManager.getInstance().setWebSocketListener(MessageActivity.this);

        sendBtn.setOnClickListener(v -> {
            String messageToSend = msg.getText().toString().trim();
            if (!messageToSend.isEmpty()) {
                WebSocketManager.getInstance().sendMessage(messageToSend);
                msg.setText("");
            }
        });

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(MessageActivity.this, MenuActivity.class);
            startActivity(intent);
            WebSocketManager.getInstance().disconnectWebSocket();
        });
    }


    @Override
    public void onWebSocketMessage(String fullMessage) {
        runOnUiThread(() -> {
            // Assuming the format is "username: message"
            int colonIndex = fullMessage.indexOf(":");
            if (colonIndex != -1) {
                String username = fullMessage.substring(0, colonIndex).trim();
                String message = fullMessage.substring(colonIndex + 1).trim();

                addMessageToLayout(username, message);
            }
        });
    }

    private void addMessageToLayout(String username, String message) {
        View messageView = getLayoutInflater().inflate(R.layout.message_item, layoutMessages, false);

        TextView textView = messageView.findViewById(R.id.tvMessage);
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
        Toast.makeText(this, "Clicked on user: " + username, Toast.LENGTH_SHORT).show();
        // You can extend this to open a user profile page or show more details
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

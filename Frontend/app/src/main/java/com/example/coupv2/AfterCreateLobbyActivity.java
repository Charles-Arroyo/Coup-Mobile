package com.example.coupv2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coupv2.utils.Const;

import org.java_websocket.handshake.ServerHandshake;
public class AfterCreateLobbyActivity extends AppCompatActivity implements WebSocketListener{
    private LinearLayout layoutMessages;
    private ScrollView scrollViewMessages;
    private ImageButton lobbyBackOut;
    private boolean isLobbyFull = false;
    private boolean spectatorMode = false;
//    private String BASE_URL = "ws://coms-309-023.class.las.iastate.edu:8443/lobby/";
    private String BASE_URL = "ws://coms-309-023.class.las.iastate.edu:8443/lobby/";
//    private static final String BASE_URL2 = "http://coms-309-023.class.las.iastate.edu:8443/lobby/0/";
    private static final String BASE_URL2 = "http://coms-309-023.class.las.iastate.edu:8443/lobby/0/";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aftercreatelobby);
        lobbyBackOut = findViewById(R.id.imageView18);
        boolean createLobby = getIntent().getBooleanExtra("createLobby", false);
        String lobbyNumber = getIntent().getStringExtra("lobbyNumber");

        //if user created lobby
        if (createLobby){
            String serverUrl = BASE_URL2 + Const.getCurrentEmail();
            WebSocketManager.getInstance().connectWebSocket(serverUrl);
            WebSocketManager.getInstance().setWebSocketListener(AfterCreateLobbyActivity.this);
        }
        else {
            String serverUrl = BASE_URL + lobbyNumber + '/' +Const.getCurrentEmail();
            WebSocketManager.getInstance().connectWebSocket(serverUrl);
            WebSocketManager.getInstance().setWebSocketListener(AfterCreateLobbyActivity.this);
        }
        //if user joined lobby
        scrollViewMessages = findViewById(R.id.scrollViewMessages);
        layoutMessages = findViewById(R.id.layoutMessages);
        lobbyBackOut.setOnClickListener(v -> {
            Intent intent = new Intent(AfterCreateLobbyActivity.this, LobbyActivity.class);
            WebSocketManager.getInstance().disconnectWebSocket();
            startActivity(intent);
        });
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
        Log.d("WebSocket", "AfterCreateLobby Activity connected: ");
    }

    @Override
    public void onWebSocketMessage(String fullMessage) {
        Log.d("WebSocket", "AfterCreateLobby Activity received: " + fullMessage);
        runOnUiThread(() -> {
            Log.d("WebSocketMessage", fullMessage);
            if ("Lobby is full".equals(fullMessage)) {
                Log.d("WebSocketMessage", "ffsffs");
                 isLobbyFull = true;
                goToNewActivity(); // Call method to transition to the new activity
            }
            else if ("spec".equals(fullMessage)) {
                Log.d("WebSocketMessage", "ffsffs");
                spectatorMode = true;
                goToNewActivity(); // Call method to transition to the new activity
            }
            else{
                int i = fullMessage.indexOf(":");
                if (i != -1) {
                    String username = fullMessage.substring(0, i).trim();
                    String message = fullMessage.substring(i + 1).trim();

                        // Log the username and message using Log.d (debug level)
                        Log.d("WebSocketMessage", "User: " + username + " Message: " + message);

                        addMessageToLayout(username, message);
                } else {
                    // Log a warning if the message format is not as expected
                    Log.w("WebSocketMessage", "Message does not contain a colon (:) separator: " + fullMessage);
                }
            }
        });
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        // Log the closure information with the player name, code, reason, and initiator.
        String initiator = remote ? "Remote" : "Local";
        Log.e("WebSocketListener", Const.getCurrentEmail() + " disconnected - Code: " + code + ", Reason: " + reason + ", Initiator: " + initiator);

        // Handle different cases based on the closure code if needed
        switch (code) {
            case 1000:
                Log.d("WebSocketListener", "Normal closure");
                break;
            case 1001:
                Log.d("WebSocketListener", "Going away");
                break;
            // Add other cases as needed based on the codes you expect to handle.
            default:
                Log.d("WebSocketListener", "Disconnected due to unknown or unhandled reason");
                break;
        }

    }

    @Override
    public void onWebSocketError(Exception ex) {

    }
//    go to PlayActivity if lobby is full
    private void goToNewActivity() {
        //if lobby is full then go to PlayActivty
        if(isLobbyFull){
            WebSocketManager.getInstance().removeWebSocketListener();
            Intent intent = new Intent(AfterCreateLobbyActivity.this, PlayActivity.class); // Replace NewActivity.class with your target activity class
            startActivity(intent);
        }
        else if(spectatorMode){
            WebSocketManager.getInstance().removeWebSocketListener();
            Intent intent = new Intent(AfterCreateLobbyActivity.this, SpectatorActivity.class); // Replace NewActivity.class with your target activity class
            startActivity(intent);
        }
    }
    // Method to add ImageView to a LinearLayout
    private void addMessageToLayout(String username, String message) {
        View messageView = getLayoutInflater().inflate(R.layout.friends_msg_item2, layoutMessages, false);

        TextView textView = messageView.findViewById(R.id.placement);
        TextView usernameButton = messageView.findViewById(R.id.btnUsername);

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
}

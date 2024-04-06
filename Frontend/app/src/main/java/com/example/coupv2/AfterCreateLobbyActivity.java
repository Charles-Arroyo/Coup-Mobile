package com.example.coupv2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.java_websocket.handshake.ServerHandshake;
import org.w3c.dom.Text;

import com.example.coupv2.utils.Const;
public class AfterCreateLobbyActivity extends AppCompatActivity implements WebSocketListener{
    private LinearLayout layoutMessages;
    private ScrollView scrollViewMessages;
    private boolean isLobbyFull = false;
//    private TextView msgTv;     old code
    @Override
    protected void onResume() {
        super.onResume();
        // Assuming WebSocketManager is your class that manages the WebSocket connection.
        WebSocketManager.getInstance().setWebSocketListener(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Remove the listener when the activity goes into the background
        // to prevent it from receiving WebSocket events
        if (WebSocketManager.getInstance().getWebSocketListener() == this) {
            WebSocketManager.getInstance().removeWebSocketListener();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aftercreatelobby);
        scrollViewMessages = findViewById(R.id.scrollViewMessages);
        layoutMessages = findViewById(R.id.layoutMessages);
//        msgTv = (TextView) findViewById(R.id.tx1); old code
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {

    }
//old code
//    @Override
//    public void onWebSocketMessage(String message) {
//        Log.d("WebSocket", "Create Lobby received: " + message);
//        runOnUiThread(() -> {
////            get whatever in in TextView at moment
//            String s = msgTv.getText().toString();
//            //add message from backend to whatever is in TextView
//            msgTv.setText(s + "\n" + message);
//            // Check if the received message from backend is "lobby is full"
//            if ("Lobby is full".equals(message.trim())) {
//                isLobbyFull = true;
//                goToNewActivity(); // Call method to transition to the new activity
//            }
//        });
//    }


    @Override
    public void onWebSocketMessage(String fullMessage) {
        runOnUiThread(() -> {
            int i = fullMessage.indexOf(":");
            if (i != -1) {
                String username = fullMessage.substring(0, i).trim();
                String message = fullMessage.substring(i + 1).trim();

                addMessageToLayout(username, message);
            }
        });
    }
    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {

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
    }
    // Method to add ImageView to a LinearLayout
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
}

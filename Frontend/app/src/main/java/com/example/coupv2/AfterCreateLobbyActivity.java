package com.example.coupv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.java_websocket.handshake.ServerHandshake;
import org.w3c.dom.Text;

import com.example.coupv2.utils.Const;
public class AfterCreateLobbyActivity extends AppCompatActivity implements WebSocketListener{
    private boolean isLobbyFull = false;
    private TextView msgTv;
    @Override
    protected void onResume() {
        super.onResume();
        // Assuming WebSocketManager is your class that manages the WebSocket connection.
        WebSocketManager.getInstance().setWebSocketListener(this);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aftercreatelobby);
        msgTv = (TextView) findViewById(R.id.tx1);
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {

    }

    @Override
    public void onWebSocketMessage(String message) {
        Log.d("WebSocket", "Message received: " + message);
        runOnUiThread(() -> {
            //get whatever in in TextView at moment
            String s = msgTv.getText().toString();
            //add message from backend to whatever is in TextView
            msgTv.setText(s + "\n" + message);

            // Check if the received message from backend is "lobby is full"
            if ("lobby is full".equals(message.trim())) {
                isLobbyFull = true;
                goToNewActivity(); // Call method to transition to the new activity
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
            Intent intent = new Intent(AfterCreateLobbyActivity.this, PlayActivity.class); // Replace NewActivity.class with your target activity class
            startActivity(intent);
        }
    }
}

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
public class GameChatActivity extends AppCompatActivity implements WebSocketListener{
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
        setContentView(R.layout.activity_gamechat);
        msgTv = (TextView) findViewById(R.id.tx1);
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {

    }

    @Override
    public void onWebSocketMessage(String message) {
        Log.d("WebSocket", "Message received: " + message);
        runOnUiThread(() -> {
            String s = msgTv.getText().toString();
            msgTv.setText(s + "\n" + message);
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
        if(isLobbyFull){
            Intent intent = new Intent(GameChatActivity.this, PlayActivity.class); // Replace NewActivity.class with your target activity class
            startActivity(intent);
        }
    }
}
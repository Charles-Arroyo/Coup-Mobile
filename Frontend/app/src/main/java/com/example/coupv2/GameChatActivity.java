package com.example.coupv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.java_websocket.handshake.ServerHandshake;
import org.w3c.dom.Text;

import com.example.coupv2.utils.Const;
public class GameChatActivity extends AppCompatActivity{
    @Override
    protected void onResume() {
        super.onResume();
        // Assuming WebSocketManager is your class that manages the WebSocket connection.
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamechat);
        ImageButton backOut = findViewById(R.id.backBtn);
    }


}
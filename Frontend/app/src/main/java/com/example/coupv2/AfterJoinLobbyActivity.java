package com.example.coupv2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import org.java_websocket.handshake.ServerHandshake;
public class AfterJoinLobbyActivity extends AppCompatActivity implements WebSocketListener{
//    private String BASE_URL = "ws://coms-309-023.class.las.iastate.edu:8443/lobby/";
//    private String BASE_URL = "ws://coms-309-023.class.las.iastate.edu:8443/lobby/";
//    private static final String BASE_URL2 = "http://coms-309-023.class.las.iastate.edu:8443/lobby/0/";
//    private String BASE_URL = "ws://coms-309-023.class.las.iastate.edu:8443/lobby/";
private String BASE_URL = "ws://coms-309-023.class.las.iastate.edu:8443/lobby/";
    private EditText lobbyNumber;
    private ImageButton joinBtn;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afterjoinlobbycharles);
        lobbyNumber = findViewById(R.id.lobby_input);
        joinBtn = findViewById(R.id.join_btn);


        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lobbyNum = lobbyNumber.getText().toString();
                Intent intent = new Intent(AfterJoinLobbyActivity.this, AfterCreateLobbyActivity.class);
                intent.putExtra("lobbyNumber", lobbyNum);
                startActivity(intent);
            }
        });



    }
    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {

    }

    @Override
    public void onWebSocketMessage(String message) {
        Log.d("WebSocket", "AfterJoinLobby Activity received: " + message);
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {

    }

    @Override
    public void onWebSocketError(Exception ex) {

    }
}

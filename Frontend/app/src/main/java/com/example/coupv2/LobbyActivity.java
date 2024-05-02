package com.example.coupv2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import org.java_websocket.handshake.ServerHandshake;

public class LobbyActivity extends AppCompatActivity implements WebSocketListener{

//    private String BASE_URL = "ws://coms-309-023.class.las.iastate.edu:8443/lobby/0/";
private String BASE_URL = "ws://coms-309-023.class.las.iastate.edu:8443/lobby/0/";
//        private String BASE_URL = "ws://localhost:8443/chat/";
    private ImageButton connectBtn, joinBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobbycharles);

        /* initialize UI elements */
        connectBtn = (ImageButton) findViewById(R.id.button1);
        joinBtn = (ImageButton) findViewById(R.id.button2);


        connectBtn.setOnClickListener(new View.OnClickListener() {
            //
            @Override
            public void onClick(View v) {
//                String serverUrl = BASE_URL + Const.getCurrentEmail();
//                // Establish WebSocket connection and set listener
//                WebSocketManager.getInstance().connectWebSocket(serverUrl);
//                WebSocketManager.getInstance().setWebSocketListener(LobbyActivity.this);

                Intent intent = new Intent(LobbyActivity.this, AfterCreateLobbyActivity.class);
                intent.putExtra("createLobby", true);
                startActivity(intent);
            }
        });
        joinBtn.setOnClickListener(new View.OnClickListener() {
            //
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LobbyActivity.this, AfterJoinLobbyActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onWebSocketMessage(String message) {
        /**
         * In Android, all UI-related operations must be performed on the main UI thread
         * to ensure smooth and responsive user interfaces. The 'runOnUiThread' method
         * is used to post a runnable to the UI thread's message queue, allowing UI updates
         * to occur safely from a background or non-UI thread.
         */
        Log.d("WebSocket", "Lobby Activity received: " + message);
//        runOnUiThread(() -> {
//            String s = msgTv.getText().toString();
//            msgTv.setText(s + "\n"+message);
//        });
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
//        String closedBy = remote ? "server" : "local";
//        runOnUiThread(() -> {
//            String s = msgTv.getText().toString();
//            msgTv.setText(s + "---\nconnection closed by " + closedBy + "\nreason: " + reason);
//        });
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {}

    @Override
    public void onWebSocketError(Exception ex) {}


}
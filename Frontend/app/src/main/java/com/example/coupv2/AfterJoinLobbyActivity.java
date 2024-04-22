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
public class AfterJoinLobbyActivity extends AppCompatActivity implements WebSocketListener{
//    private String BASE_URL = "ws://coms-309-023.class.las.iastate.edu:8080/lobby/";
private String BASE_URL = "ws://coms-309-023.class.las.iastate.edu:8443/lobby/";
    private EditText lobbyNumber;
    private Button joinBtn;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afterjoinlobby);
        lobbyNumber = findViewById(R.id.lobby_input);
        joinBtn = findViewById(R.id.join_btn);


        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lobbyNum = lobbyNumber.getText().toString();
                String serverUrl = BASE_URL + lobbyNum + '/' +Const.getCurrentEmail();
                // Establish WebSocket connection and set listener
                WebSocketManager.getInstance().connectWebSocket(serverUrl);
                WebSocketManager.getInstance().setWebSocketListener(AfterJoinLobbyActivity.this);
                Intent intent = new Intent(AfterJoinLobbyActivity.this, AfterCreateLobbyActivity.class);
                startActivity(intent);
            }
        });



    }
    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {

    }

    @Override
    public void onWebSocketMessage(String message) {

    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {

    }

    @Override
    public void onWebSocketError(Exception ex) {

    }
}

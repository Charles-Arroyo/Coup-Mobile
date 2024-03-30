package com.example.coupv2;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import org.java_websocket.handshake.ServerHandshake;

import androidx.appcompat.app.AppCompatActivity;

public class PlayActivity extends AppCompatActivity implements WebSocketListener{
    //store current cards here as well
    private String card1;
    private String card2;
    private int coins;
    @Override
    //keep websocket open from LobbyActivity
    protected void onResume() {
        super.onResume();
        //set listener to this class
        WebSocketManager.getInstance().setWebSocketListener(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play); // link to Play activity XML
        //variables
        ImageView gameBoard = findViewById(R.id.gameBoard);
        ImageButton openChat = findViewById(R.id.imageButton2);

        gameBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                go to new activity when clicked on
                Intent intent = new Intent(PlayActivity.this, ActionActivity.class);
                startActivity(intent);
            }
        });

        openChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                go to new activity when clicked on
                Intent intent = new Intent(PlayActivity.this, GameChatActivity.class);
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

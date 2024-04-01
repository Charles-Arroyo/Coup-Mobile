package com.example.coupv2;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import org.java_websocket.handshake.ServerHandshake;

public class ActionActivity extends AppCompatActivity implements WebSocketListener {
    //        need coins here atleast
    //keep websocket open from LobbyActivity
    @Override
    protected void onResume() {
        super.onResume();
        //set listener to this class
        WebSocketManager.getInstance().setWebSocketListener(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action); // link to Play activity XML
        ImageView whiteIcon1 = findViewById(R.id.whiteshape1);
        ImageView whiteIcon2 =  findViewById(R.id.whiteshape2);
        ImageView whiteIcon3 = findViewById(R.id.whiteshape3);
        ImageView whiteIcon4 = findViewById(R.id.whiteshape4);
        ImageView whiteIcon5 = findViewById(R.id.whiteshape5);
        ImageView whiteIcon6 = findViewById(R.id.whiteshape6);
        ImageView whiteIcon7 = findViewById(R.id.whiteshape7);
        ImageView whiteIcon8 = findViewById(R.id.whiteshape8);
        ImageView whiteIcon9 = findViewById(R.id.whiteshape9);
        Button backBtn = findViewById(R.id.back_button);

        whiteIcon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                go to new activity when clicked on
                Intent intent = new Intent(ActionActivity.this, PlayActivity.class);
                startActivity(intent);
            }
        });
        whiteIcon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                go to new activity when clicked on
                Intent intent = new Intent(ActionActivity.this, PlayActivity.class);
                startActivity(intent);
            }
        });
        whiteIcon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                go to new activity when clicked on
                Intent intent = new Intent(ActionActivity.this, PlayActivity.class);
                startActivity(intent);
            }
        });
        whiteIcon4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                go to new activity when clicked on
                Intent intent = new Intent(ActionActivity.this, PlayActivity.class);
                startActivity(intent);
            }
        });
        whiteIcon5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                go to new activity when clicked on
                Intent intent = new Intent(ActionActivity.this, PlayActivity.class);
                startActivity(intent);
            }
        });
        whiteIcon6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                go to new activity when clicked on
                Intent intent = new Intent(ActionActivity.this, PlayActivity.class);
                startActivity(intent);
            }
        });
        whiteIcon7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                go to new activity when clicked on
                Intent intent = new Intent(ActionActivity.this, PlayActivity.class);
                startActivity(intent);
            }
        });
        whiteIcon8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                go to new activity when clicked on
                Intent intent = new Intent(ActionActivity.this, PlayActivity.class);
                startActivity(intent);
            }
        });
        whiteIcon9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                go to new activity when clicked on
                Intent intent = new Intent(ActionActivity.this, PlayActivity.class);
                startActivity(intent);
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                go to new activity when clicked on
                Intent intent = new Intent(ActionActivity.this, PlayActivity.class);
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

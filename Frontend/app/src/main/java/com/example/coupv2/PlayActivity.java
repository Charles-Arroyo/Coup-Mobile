package com.example.coupv2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;
import androidx.appcompat.app.AppCompatActivity;

public class PlayActivity extends AppCompatActivity implements WebSocketListener{
    //store current cards here as well
     String card1;
     String card2;
     int coins;
//    these three variables are for displaying ui
//    boolean isWaiting;
     boolean isTurn;
     boolean isContesting;
    private ImageView gameBoard;
    private ImageButton openChat;
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
        setContentView(R.layout.activity_play); // link tupdo Play activity XML
        //variables
        gameBoard = findViewById(R.id.gameBoard);
        openChat = findViewById(R.id.imageButton2);
        //set gameBoard to be visible by default
        gameBoard.setOnClickListener(gameBoardClickListener);
        openChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                go to new activity when clicked on
                Intent intent = new Intent(PlayActivity.this, GameChatActivity.class);
                startActivity(intent);
            }
        });
        Button testButton = findViewById(R.id.btn123);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePlayerUi();
            }
        });

    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {

    }

    @Override
    public void onWebSocketMessage(String message) {
        Log.d("WebSocket", "Message received: " + message);
        runOnUiThread(() -> {
            try {
                Log.d("step1", "world");
                // Convert message string to JSONObject
                JSONObject jsonObject = new JSONObject(message);
                // Get the player JSONObject
                Log.d("step2", "world");
                JSONObject player = jsonObject.getJSONObject("player");
                // Extract values from the player object
                Log.d("step3", "world");
                card1 = player.getString("cardOne");
                Log.d("step4", "world");
                card2 = player.getString("cardTwo");
                Log.d("step5", "world");
                coins = player.getInt("coins");
                Log.d("step6", "world");
                isTurn = player.getBoolean("turn"); // corrected from 'isTurn' to 'turn'
                // Update the UI
                Log.d("step7", "world");
                updatePlayerUi();
            } catch (JSONException e) {
                Log.e("WebSocket", "Error parsing JSON message", e);
            }
        });
    }



    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {

    }
    //check if player is waiting
    public void updatePlayerUi(){
        Log.d("hello", "world");
        //get image view for waiting
        ImageView waitingOverlay = findViewById(R.id.gameBoard_wait);
        //if not player turn then display waiting
        if (!isTurn){
            waitingOverlay.setVisibility(View.VISIBLE);
            //disable gameBoard listener if not turn
            gameBoard.setOnClickListener(null);
            Log.d("updatePlayerUi", "Not player's turn. Showing waiting overlay.");

        }
        //if player turn
        else if (isTurn){
            //do not show waiting screen
            Log.d("updatePlayerUi", "Player's turn. Hiding waiting overlay.");
            gameBoard.setOnClickListener(gameBoardClickListener);
            waitingOverlay.setVisibility(View.GONE);
        }
        //implement later
//        else if(isContesting){
//
//        }
    }

    @Override
    public void onWebSocketError(Exception ex) {

    }
    // A variable to store the game board click listener
    private View.OnClickListener gameBoardClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Handle game board click
            Intent intent = new Intent(PlayActivity.this, ActionActivity.class);
            startActivity(intent);
            // Let backend know turn is over
            WebSocketManager.getInstance().sendMessage("Income");
        }
    };
}

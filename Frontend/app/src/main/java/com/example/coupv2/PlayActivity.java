package com.example.coupv2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;
import androidx.appcompat.app.AppCompatActivity;

import utils.Const;

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
        JSONObject jsonObject = new JSONObject();
        //let backend know that i am ready to receive
        try {
            jsonObject.put("playerEmail", Const.getCurrentEmail());
            jsonObject.put("readyToListen", true);
            String jsonStr = jsonObject.toString();
            WebSocketManager.getInstance().sendMessage(jsonStr);
            Log.d("WebSocket", "it worked");
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                ImageView playerIcon = findViewById(R.id.person3); // Replace with your actual ImageView ID.
                // Apply the pulse animation.
                Animation pulse = AnimationUtils.loadAnimation(PlayActivity.this, R.anim.pulse_animation);
                playerIcon.startAnimation(pulse);
            }
        });

    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {

    }

    @Override
    public void onWebSocketMessage(String message) {
        Log.d("WebSocket", "Play Activity received: " + message);
        try {
            // Try to parse the message into a JSONObject
            JSONObject jsonObject = new JSONObject(message);
            // Since we now know the JSONObject has been created successfully,
            // you can proceed with other operations, such as extracting data.
            JSONObject player = jsonObject.getJSONObject("player");
            card1 = player.getString("cardOne");
            card2 = player.getString("cardTwo");
            coins = player.getInt("coins");
            isTurn = player.getBoolean("turn");
            // Now call updatePlayerUi() on the UI thread
            runOnUiThread(this::updatePlayerStateUi);
            Log.d("WebSocket", "you got json object");
            Log.d("WebSocket", card1);
            Log.d("WebSocket", card2);
            Log.d("WebSocket", String.valueOf(coins));
            Log.d("WebSocket", String.valueOf(isTurn));
        } catch (JSONException e) {
            // If an exception is thrown, log the error and the message that caused it
            Log.e("WebSocket", "Error parsing JSON message: " + message, e);
        }
    }




    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {

    }
    //check if player is waiting
    public void updatePlayerStateUi(){
        //get image view for waiting
        ImageView waitingOverlay = findViewById(R.id.gameBoard_wait);
        //if not player turn then display waiting
        if (!isTurn){
            waitingOverlay.setVisibility(View.VISIBLE);
            //disable gameBoard listener if not turn
            gameBoard.setOnClickListener(null);
        }
        //if player turn
        else if (isTurn){
            //do not show waiting screen
            gameBoard.setOnClickListener(gameBoardClickListener);
            waitingOverlay.setVisibility(View.GONE);
        }
        //implement later
//        else if(isContesting){
//
//        }
    }
    public void updatePlayerCoinsUi(int coins1, int coins2, int coins3, int coins4){
        // Find the TextViews by ID
        TextView oval1Text = findViewById(R.id.oval1Text);
        TextView oval2Text = findViewById(R.id.oval2Text);
        TextView oval3Text = findViewById(R.id.oval3Text);
        TextView oval4Text = findViewById(R.id.oval4Text);

        // Update the text to show the number of coins
        oval1Text.setText(String.valueOf(coins1));
        oval2Text.setText(String.valueOf(coins2));
        oval3Text.setText(String.valueOf(coins3));
        oval4Text.setText(String.valueOf(coins4));

    }
    //call method 4 times for each player
    public void updatePlayer1LivesUi(int lives){
        ImageView cardIcon1 = findViewById(R.id.greenIcon1);
        ImageView cardIcon2 = findViewById(R.id.greenIcon2);
        ImageView cardIcon3 = findViewById(R.id.greenIcon3);
        if (lives ==  2){
            cardIcon1.setVisibility(View.VISIBLE);
            cardIcon2.setVisibility(View.VISIBLE);
            cardIcon3.setVisibility(View.GONE);
        }
        //one life
        else if (lives ==  1){
            cardIcon1.setVisibility(View.GONE);
            cardIcon2.setVisibility(View.GONE);
            cardIcon3.setVisibility(View.VISIBLE);
        }
        else if (lives ==  0){
            cardIcon1.setVisibility(View.GONE);
            cardIcon2.setVisibility(View.GONE);
            cardIcon3.setVisibility(View.GONE);
        }
    }
    public void updatePlayer2LivesUi(int lives){
        ImageView cardIcon1 = findViewById(R.id.yellowicon1);
        ImageView cardIcon2 = findViewById(R.id.yellowicon2);
        ImageView cardIcon3 = findViewById(R.id.yellowicon3);
        if (lives ==  2){
            cardIcon1.setVisibility(View.VISIBLE);
            cardIcon2.setVisibility(View.VISIBLE);
            cardIcon3.setVisibility(View.GONE);
        }
        //one life
        else if (lives ==  1){
            cardIcon1.setVisibility(View.GONE);
            cardIcon2.setVisibility(View.GONE);
            cardIcon3.setVisibility(View.VISIBLE);
        }
        else if (lives ==  0){
            cardIcon1.setVisibility(View.GONE);
            cardIcon2.setVisibility(View.GONE);
            cardIcon3.setVisibility(View.GONE);
        }
    }
    public void updatePlayer3LivesUi(int lives){
        ImageView cardIcon1 = findViewById(R.id.redIcon1);
        ImageView cardIcon2 = findViewById(R.id.redIcon2);
        ImageView cardIcon3 = findViewById(R.id.redIcon3);
        if (lives ==  2){
            cardIcon1.setVisibility(View.VISIBLE);
            cardIcon2.setVisibility(View.VISIBLE);
            cardIcon3.setVisibility(View.GONE);
        }
        //one life
        else if (lives ==  1){
            cardIcon1.setVisibility(View.GONE);
            cardIcon2.setVisibility(View.GONE);
            cardIcon3.setVisibility(View.VISIBLE);
        }
        else if (lives ==  0){
            cardIcon1.setVisibility(View.GONE);
            cardIcon2.setVisibility(View.GONE);
            cardIcon3.setVisibility(View.GONE);
        }
    }
    public void updatePlayer4LivesUi(int lives){
        ImageView cardIcon1 = findViewById(R.id.blueIcon1);
        ImageView cardIcon2 = findViewById(R.id.blueIcon2);
        ImageView cardIcon3 = findViewById(R.id.blueIcon3);
        if (lives ==  2){
            cardIcon1.setVisibility(View.VISIBLE);
            cardIcon2.setVisibility(View.VISIBLE);
            cardIcon3.setVisibility(View.GONE);
        }
        //one life
        else if (lives ==  1){
            cardIcon1.setVisibility(View.GONE);
            cardIcon2.setVisibility(View.GONE);
            cardIcon3.setVisibility(View.VISIBLE);
        }
        else if (lives ==  0){
            cardIcon1.setVisibility(View.GONE);
            cardIcon2.setVisibility(View.GONE);
            cardIcon3.setVisibility(View.GONE);
        }
    }
    public void updatePlayerTurnUi(String Player){
        ImageView playerIcon1 = findViewById(R.id.person1);
        ImageView playerIcon2 = findViewById(R.id.person2);
        ImageView playerIcon3 = findViewById(R.id.person3);
        ImageView playerIcon4 = findViewById(R.id.person4);
        // Apply the pulse animation.
        Animation pulse = AnimationUtils.loadAnimation(PlayActivity.this, R.anim.pulse_animation);
        //if this screen is current player
        if (Player.equals("Player 1")){
            playerIcon1.startAnimation(pulse);
        }
        else if(Player.equals("Player 2")){
            playerIcon2.startAnimation(pulse);
        }
        else if(Player.equals("Player 3")){
            playerIcon3.startAnimation(pulse);
        }
        else if(Player.equals("Player 4")){
            playerIcon4.startAnimation(pulse);
        }
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

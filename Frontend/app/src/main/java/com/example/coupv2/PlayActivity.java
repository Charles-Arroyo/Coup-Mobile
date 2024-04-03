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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coupv2.utils.Const;

public class PlayActivity extends AppCompatActivity implements WebSocketListener{
    //track who the current player is
    String whoTurnIsIt;
    //cards of currentPlayer
    String card1;
    String card2;
    //lives and coins of current players
    int currentLives;
    int currentCoins;
     int currentTurnNumber;
    //coins
     int coins2;
     int coins3;
     int coins4;
//    these three variables are for displaying ui
//    boolean isWaiting;
     boolean playerState;
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
        try {
            jsonObject.put("playerEmail", Const.getCurrentEmail());
            jsonObject.put("move", "ready");
            jsonObject.put("targetPlayer", "null");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String jsonStr = jsonObject.toString();
        WebSocketManager.getInstance().sendMessage(jsonStr);
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
            //parse the message into a JSONObject
            JSONObject jsonObject = new JSONObject(message);
            //parse game object
            JSONObject game = jsonObject.getJSONObject("Game");
            // Access the current player object
            JSONObject currentPlayer = game.getJSONObject("currentPlayer");

//            currentCoins = currentPlayer.getInt("coins");      not using this
//            currentLives = currentPlayer.getInt("lives");      not using this
//            currentTurnNumber = currentPlayer.getInt("turnNumber");  not using this

            //get current player state, and cards(this is visible to current screen)
            playerState = currentPlayer.getBoolean("turn");
            card1 = currentPlayer.getString("cardOne");
            card2 = currentPlayer.getString("cardTwo");

            // Read the player array
            JSONArray playerArray = game.getJSONArray("playerArrayList");
            //track current coins and lives for index in array
            int thisGuyCoins;
            int thisGuyLives;
            //update lives, coins, and current turn for ui(this is same across all screens)
            for (int i = 0; i < playerArray.length(); i++) {
//                if (i != currentTurnNumber - 1){ not using this
                    //grab whole player information
                    JSONObject player = playerArray.getJSONObject(i);
                    //update coins for current player
                    thisGuyCoins = player.getInt("coins");
                    updatePlayerCoinsUi(thisGuyCoins, i + 1);
                    //update player lives
                    thisGuyLives = player.getInt("lives");
                    //player 1 is green, 2 is yellow, 3 is red, 4 is blue
                    if (i == 0){
                        updatePlayer1LivesUi(thisGuyLives);
                    }
                    else if (i == 1){
                        updatePlayer2LivesUi(thisGuyLives);
                    }
                    else if (i == 2){
                        updatePlayer3LivesUi(thisGuyLives);
                    }
                    else if (i == 3){
                        updatePlayer4LivesUi(thisGuyLives);
                    }
                    //if this player turn is true then animate his character
                    if (player.getBoolean("turn")){
                        updatePlayerTurnUi(i + 1);
                    }
//                }
            }
            // Now call updatePlayerUi() on the UI thread
            runOnUiThread(this::updatePlayerStateUi);
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
        if (!playerState){
            waitingOverlay.setVisibility(View.VISIBLE);
            //disable gameBoard listener if not turn
            //check if this works
            gameBoard.setOnClickListener(null);
        }
        //if player turn
        else if (playerState){
            //do not show waiting screen
            gameBoard.setOnClickListener(gameBoardClickListener);
            waitingOverlay.setVisibility(View.GONE);
        }
        //implement later
//        else if(isContesting){
//
//        }
    }
    //take in turn number and total coins
    public void updatePlayerCoinsUi(int totalCoins, int numOfTurn){
        // Find the TextViews by ID
        TextView oval1Text = findViewById(R.id.oval1Text);
        TextView oval2Text = findViewById(R.id.oval2Text);
        TextView oval3Text = findViewById(R.id.oval3Text);
        TextView oval4Text = findViewById(R.id.oval4Text);
        if (numOfTurn == 1){
            oval1Text.setText(String.valueOf(totalCoins));
        }
        else if (numOfTurn == 2){
            oval2Text.setText(String.valueOf(totalCoins));
        }
        else if (numOfTurn == 3){
            oval3Text.setText(String.valueOf(totalCoins));
        }
        else if (numOfTurn == 4){
            oval4Text.setText(String.valueOf(totalCoins));
        }

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
    public void updatePlayerTurnUi(int turnNum){
        ImageView playerIcon1 = findViewById(R.id.person1);
        ImageView playerIcon2 = findViewById(R.id.person2);
        ImageView playerIcon3 = findViewById(R.id.person3);
        ImageView playerIcon4 = findViewById(R.id.person4);
        // Apply the pulse animation.
        Animation pulse = AnimationUtils.loadAnimation(PlayActivity.this, R.anim.pulse_animation);
        //if this screen is current player
        if (turnNum == 1){
            playerIcon1.startAnimation(pulse);
        }
        else if(turnNum == 2){
            playerIcon2.startAnimation(pulse);
        }
        else if(turnNum == 3){
            playerIcon3.startAnimation(pulse);
        }
        else if(turnNum == 4){
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
        }
    };
}

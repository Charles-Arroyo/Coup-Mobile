package com.example.coupv2;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.DialogInterface;
// ... other imports

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coupv2.utils.Const;

import java.util.List;
import java.util.Objects;

public class PlayActivity extends AppCompatActivity implements WebSocketListener{

    //button for exchange
    ImageButton exchangeBtn;
    //player text
    TextView currentPlayerText;
    private int checkedCount = 0;
    //chat layout views
    private LinearLayout layoutMessages1;
    private ScrollView scrollViewMessages;
    //view for text icon down screen
    ImageView textBox;
    //view for when player is waiting
    ImageView waitingOverlay;
    //view for when player is dead
    ImageView deadOverLay;
    //views for when player turn
    private ImageView gameBoard;
    //views for when in contest mode
    ImageView smallwhite1;
    ImageView smallwhite2;
    ImageView longwhite;
    ImageView bigBlock;
    //player variables
    String card1;
    String card2;
    String exCard1;   //used for exchange
    String exCard2;   //used for exchange
    //store current user coins
    int totalCoins = 0;
    String playerState;
    //last move (this is used for case of blocking stealing)
    String lastMoveMade;
    //order of game for current player
    String Player1;
    String Player2;
    String Player3;
    String Player4;
    String Player1Name;
    String Player2Name;
    String Player3Name;
    String Player4Name;
    //screen variables
    ImageView greenCard1;
    ImageView greenCard2;
    ImageView greenCard3;
    ImageView redCard1;
    ImageView redCard2;
    ImageView redCard3;
    ImageView yellowCard1;
    ImageView yellowCard2;
    ImageView yellowCard3;
    ImageView blueCard1;
    ImageView blueCard2;
    ImageView blueCard3;
    TextView numCoins1;
    TextView numCoins2;
    TextView numCoins3;
    TextView numCoins4;
    //view for player icons
    ImageView playerIcon1;
    ImageView playerIcon2;
    ImageView playerIcon3;
    ImageView playerIcon4;

    @Override
    protected void onResume() {
        super.onResume();
        //load messages
        loadMessages();
        //set listener to this class
        WebSocketManager.getInstance().setWebSocketListener(this);
        // Get the intent that started this activity(this is used from action activity)
        Intent intent = getIntent();
        if (intent.hasExtra("playerEmail") && intent.hasExtra("move") && intent.hasExtra("targetPlayer")) {
            // Retrieve the data from the intent
            String pE = intent.getStringExtra("playerEmail");
            String mov = intent.getStringExtra("move");
            String tp = intent.getStringExtra("targetPlayer");
            try {
                // Create a new JSONObject
                JSONObject jsonObject = new JSONObject();
                // Put each piece of data into the JSONObject with a key
                jsonObject.put("playerEmail", pE);
                jsonObject.put("move", mov);
                jsonObject.put("targetPlayer", tp);
                jsonObject.put("card1", "null");
                jsonObject.put("card2", "null");
                // Use jsonObject.toString() to get JSON string representation
                String jsonString = jsonObject.toString();
                WebSocketManager.getInstance().sendMessage(jsonString);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set listener to this class
        WebSocketManager.getInstance().setWebSocketListener(this);
        JSONObject jsonObject = new JSONObject();
        //let backend know that player is ready to receive data
        try {
            jsonObject.put("playerEmail", Const.getCurrentEmail());
            jsonObject.put("move", "ready");
            jsonObject.put("targetPlayer", "null");
            jsonObject.put("card1", "null");
            jsonObject.put("card2", "null");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String jsonStr = jsonObject.toString();
        WebSocketManager.getInstance().sendMessage(jsonStr);

        // link Play activity XML
        setContentView(R.layout.activity_play);
        //exchange buttons and setup
        exchangeBtn = findViewById(R.id.exchangeView);
//         checkbox1 = findViewById(R.id.radioButton);
//         checkbox2 = findViewById(R.id.radioButton1);
//         checkbox3 = findViewById(R.id.radioButton2);
//         checkbox4 = findViewById(R.id.radioButton3);
//         submitButton = findViewById(R.id.button5);
//         submitButton.setOnClickListener(submitExchange);
        //playericons
        playerIcon1 = findViewById(R.id.person1);
         playerIcon2 = findViewById(R.id.person2);
         playerIcon3 = findViewById(R.id.person3);
         playerIcon4 = findViewById(R.id.person4);
         //current player
        currentPlayerText = findViewById(R.id.timerText);


        //exchange popup
        exchangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog2();
            }
        });
        // Set a click listeners for player icons
        playerIcon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the username of the player
                String playerUsername = Player1Name; // Replace with actual logic to retrieve the username

                // Create an AlertDialog builder
                AlertDialog.Builder builder = new AlertDialog.Builder(PlayActivity.this);

                // Set the title and message of the dialog
                builder.setTitle("Player Information");
                builder.setMessage("Username: " + playerUsername);

                // Add a button to close the dialog
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        dialog.dismiss();
                    }
                });
                // Create and show the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        playerIcon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the username of the player
                String playerUsername = Player2Name; // Replace with actual logic to retrieve the username

                // Create an AlertDialog builder
                AlertDialog.Builder builder = new AlertDialog.Builder(PlayActivity.this);

                // Set the title and message of the dialog
                builder.setTitle("Player Information");
                builder.setMessage("Username: " + playerUsername);

                // Add a button to close the dialog
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        dialog.dismiss();
                    }
                });

                // Create and show the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        playerIcon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the username of the player
                String playerUsername = Player3Name; // Replace with actual logic to retrieve the username

                // Create an AlertDialog builder
                AlertDialog.Builder builder = new AlertDialog.Builder(PlayActivity.this);

                // Set the title and message of the dialog
                builder.setTitle("Player Information");
                builder.setMessage("Username: " + playerUsername);

                // Add a button to close the dialog
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        dialog.dismiss();
                    }
                });

                // Create and show the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        playerIcon4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the username of the player
                String playerUsername = Player4Name; // Replace with actual logic to retrieve the username

                // Create an AlertDialog builder
                AlertDialog.Builder builder = new AlertDialog.Builder(PlayActivity.this);

                // Set the title and message of the dialog
                builder.setTitle("Player Information");
                builder.setMessage("Username: " + playerUsername);
                // Add a button to close the dialog
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        dialog.dismiss();
                    }
                });

                // Create and show the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        //assign player cards
        greenCard1 = findViewById(R.id.player1);
        greenCard2 = findViewById(R.id.player2);
        greenCard3 = findViewById(R.id.player3);
        greenCard1.setOnClickListener(card1ClickListener);
        greenCard2.setOnClickListener(card2ClickListener);
        greenCard3.setOnClickListener(card3ClickListener);
        //other cards
        redCard1 = findViewById(R.id.redIcon1);
        redCard2 = findViewById(R.id.redIcon2);
        redCard3 = findViewById(R.id.redIcon3);
        blueCard1 = findViewById(R.id.blueIcon1);
        blueCard2 = findViewById(R.id.blueIcon2);
        blueCard3 = findViewById(R.id.blueIcon3);
        yellowCard1 = findViewById(R.id.yellowicon1);
        yellowCard2 = findViewById(R.id.yellowicon2);
        yellowCard3 = findViewById(R.id.yellowicon3);
        //assign coins
         numCoins1 = findViewById(R.id.oval1Text);
         numCoins2 = findViewById(R.id.oval2Text);
         numCoins3 = findViewById(R.id.oval3Text);
         numCoins4 = findViewById(R.id.oval4Text);
        //chat layout views
        scrollViewMessages = findViewById(R.id.scrollViewMessages1);
        layoutMessages1 = findViewById(R.id.layoutMessages1);
        textBox =  findViewById(R.id.imageView4);
        //assign view for when player is waiting
        waitingOverlay = findViewById(R.id.gameBoard_wait);
        //assign view for when player is dead
        deadOverLay = findViewById(R.id.gameBoard_dead);
        //assign view for player turn
        gameBoard = findViewById(R.id.gameBoard);
        gameBoard.setOnClickListener(gameBoardClickListener); //set gameBoard to be visible by default
        //assign view for player is contesting
        smallwhite1 = findViewById(R.id.smallwhite1);
        smallwhite2 = findViewById(R.id.smallwhite2);
        longwhite = findViewById(R.id.longwhite);
        bigBlock = findViewById(R.id.BIGBLOCK);
        // Create an AlertDialog builder for current player
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Set the title and message of the dialog
        builder.setTitle("Player Information");
        builder.setMessage("Username: " + Const.getCurrentEmail());

        // Add a button to close the dialog
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                dialog.dismiss();
            }
        });

        loadMessages();
        //open up chat
        textBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                go to new activity when clicked on
                showDialog();
            }
        });

    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {

    }

    //how to handle data coming from backend
    @Override
    public void onWebSocketMessage(String message) {
        Log.d("WebSocket", "Play Activity received: " + message);

        //handle game data
        if (message.trim().startsWith("{")) {
            // It seems to be a JSON message, parse it
            processJsonMessage(message);
        }
        //handle chat data
        else if (message.matches(".*: .*")) {
            // It matches the pattern "Username: 'message'"
            processStringMessage(message);
        }
        else if (message.equals("over")) {
            // It matches the pattern "Username: 'message'"
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    showGameOverDialog();
                }
            });

        }
        // Unknown format
        else {
            Log.e("WebSocket", "Received message in unknown format: " + message);
        }
    }
//    how to handle chat data

    private void processStringMessage(String message) {
        int colonIndex = message.indexOf(":");
        // Extract the username and message from the string
        if (colonIndex != -1) {
            String usernameMessage = message.substring(0, colonIndex).trim();
            String messageContent = message.substring(colonIndex + 1).trim().replaceAll("^'(.*)'$", "$1");

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    sendMessage(usernameMessage, messageContent);
                }
            });
        }
    }
    //how to handle game data
    private void processJsonMessage(String message) {

        // Existing JSON processing code here...
        try {
            // Parse the message into a JSONObject
            JSONObject jsonObject = new JSONObject(message);
            // Parse game object
            JSONObject game = jsonObject.getJSONObject("Game");
            JSONObject currentPlayer = game.getJSONObject("currentPlayer");
            // Read the player array
            final JSONArray playerArray = game.getJSONArray("playerArrayList");
            lastMoveMade = currentPlayer.getString("currentMove");  // Use optString to handle null cases

            // This will run on the UI thread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //set exchange cards
                        exCard1 = currentPlayer.getString("exchangeCard1");
                        exCard2 = currentPlayer.getString("exchangeCard2");
                        //set player order before anything and check if they are still alive
                        for (int i = 0; i < playerArray.length(); i++) {
                            JSONObject player = playerArray.getJSONObject(i);
                            String playerEmail = player.getString("userEmail");
                            String playerViewString = player.getString("playerView");
                            JSONArray playerView = new JSONArray(playerViewString);

                            if (playerEmail.equals(Const.getCurrentEmail())) {
                                for (int j = 0; j < playerView.length(); j++) {
                                    String viewEmail = playerView.getString(j);
                                    if(j == 0){
                                        Player1 = viewEmail;
                                        Player1Name = viewEmail;
                                    }
                                    else if(j == 1){
                                        Player2 = viewEmail;
                                        Player2Name = viewEmail;
                                    }
                                    else if(j == 2){
                                        Player3 = viewEmail;
                                        Player3Name = viewEmail;;
                                    }
                                    else if(j == 3){
                                        Player4 = viewEmail;
                                        Player4Name = viewEmail;
                                    }
                                }

                                // Updating player state and cards for the current -player
                                playerState = player.getString("playerState");
                                card1 = player.getString("cardOne");
                                card2 = player.getString("cardTwo");
                                totalCoins = player.getInt("coins");
                            }
                        }

                        // Perform UI updates for each player
                        for (int i = 0; i < playerArray.length(); i++) {
                            //get all data from player object
                            JSONObject player = playerArray.getJSONObject(i);
                            String playerEmail = player.getString("userEmail");  //player email for current index
                            int thisGuyCoins = player.getInt("coins");
                            int thisGuyLives = player.getInt("lives");
                            //determine player order
                            String playerViewString = player.getString("playerView");
                            JSONArray playerView = new JSONArray(playerViewString);

                            // Update coins for current player
                            updatePlayerCoinsUi(thisGuyCoins, playerEmail);

                            // Update player lives based on index (player 1 is green, 2 is yellow, etc.)
                            if (playerEmail.equals(Player1)) {
                                updatePlayer1LivesUi(thisGuyLives);
                            } else if (playerEmail.equals(Player2)) {
                                updatePlayer2LivesUi(thisGuyLives);
                            } else if (playerEmail.equals(Player3)) {
                                updatePlayer3LivesUi(thisGuyLives);
                            } else if (playerEmail.equals(Player4)) {
                                updatePlayer4LivesUi(thisGuyLives);
                            }
                            if (thisGuyLives == 0){
                                //dont need to check themselves
                                if (playerEmail.equals(Player2)){
                                    Player2 = null;
                                }
                                else if (playerEmail.equals(Player3)){
                                    Player3 = null;
                                }
                                else if (playerEmail.equals(Player4)){
                                    Player4 = null;
                                }
                            }

                            // If this player's turn is true, then animate his character
                            if (player.getBoolean("turn")) {
                                currentPlayerText.setText("Current Player: " + playerEmail);
                                updatePlayerTurnUi(playerEmail);
                            }
                        }
                        updatePlayerStateUi();
//                        setupCheckboxes();

                        Log.d("GameDebug", "Player State: " + playerState);
                    } catch (JSONException e) {
                        Log.e("WebSocket", "Error parsing JSON in UI thread", e);
                    }
                }
            });
        } catch (JSONException e) {
            // If an exception is thrown, log the error and the message that caused it
            Log.e("WebSocket", "Error parsing JSON message: " + message, e);
        }
    }


    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        // Log the closure information with the player name, code, reason, and initiator.
        String initiator = remote ? "Remote" : "Local";
        Log.e("WebSocketListener", Player1 + " disconnected - Code: " + code + ", Reason: " + reason + ", Initiator: " + initiator);

        // Handle different cases based on the closure code if needed
        switch (code) {
            case 1000:
                Log.d("WebSocketListener", "Normal closure");
                break;
            case 1001:
                Log.d("WebSocketListener", "Going away");
                break;
            // Add other cases as needed based on the codes you expect to handle.
            default:
                Log.d("WebSocketListener", "Disconnected due to unknown or unhandled reason");
                break;
        }

    }

    //check if player is waiting
    public void updatePlayerStateUi(){
        //if not player turn then display waiting
        if (playerState.equals("wait")){
            exchangeBtn.setVisibility(View.GONE);

            //hide contest layout
            bigBlock.setVisibility(View.GONE);
            smallwhite1.setVisibility(View.GONE);
            smallwhite2.setVisibility(View.GONE);
            longwhite.setVisibility(View.GONE);
            //show waiting on screeN

            waitingOverlay.setVisibility(View.VISIBLE);
            //disable listeners if not turn
            gameBoard.setOnClickListener(null);
            smallwhite1.setOnClickListener(null);
            smallwhite2.setOnClickListener(null);
            longwhite.setOnClickListener(null);
        }
        //if player turn
        else if (playerState.equals("turn")){
            gameBoard.setVisibility(View.VISIBLE);

            exchangeBtn.setVisibility(View.GONE);
            //hide contest layout
            bigBlock.setVisibility(View.GONE);
            smallwhite1.setVisibility(View.GONE);
            smallwhite2.setVisibility(View.GONE);
            longwhite.setVisibility(View.GONE);
            //hide waiting layout
            waitingOverlay.setVisibility(View.GONE);
            deadOverLay.setVisibility(View.GONE);
            //set on game board listener
            gameBoard.setOnClickListener(gameBoardClickListener);
            //disable contest listeners
            smallwhite1.setOnClickListener(null);
            smallwhite2.setOnClickListener(null);
            longwhite.setOnClickListener(null);
        }
        //implement later
        else if(playerState.equals("contest")){
            //disable exchange
            exchangeBtn.setVisibility(View.GONE);
            //hide waiting and dead layout
            waitingOverlay.setVisibility(View.GONE);
            deadOverLay.setVisibility(View.GONE);
            //show contest mode layout
            bigBlock.setVisibility(View.VISIBLE);
            smallwhite1.setVisibility(View.VISIBLE);
            smallwhite2.setVisibility(View.VISIBLE);
            longwhite.setVisibility(View.VISIBLE);
            //disable game listeners if not turn
            gameBoard.setOnClickListener(null);
            //set contest listeners
            smallwhite1.setOnClickListener(blockButtonListener);
            smallwhite2.setOnClickListener(bluffButtonListener);
            longwhite.setOnClickListener(skipButtonListener);
        }
        else if(playerState.equals("Exchange2")){
            exchangeBtn.setVisibility(View.VISIBLE);
            waitingOverlay.setVisibility(View.GONE);
            deadOverLay.setVisibility(View.GONE);
            //hide game board
            gameBoard.setVisibility(View.GONE);
            //disable listeners if not turn
            gameBoard.setOnClickListener(null);
            smallwhite1.setOnClickListener(null);
            smallwhite2.setOnClickListener(null);
            longwhite.setOnClickListener(null);
            //hide contest layout
            bigBlock.setVisibility(View.GONE);
            smallwhite1.setVisibility(View.GONE);
            smallwhite2.setVisibility(View.GONE);
            longwhite.setVisibility(View.GONE);
            //show button and listeners
        }
        else if(playerState.equals("Exchange1")){
            exchangeBtn.setVisibility(View.VISIBLE);
            waitingOverlay.setVisibility(View.GONE);
            deadOverLay.setVisibility(View.GONE);
            //hide game board
            gameBoard.setVisibility(View.GONE);
            //disable listeners if not turn
            gameBoard.setOnClickListener(null);
            smallwhite1.setOnClickListener(null);
            smallwhite2.setOnClickListener(null);
            longwhite.setOnClickListener(null);
            //hide contest layout
            bigBlock.setVisibility(View.GONE);
            smallwhite1.setVisibility(View.GONE);
            smallwhite2.setVisibility(View.GONE);
            longwhite.setVisibility(View.GONE);

        }
        //implement later
        else if(playerState.equals("challenge")){

            exchangeBtn.setVisibility(View.GONE);
            //hide waiting and dead layout
            waitingOverlay.setVisibility(View.GONE);
            deadOverLay.setVisibility(View.GONE);
            //show challenge mode layout
            bigBlock.setVisibility(View.VISIBLE);
            //hide block
            smallwhite1.setVisibility(View.GONE);
            smallwhite2.setVisibility(View.VISIBLE);
            longwhite.setVisibility(View.VISIBLE);
            //disable game listeners if not turn
            gameBoard.setOnClickListener(null);
            //set contest listeners
            smallwhite1.setOnClickListener(null);
            smallwhite2.setOnClickListener(bluffButtonListener);
            longwhite.setOnClickListener(skipButtonListener);
        }
        else if (playerState.equals("dead")){  //hide contest layout
            //hide game board
            gameBoard.setVisibility(View.GONE);
            exchangeBtn.setVisibility(View.GONE);
        bigBlock.setVisibility(View.GONE);
        smallwhite1.setVisibility(View.GONE);
        smallwhite2.setVisibility(View.GONE);
        longwhite.setVisibility(View.GONE);
        waitingOverlay.setVisibility(View.GONE);
        //show dead on screen
        deadOverLay.setVisibility(View.VISIBLE);
        //disable listeners if not turn
        gameBoard.setOnClickListener(null);
        smallwhite1.setOnClickListener(null);
        smallwhite2.setOnClickListener(null);
        longwhite.setOnClickListener(null);
        }
    }

    public void updatePlayerCoinsUi(int totalCoins, String currentPlayer){
        if (currentPlayer.equals(Player1)){
            numCoins1.setText(String.valueOf(totalCoins));
        }
        else if (currentPlayer.equals(Player2)){
            numCoins2.setText(String.valueOf(totalCoins));
        }
        else if (currentPlayer.equals(Player3)){
            numCoins3.setText(String.valueOf(totalCoins));
        }
        else if (currentPlayer.equals(Player4)){
            numCoins4.setText(String.valueOf(totalCoins));
        }
    }
    //call method 4 times for each player
    public void updatePlayer1LivesUi(int lives){
        if (lives ==  2){
            greenCard1.setVisibility(View.VISIBLE);
            greenCard2.setVisibility(View.VISIBLE);
            greenCard3.setVisibility(View.GONE);
            //disable card listeners
            greenCard3.setOnClickListener(null);
        }
        //one life
        else if (lives ==  1){
            greenCard1.setVisibility(View.GONE);
            greenCard2.setVisibility(View.GONE);
            greenCard3.setVisibility(View.VISIBLE);
            //disable card listeners
            greenCard1.setOnClickListener(null);
            greenCard2.setOnClickListener(null);
            //enable
            greenCard3.setOnClickListener(card3ClickListener);
        }
        else if (lives ==  0){
            greenCard1.setVisibility(View.GONE);
            greenCard2.setVisibility(View.GONE);
            greenCard3.setVisibility(View.GONE);
            //disable card listeners
            greenCard1.setOnClickListener(null);
            greenCard2.setOnClickListener(null);
            greenCard3.setOnClickListener(null);
        }
    }
    public void updatePlayer2LivesUi(int lives){
        if (lives ==  2){
            yellowCard1.setVisibility(View.VISIBLE);
            yellowCard2.setVisibility(View.VISIBLE);
            yellowCard3.setVisibility(View.GONE);
        }
        //one life
        else if (lives ==  1){
            yellowCard1.setVisibility(View.GONE);
            yellowCard2.setVisibility(View.GONE);
            yellowCard3.setVisibility(View.VISIBLE);
        }
        else if (lives ==  0){
            yellowCard1.setVisibility(View.GONE);
            yellowCard2.setVisibility(View.GONE);
            yellowCard3.setVisibility(View.GONE);
        }
    }
    public void updatePlayer3LivesUi(int lives){
        if (lives ==  2){
            redCard1.setVisibility(View.VISIBLE);
            redCard2.setVisibility(View.VISIBLE);
            redCard3.setVisibility(View.GONE);
        }
        //one life
        else if (lives ==  1){
            redCard1.setVisibility(View.GONE);
            redCard2.setVisibility(View.GONE);
            redCard3.setVisibility(View.VISIBLE);
        }
        else if (lives ==  0){
            redCard1.setVisibility(View.GONE);
            redCard2.setVisibility(View.GONE);
            redCard3.setVisibility(View.GONE);
        }
    }
    public void updatePlayer4LivesUi(int lives){
        if (lives ==  2){
            blueCard1.setVisibility(View.VISIBLE);
            blueCard2.setVisibility(View.VISIBLE);
            blueCard3.setVisibility(View.GONE);
        }
        //one life
        else if (lives ==  1){
            blueCard1.setVisibility(View.GONE);
            blueCard2.setVisibility(View.GONE);
            blueCard3.setVisibility(View.VISIBLE);
        }
        else if (lives ==  0){
            blueCard1.setVisibility(View.GONE);
            blueCard2.setVisibility(View.GONE);
            blueCard3.setVisibility(View.GONE);
        }
    }
    public void updatePlayerTurnUi(String currentPlayer){

        // Stop any animations that are currently running on all player icons
        playerIcon1.clearAnimation();
        playerIcon2.clearAnimation();
        playerIcon3.clearAnimation();
        playerIcon4.clearAnimation();
        // Apply the pulse animation.
        Animation pulse = AnimationUtils.loadAnimation(PlayActivity.this, R.anim.pulse_animation);
        //if this screen is current player
        if (currentPlayer.equals(Player1)){
            playerIcon1.startAnimation(pulse);
        }
        else if(currentPlayer.equals(Player2)){
            playerIcon2.startAnimation(pulse);
        }
        else if(currentPlayer.equals(Player3)){
            playerIcon3.startAnimation(pulse);
        }
        else if(currentPlayer.equals(Player4)){
            playerIcon4.startAnimation(pulse);
        }
    }

    @Override
    public void onWebSocketError(Exception ex) {
        Log.e("WebSocketListener", "Error received from WebSocket", ex);
    }
    //game board listener to open up Action Activity(only visible in turn mode)
    private View.OnClickListener gameBoardClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            numCoins1 = 5;
            // Handle game board click
            Intent intent = new Intent(PlayActivity.this, ActionActivity.class);
            intent.putExtra("Player2Key", Player2); // Assume Player2 is a String with some value
            intent.putExtra("Player3Key", Player3); // Assume Player3 is a String with some value
            intent.putExtra("Player4Key", Player4); // Assume Player4 is a String with some value
            intent.putExtra("coins", totalCoins); // Assume Player4 is a String with some value
            startActivity(intent);

        }
    };
    private View.OnClickListener card1ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Objects.equals(card1, "Assassin")){
                showImagePopup(R.drawable.assassin1);
            }
            else if (Objects.equals(card1, "Contessa")){
                showImagePopup(R.drawable.contra1);
            }
            else if (Objects.equals(card1, "Duke")){
                showImagePopup(R.drawable.duke1);
            }
            else if (Objects.equals(card1, "Captain")){
                showImagePopup(R.drawable.captain1);
            }
            else if (Objects.equals(card1, "Ambassador")){
                showImagePopup(R.drawable.ambassador1);
            }

        }
    };
    private View.OnClickListener card2ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Objects.equals(card2, "Assassin")){
                showImagePopup(R.drawable.assassin1);
            }
            else if (Objects.equals(card2, "Contessa")){
                showImagePopup(R.drawable.contra1);
            }
            else if (Objects.equals(card2, "Duke")){
                showImagePopup(R.drawable.duke1);
            }
            else if (Objects.equals(card2, "Captain")){
                showImagePopup(R.drawable.captain1);
            }
            else if (Objects.equals(card2, "Ambassador")){
                showImagePopup(R.drawable.ambassador1);
            }

        }
    };
    private View.OnClickListener card3ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String card = null;
            if (Objects.equals(card1, "null")){
                 card = card2;
            }
            else{
                 card = card1;
            }
            if (Objects.equals(card, "Assassin")){
                showImagePopup(R.drawable.assassin1);
            }
            else if (Objects.equals(card, "Contessa")){
                showImagePopup(R.drawable.contra1);
            }
            else if (Objects.equals(card, "Duke")){
                showImagePopup(R.drawable.duke1);
            }
            else if (Objects.equals(card, "Captain")){
                showImagePopup(R.drawable.captain1);
            }
            else if (Objects.equals(card, "Ambassador")){
                showImagePopup(R.drawable.ambassador1);
            }
        }
    };
    //listeners for contest mode
    private View.OnClickListener blockButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d("GameDebug", "lastMoveMade: " + lastMoveMade);
            if (lastMoveMade.equals("Steal")){
                PopupMenu popup = new PopupMenu(PlayActivity.this, smallwhite1);
                popup.getMenuInflater().inflate(R.menu.steal_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        // Variable to hold the selected player
                        String whatCharacter = null;
                        // Check which item was clicked and set the selected player accordingly
                        int id = item.getItemId();
                        if (id == R.id.CA) {
                            whatCharacter = "Ambassador";
                        } else if (id == R.id.CC) {
                            whatCharacter = "Captain";
                        }
                        // After picking the player, send the WebSocket message
                        if (whatCharacter != null) {
                            JSONObject jsonObject = new JSONObject();
                            if (whatCharacter.equals("Ambassador")){
                                try {
                                    jsonObject.put("playerEmail", Const.getCurrentEmail());
                                    jsonObject.put("move", "Block Ambassador");
                                    jsonObject.put("targetPlayer", "null");
                                    jsonObject.put("card1", "null");
                                    jsonObject.put("card2", "null");
                                    Log.d("Websocket", "MoveMade: *Block Ambassador");
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                String jsonStr = jsonObject.toString();
                                WebSocketManager.getInstance().sendMessage(jsonStr);

                            }
                            //captain
                            else {
                                try {
                                    jsonObject.put("playerEmail", Const.getCurrentEmail());
                                    jsonObject.put("move", "Block Captain");
                                    jsonObject.put("targetPlayer", "null");
                                    jsonObject.put("card1", "null");
                                    jsonObject.put("card2", "null");
                                    Log.d("Websocket", "MoveMade: Block Captain");
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                String jsonStr = jsonObject.toString();
                                WebSocketManager.getInstance().sendMessage(jsonStr);
                            }
                        }
                        return true;
                    }
                });
                // Showing the popup
                popup.show();
            }
            else{
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("playerEmail", Const.getCurrentEmail());
                    jsonObject.put("move", "Block");
                    jsonObject.put("targetPlayer", "null");
                    jsonObject.put("card1", "null");
                    jsonObject.put("card2", "null");
                    Log.d("Websocket", "MoveMade: Block");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                String jsonStr = jsonObject.toString();
                WebSocketManager.getInstance().sendMessage(jsonStr);
            }
        }
    };
    private View.OnClickListener bluffButtonListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("playerEmail", Const.getCurrentEmail());
                jsonObject.put("move", "Bluff");
                jsonObject.put("targetPlayer", "null");
                jsonObject.put("card1", "null");
                jsonObject.put("card2", "null");
                Log.d("Websocket", "MoveMade: Bluff");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            String jsonStr = jsonObject.toString();
            WebSocketManager.getInstance().sendMessage(jsonStr);
        }
    };

    //pass button function
    private View.OnClickListener skipButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("playerEmail", Const.getCurrentEmail());
                jsonObject.put("move", "pass");
                jsonObject.put("targetPlayer", "null");
                jsonObject.put("card1", "null");
                jsonObject.put("card2", "null");
                Log.d("Websocket", "MoveMade: Skip");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            String jsonStr = jsonObject.toString();
            WebSocketManager.getInstance().sendMessage(jsonStr);
            //update state to waiting
            playerState = "wait";
            updatePlayerStateUi();
        }
    };


    private void addMessageToLayout(String username, String message) {
        //create a new view with xml layout and indicate where to add but dont attach yet
        View messageView = getLayoutInflater().inflate(R.layout.chat_item, layoutMessages1, false);
        //find these views in the layout
        TextView textView = messageView.findViewById(R.id.placement);
        TextView usernameB = messageView.findViewById(R.id.viewUsername);
        //assign these views
        textView.setText(message);
        usernameB.setText(username);
        //add view to linear layout
        layoutMessages1.addView(messageView);
        // After adding the message, scroll to the bottom of the scrollViewMessages to show the latest message.
        scrollViewMessages.post(() -> scrollViewMessages.fullScroll(ScrollView.FOCUS_DOWN));
    };

    private void loadMessages() {
        List<String[]> existingMessages = ChatManager.getInstance().getMessages();
        for (String[] messageData : existingMessages) {
            addMessageToLayout(messageData[0], messageData[1]);
        }
    };

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chat Message");
        // Set up the input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        builder.setView(input);
        // Set up the buttons
        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String message = input.getText().toString();
//                String messageToSend = chatMessage.getText().toString();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("playerEmail", Const.getCurrentEmail());
                    jsonObject.put("move", "@" + message);
                    jsonObject.put("targetPlayer", "null");
                    jsonObject.put("card1", "null");
                    jsonObject.put("card2", "null");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                String jsonStr = jsonObject.toString();
                WebSocketManager.getInstance().sendMessage(jsonStr);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    public void showGameOverDialog() {
        Context context = this; // Use 'this' or 'getActivity()' in case of a fragment
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Game Over");  // Set the title for the dialog
        builder.setMessage("The game has ended.");  // Set the message to show

        // Add a button to the dialog
        builder.setPositiveButton("Go to Main Menu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(PlayActivity.this, MainActivity.class);
                WebSocketManager.getInstance().disconnectWebSocket();
                startActivity(intent);
                dialog.dismiss();  // Dismiss the dialog when the button is clicked
            }
        });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void sendMessage(String username, String message) {
        // Add message to layout
        addMessageToLayout(username, message);
        // Save message to Singleton
        ChatManager.getInstance().addMessage(username, message);
    }
    private void showImagePopup(int imageResource) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogLayout = getLayoutInflater().inflate(R.layout.popup_image, null);
        ImageView imageView = dialogLayout.findViewById(R.id.popup_image);

        imageView.setImageResource(imageResource);
        builder.setView(dialogLayout);
        AlertDialog dialog = builder.create();

        dialogLayout.setOnClickListener(v -> dialog.dismiss());

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        dialog.show();
    }


    public void showDialog2() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_exchange);


        CheckBox checkbox1 = dialog.findViewById(R.id.radioButtonMain);
        CheckBox checkbox2 = dialog.findViewById(R.id.radioButtonMain2);
        CheckBox checkbox3 = dialog.findViewById(R.id.radioButtonMain3);
        CheckBox checkbox4 = dialog.findViewById(R.id.radioButtonMain4);
        Button doExchange = dialog.findViewById(R.id.button4);
        if (playerState.equals("Exchange2")){
            checkbox1.setVisibility(View.VISIBLE);
            checkbox2.setVisibility(View.VISIBLE);
            checkbox3.setVisibility(View.VISIBLE);
            checkbox4.setVisibility(View.VISIBLE);
            //update names for check boxes
            checkbox1.setText(card1);
            checkbox2.setText(card2);
            checkbox3.setText(exCard1);
            checkbox4.setText(exCard2);
        }
        else if (playerState.equals("Exchange1")){
            checkbox1.setVisibility(View.VISIBLE);
            checkbox2.setVisibility(View.VISIBLE);
            checkbox3.setVisibility(View.GONE);
            checkbox4.setVisibility(View.GONE);
//            update names for check boxes
            if(Objects.equals(card1, "null")){
                checkbox1.setText(card2);
            }
            if(Objects.equals(card2, "null")){
                checkbox1.setText(card1);
            }
            checkbox2.setText(exCard1);
        }

        doExchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Objects.equals(playerState, "Exchange2")) {
                    // Variables to hold the selections
                    String selectedCard1 = "null";
                    String selectedCard2 = "null";

                    // Check which checkboxes are checked and record the selections
                    if (checkbox1.isChecked()) {
                        selectedCard1 = checkbox1.getText().toString();
                    }
                    if (checkbox2.isChecked()) {
                        if (selectedCard1.equals("null")) {
                            selectedCard1 = checkbox2.getText().toString();
                        } else {
                            selectedCard2 = checkbox2.getText().toString();
                        }
                    }
                    if (checkbox3.isChecked()) {
                        if (selectedCard1.equals("null")) {
                            selectedCard1 = checkbox3.getText().toString();
                        } else {
                            selectedCard2 = checkbox3.getText().toString();
                        }
                    }
                    if (checkbox4.isChecked()) {
                        if (selectedCard1.equals("null")) {
                            selectedCard1 = checkbox4.getText().toString();
                        } else {
                            selectedCard2 = checkbox4.getText().toString();
                        }
                    }

                    // Verify that two cards are selected
                    if (selectedCard1.equals("null") || selectedCard2.equals("null")) {
                        // If less than two cards are selected, inform the user and do not proceed
                        Toast.makeText(PlayActivity.this, "You must select at least two cards to proceed", Toast.LENGTH_SHORT).show();
                        return; // Exit the onClick method early
                    }

                    // Proceed with the exchange
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("playerEmail", Const.getCurrentEmail());
                        jsonObject.put("move", "*Exchange");
                        jsonObject.put("targetPlayer", "null");
                        jsonObject.put("card1", selectedCard1);
                        jsonObject.put("card2", selectedCard2);
                        Log.d("Websocket", "MoveMade: Exchange");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    String jsonStr = jsonObject.toString();
                    WebSocketManager.getInstance().sendMessage(jsonStr);
                } else if (Objects.equals(playerState, "Exchange1")) {
                    // Variables to hold the selections
                    String selectedCard1 = "null";

                    // Check which checkboxes are checked and record the selections
                    if (checkbox1.isChecked()) {
                        selectedCard1 = checkbox1.getText().toString();
                    }
                    if (checkbox2.isChecked()) {
                        selectedCard1 = checkbox2.getText().toString();
                    }

                    // Verify that two cards are selected
                    if (selectedCard1.equals("null") ) {
                        // If less than two cards are selected, inform the user and do not proceed
                        Toast.makeText(PlayActivity.this, "You must select at least one card to proceed", Toast.LENGTH_SHORT).show();
                        return; // Exit the onClick method early
                    }
                    // Proceed with the exchange
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("playerEmail", Const.getCurrentEmail());
                        jsonObject.put("move", "*Exchange");
                        jsonObject.put("targetPlayer", "null");
                        jsonObject.put("card1", selectedCard1);
                        jsonObject.put("card2", "null");
                        Log.d("Websocket", "MoveMade: Exchange");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    String jsonStr = jsonObject.toString();
                    WebSocketManager.getInstance().sendMessage(jsonStr);
                }
                dialog.dismiss();
            }
        });


        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Update the checked count
                if (isChecked) {
                    checkedCount++;
                } else {
                    checkedCount--;
                }
                if (Objects.equals(playerState, "Exchange2")){
                    // If more than two checkboxes are selected, show a toast and uncheck the last selected checkbox
                    if (checkedCount > 2) {
                        Toast.makeText(PlayActivity.this, "You can only select up to two options", Toast.LENGTH_SHORT).show();
                        // Uncheck and do not count the last checkbox that caused the overflow
                        buttonView.setChecked(false);
                        checkedCount--;
                    }
                }
                else if (Objects.equals(playerState, "Exchange1")){
                    // If more than one checkboxe os selected, show a toast and uncheck the last selected checkbox
                    if (checkedCount > 1) {
                        Toast.makeText(PlayActivity.this, "You can only select one option", Toast.LENGTH_SHORT).show();
                        // Uncheck and do not count the last checkbox that caused the overflow
                        buttonView.setChecked(false);
                        checkedCount--;
                    }
                }
            }
        };
        checkbox1.setOnCheckedChangeListener(listener);
        checkbox2.setOnCheckedChangeListener(listener);
        checkbox3.setOnCheckedChangeListener(listener);
        checkbox4.setOnCheckedChangeListener(listener);


        dialog.setCancelable(true);
        dialog.show();
    }
    
}

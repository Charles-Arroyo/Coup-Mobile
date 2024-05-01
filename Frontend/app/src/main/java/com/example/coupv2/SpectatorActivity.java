package com.example.coupv2;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
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

public class SpectatorActivity extends AppCompatActivity implements WebSocketListener{
    TextView currentPlayerText;

    //timer
    //game chat Views
    //chat layout views
    private LinearLayout layoutMessages1;
    private ScrollView scrollViewMessages;
    private EditText chatMessage;
    //    private ImageButton openChat;
    //views for when in contest mode

    //player variables
    String p1card1;
    String p1card2;
    String p2card1;
    String p2card2;
    String p3card1;
    String p3card2;
    String p4card1;
    String p4card2;

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
    //order of game for current player
    String Player1;
    String Player2;
    String Player3;
    String Player4;

    //view for player icons
    ImageView playerIcon1;
    ImageView playerIcon2;
    ImageView playerIcon3;
    ImageView playerIcon4;
    //view for text icon down screen
    ImageView textBox;

    @Override
    protected void onResume() {
        super.onResume();
        //load messages
        loadMessages();
        //set listener to this class
        WebSocketManager.getInstance().setWebSocketListener(this);
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
            jsonObject.put("move", "spectate");
            jsonObject.put("targetPlayer", "null");
            jsonObject.put("card1", "null");
            jsonObject.put("card2", "null");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String jsonStr = jsonObject.toString();
        WebSocketManager.getInstance().sendMessage(jsonStr);

        // link Play activity XML
        setContentView(R.layout.activity_spectator);
        //exchange buttons nad setup
        //playericons
        playerIcon1 = findViewById(R.id.person1);
        playerIcon2 = findViewById(R.id.person2);
        playerIcon3 = findViewById(R.id.person3);
        playerIcon4 = findViewById(R.id.person4);
        //current player
        currentPlayerText = findViewById(R.id.timerText);
        // Set a click listeners for player icons
        playerIcon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the username of the player
                String playerUsername = Player1; // Replace with actual logic to retrieve the username

                // Create an AlertDialog builder
                AlertDialog.Builder builder = new AlertDialog.Builder(SpectatorActivity.this);

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
                String playerUsername = Player2; // Replace with actual logic to retrieve the username

                // Create an AlertDialog builder
                AlertDialog.Builder builder = new AlertDialog.Builder(SpectatorActivity.this);

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
                String playerUsername = Player3; // Replace with actual logic to retrieve the username

                // Create an AlertDialog builder
                AlertDialog.Builder builder = new AlertDialog.Builder(SpectatorActivity.this);

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
                String playerUsername = Player4; // Replace with actual logic to retrieve the username

                // Create an AlertDialog builder
                AlertDialog.Builder builder = new AlertDialog.Builder(SpectatorActivity.this);

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
        greenCard1.setOnClickListener(Player1card1ClickListener);
        greenCard2.setOnClickListener(Player1card2ClickListener);
        greenCard3.setOnClickListener(Player1card3ClickListener);
        //other cards
        redCard1 = findViewById(R.id.redIcon1);
        redCard2 = findViewById(R.id.redIcon2);
        redCard3 = findViewById(R.id.redIcon3);
        redCard1.setOnClickListener(Player3card1ClickListener);
        redCard2.setOnClickListener(Player3card2ClickListener);
        redCard3.setOnClickListener(Player3card3ClickListener);
        //
        blueCard1 = findViewById(R.id.blueIcon1);
        blueCard2 = findViewById(R.id.blueIcon2);
        blueCard3 = findViewById(R.id.blueIcon3);
        blueCard1.setOnClickListener(Player4card1ClickListener);
        blueCard2.setOnClickListener(Player4card2ClickListener);
        blueCard3.setOnClickListener(Player4card3ClickListener);
        //
        redCard3.setOnClickListener(Player3card3ClickListener);
        yellowCard1 = findViewById(R.id.yellowicon1);
        yellowCard2 = findViewById(R.id.yellowicon2);
        yellowCard3 = findViewById(R.id.yellowicon3);
        yellowCard1.setOnClickListener(Player2card1ClickListener);
        yellowCard2.setOnClickListener(Player2card2ClickListener);
        yellowCard3.setOnClickListener(Player2card3ClickListener);
        //assign coins
        numCoins1 = findViewById(R.id.oval1Text);
        numCoins2 = findViewById(R.id.oval2Text);
        numCoins3 = findViewById(R.id.oval3Text);
        numCoins4 = findViewById(R.id.oval4Text);
        //chat layout views
        scrollViewMessages = findViewById(R.id.scrollViewMessages1);
        layoutMessages1 = findViewById(R.id.layoutMessages1);
        textBox =  findViewById(R.id.imageView4);


        // Create an AlertDialog builder
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
            showGameOverDialog();
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

            // This will run on the UI thread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {

                        // Perform UI updates for each player
                        for (int i = 0; i < playerArray.length(); i++) {
                            //get all data from player object
                            JSONObject player = playerArray.getJSONObject(i);
                            String playerEmail = player.getString("userEmail");  //player email for current index
                            int thisGuyCoins = player.getInt("coins");
                            int thisGuyLives = player.getInt("lives");


                            if (i == 0){
                                Player1 = playerEmail;
                                p1card1 = player.getString("cardOne");
                                p1card2 = player.getString("cardTwo");
                            }
                            else if (i == 1){
                                Player2 = playerEmail;
                                p2card1 = player.getString("cardOne");
                                p2card2 = player.getString("cardTwo");
                            }
                            else if (i == 2){
                                Player3 = playerEmail;
                                p3card1 = player.getString("cardOne");
                                p3card2 = player.getString("cardTwo");
                            }
                            else if (i == 3 ){
                                Player4 = playerEmail;
                                p4card1 = player.getString("cardOne");
                                p4card2 = player.getString("cardTwo");
                            }
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

                            // If this player's turn is true, then animate his character
                            if (player.getBoolean("turn")) {
                                currentPlayerText.setText("Current Player: " + playerEmail);
                                updatePlayerTurnUi(playerEmail);
                            }
                        }


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
            greenCard3.setOnClickListener(Player1card3ClickListener);
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
            yellowCard3.setOnClickListener(null);
        }
        //one life
        else if (lives ==  1){
            yellowCard1.setVisibility(View.GONE);
            yellowCard2.setVisibility(View.GONE);
            yellowCard3.setVisibility(View.VISIBLE);
            //disable card listeners
            yellowCard1.setOnClickListener(null);
            yellowCard2.setOnClickListener(null);
        }
        else if (lives ==  0){
            yellowCard1.setVisibility(View.GONE);
            yellowCard2.setVisibility(View.GONE);
            yellowCard3.setVisibility(View.GONE);

            yellowCard1.setOnClickListener(null);
            yellowCard2.setOnClickListener(null);
            yellowCard3.setOnClickListener(null);
        }
    }
    public void updatePlayer3LivesUi(int lives){
        if (lives ==  2){
            redCard1.setVisibility(View.VISIBLE);
            redCard2.setVisibility(View.VISIBLE);
            redCard3.setVisibility(View.GONE);
            redCard3.setOnClickListener(null);
        }
        //one life
        else if (lives ==  1){
            redCard1.setVisibility(View.GONE);
            redCard2.setVisibility(View.GONE);
            redCard3.setVisibility(View.VISIBLE);

            //disable card listeners
            redCard1.setOnClickListener(null);
            redCard2.setOnClickListener(null);
        }
        else if (lives ==  0){
            redCard1.setVisibility(View.GONE);
            redCard2.setVisibility(View.GONE);
            redCard3.setVisibility(View.GONE);

            redCard1.setOnClickListener(null);
            redCard2.setOnClickListener(null);
            redCard3.setOnClickListener(null);
        }
    }
    public void updatePlayer4LivesUi(int lives){
        if (lives ==  2){
            blueCard1.setVisibility(View.VISIBLE);
            blueCard2.setVisibility(View.VISIBLE);
            blueCard3.setVisibility(View.GONE);
            blueCard3.setOnClickListener(null);
        }
        //one life
        else if (lives ==  1){
            blueCard1.setVisibility(View.GONE);
            blueCard2.setVisibility(View.GONE);
            blueCard3.setVisibility(View.VISIBLE);

            //disable card listeners
            blueCard1.setOnClickListener(null);
            blueCard2.setOnClickListener(null);
        }
        else if (lives ==  0){
            blueCard1.setVisibility(View.GONE);
            blueCard2.setVisibility(View.GONE);
            blueCard3.setVisibility(View.GONE);

            blueCard1.setOnClickListener(null);
            blueCard2.setOnClickListener(null);
            blueCard3.setOnClickListener(null);
        }
    }
    public void updatePlayerTurnUi(String currentPlayer){

        // Stop any animations that are currently running on all player icons
        playerIcon1.clearAnimation();
        playerIcon2.clearAnimation();
        playerIcon3.clearAnimation();
        playerIcon4.clearAnimation();
        // Apply the pulse animation.
        Animation pulse = AnimationUtils.loadAnimation(SpectatorActivity.this, R.anim.pulse_animation);
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

    private View.OnClickListener Player1card1ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Objects.equals(p1card1, "Assassin")){
                showImagePopup(R.drawable.assassin1);
            }
            else if (Objects.equals(p1card1, "Contessa")){
                showImagePopup(R.drawable.contra1);
            }
            else if (Objects.equals(p1card1, "Duke")){
                showImagePopup(R.drawable.duke1);
            }
            else if (Objects.equals(p1card1, "Captain")){
                showImagePopup(R.drawable.captain1);
            }
            else if (Objects.equals(p1card1, "Ambassador")){
                showImagePopup(R.drawable.ambassador1);
            }

        }
    };
    private View.OnClickListener Player1card2ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Objects.equals(p1card2, "Assassin")){
                showImagePopup(R.drawable.assassin1);
            }
            else if (Objects.equals(p1card2, "Contessa")){
                showImagePopup(R.drawable.contra1);
            }
            else if (Objects.equals(p1card2, "Duke")){
                showImagePopup(R.drawable.duke1);
            }
            else if (Objects.equals(p1card2, "Captain")){
                showImagePopup(R.drawable.captain1);
            }
            else if (Objects.equals(p1card2, "Ambassador")){
                showImagePopup(R.drawable.ambassador1);
            }

        }
    };
    private View.OnClickListener Player1card3ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String card = null;
            if (Objects.equals(p1card1, "null")){
                card = p1card2;
            }
            else{
                card = p1card1;
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
    private View.OnClickListener Player2card1ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Objects.equals(p2card1, "Assassin")){
                showImagePopup(R.drawable.assassin1);
            }
            else if (Objects.equals(p2card1, "Contessa")){
                showImagePopup(R.drawable.contra1);
            }
            else if (Objects.equals(p2card1, "Duke")){
                showImagePopup(R.drawable.duke1);
            }
            else if (Objects.equals(p2card1, "Captain")){
                showImagePopup(R.drawable.captain1);
            }
            else if (Objects.equals(p2card1, "Ambassador")){
                showImagePopup(R.drawable.ambassador1);
            }

        }
    };
    private View.OnClickListener Player2card2ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Objects.equals(p2card2, "Assassin")){
                showImagePopup(R.drawable.assassin1);
            }
            else if (Objects.equals(p2card2, "Contessa")){
                showImagePopup(R.drawable.contra1);
            }
            else if (Objects.equals(p2card2, "Duke")){
                showImagePopup(R.drawable.duke1);
            }
            else if (Objects.equals(p2card2, "Captain")){
                showImagePopup(R.drawable.captain1);
            }
            else if (Objects.equals(p2card2, "Ambassador")){
                showImagePopup(R.drawable.ambassador1);
            }

        }
    };
    private View.OnClickListener Player2card3ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String card = null;
            if (Objects.equals(p2card1, "null")){
                card = p2card2;
            }
            else{
                card = p2card1;
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
    private View.OnClickListener Player3card1ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Objects.equals(p3card1, "Assassin")){
                showImagePopup(R.drawable.assassin1);
            }
            else if (Objects.equals(p3card1, "Contessa")){
                showImagePopup(R.drawable.contra1);
            }
            else if (Objects.equals(p3card1, "Duke")){
                showImagePopup(R.drawable.duke1);
            }
            else if (Objects.equals(p3card1, "Captain")){
                showImagePopup(R.drawable.captain1);
            }
            else if (Objects.equals(p3card1, "Ambassador")){
                showImagePopup(R.drawable.ambassador1);
            }

        }
    };
    private View.OnClickListener Player3card2ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Objects.equals(p3card2, "Assassin")){
                showImagePopup(R.drawable.assassin1);
            }
            else if (Objects.equals(p3card2, "Contessa")){
                showImagePopup(R.drawable.contra1);
            }
            else if (Objects.equals(p3card2, "Duke")){
                showImagePopup(R.drawable.duke1);
            }
            else if (Objects.equals(p3card2, "Captain")){
                showImagePopup(R.drawable.captain1);
            }
            else if (Objects.equals(p3card2, "Ambassador")){
                showImagePopup(R.drawable.ambassador1);
            }

        }
    };
    private View.OnClickListener Player3card3ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String card = null;
            if (Objects.equals(p3card1, "null")){
                card = p3card2;
            }
            else{
                card = p3card1;
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
    private View.OnClickListener Player4card1ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Objects.equals(p4card1, "Assassin")){
                showImagePopup(R.drawable.assassin1);
            }
            else if (Objects.equals(p4card1, "Contessa")){
                showImagePopup(R.drawable.contra1);
            }
            else if (Objects.equals(p4card1, "Duke")){
                showImagePopup(R.drawable.duke1);
            }
            else if (Objects.equals(p4card1, "Captain")){
                showImagePopup(R.drawable.captain1);
            }
            else if (Objects.equals(p4card1, "Ambassador")){
                showImagePopup(R.drawable.ambassador1);
            }

        }
    };
    private View.OnClickListener Player4card2ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Objects.equals(p4card2, "Assassin")){
                showImagePopup(R.drawable.assassin1);
            }
            else if (Objects.equals(p4card2, "Contessa")){
                showImagePopup(R.drawable.contra1);
            }
            else if (Objects.equals(p4card2, "Duke")){
                showImagePopup(R.drawable.duke1);
            }
            else if (Objects.equals(p4card2, "Captain")){
                showImagePopup(R.drawable.captain1);
            }
            else if (Objects.equals(p4card2, "Ambassador")){
                showImagePopup(R.drawable.ambassador1);
            }

        }
    };
    private View.OnClickListener Player4card3ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String card = null;
            if (Objects.equals(p4card1, "null")){
                card = p4card2;
            }
            else{
                card = p4card1;
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

    public void sendMessage(String username, String message) {
        // Add message to layout
        addMessageToLayout(username, message);
        // Save message to Singleton
        ChatManager.getInstance().addMessage(username, message);
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
                Intent intent = new Intent(SpectatorActivity.this, MainActivity.class);
                WebSocketManager.getInstance().disconnectWebSocket();
                startActivity(intent);
                dialog.dismiss();  // Dismiss the dialog when the button is clicked
            }
        });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
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




}

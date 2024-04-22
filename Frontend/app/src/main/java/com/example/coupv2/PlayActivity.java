package com.example.coupv2;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coupv2.utils.Const;

import java.util.Objects;

public class PlayActivity extends AppCompatActivity implements WebSocketListener{
    //has player order been determined (not using at current moment but might later)
//    boolean playerOrder = false;
    //game chat Views
    private LinearLayout layoutMessages;
    private ScrollView scrollViewMessages;
    private ImageButton submitText;
    private EditText chatMessage;
    private ImageButton openChat;
    //views for when player is waiting
    ImageView waitingOverlay;
    //views for when player turn
    private ImageView gameBoard;
    //views for when in contest mode
    ImageView smallwhite1;
    TextView smallwhite1Text;
    ImageView smallwhite2;
    TextView smallwhite2Text;
    ImageView longwhite;
    TextView longwhitetext;
    ImageView bigBlock;
    //player variables
    String card1;
    String card2;
    String playerState;
    int turnIndex; //will be a value of 1-4
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
    ImageView cardIcon2;
    ImageView cardIcon3;
    TextView numCoins1;
    TextView numCoins2;
    TextView numCoins3;
    TextView numCoins4;
    //order of game for current player
    String Player1;
    String Player2;
    String Player3;
    String Player4;
    //last move (this is used for case of blocking stealing)
    String lastMoveMade;
    @Override
    //keep websocket open from LobbyActivity
    protected void onResume() {
        super.onResume();
        //set listener to this class
        WebSocketManager.getInstance().setWebSocketListener(this);
        JSONObject jsonObject = new JSONObject();
        //let backend know that player is ready to receive data
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
        // link Play activity XML
        setContentView(R.layout.activity_play);
        //assign screen views
        greenCard1 = findViewById(R.id.greenIcon1);
        greenCard2 = findViewById(R.id.greenIcon2);
        greenCard3 = findViewById(R.id.greenIcon3);
        redCard1 = findViewById(R.id.redIcon1);
        redCard2 = findViewById(R.id.redIcon2);
        redCard3 = findViewById(R.id.redIcon3);
        blueCard1 = findViewById(R.id.blueIcon1);
        blueCard2 = findViewById(R.id.blueIcon2);
        blueCard3 = findViewById(R.id.blueIcon3);
        yellowCard1 = findViewById(R.id.yellowicon1);
        yellowCard2 = findViewById(R.id.yellowicon2);
        yellowCard3 = findViewById(R.id.yellowicon3);
         numCoins1 = findViewById(R.id.oval1Text);
         numCoins2 = findViewById(R.id.oval2Text);
         numCoins3 = findViewById(R.id.oval3Text);
         numCoins4 = findViewById(R.id.oval4Text);
        //assign chat views
        scrollViewMessages = findViewById(R.id.scrollViewMessages1);
        layoutMessages = findViewById(R.id.layoutMessages);
        chatMessage =  findViewById(R.id.chatText);
        submitText =  findViewById(R.id.submitText);
        openChat = findViewById(R.id.imageButton2);
        //assign view for player is waiting
        waitingOverlay = findViewById(R.id.gameBoard_wait);
        //assign view for player turn
        gameBoard = findViewById(R.id.gameBoard);
        gameBoard.setOnClickListener(gameBoardClickListener); //set gameBoard to be visible by default
        //assign view for player is contesting
        smallwhite1 = findViewById(R.id.smallwhite1);
        smallwhite1Text = findViewById(R.id.smallwhite1Text);
        smallwhite2 = findViewById(R.id.smallwhite2);
        smallwhite2Text = findViewById(R.id.smallwhite2Text);
        longwhite = findViewById(R.id.longwhite);
        longwhitetext = findViewById(R.id.longwhitetext);
        bigBlock = findViewById(R.id.BIGBLOCK);
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
//                ImageView playerIcon = findViewById(R.id.person3); // Replace with your actual ImageView ID.
//                // Apply the pulse animation.
//                Animation pulse = AnimationUtils.loadAnimation(PlayActivity.this, R.anim.pulse_animation);
//                playerIcon.startAnimation(pulse);
                updatePlayerStateUi();
            }
        });
        submitText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageToSend = chatMessage.getText().toString();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("playerEmail", Const.getCurrentEmail());
                    jsonObject.put("move", "@" + messageToSend);
                    jsonObject.put("targetPlayer", "null");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                String jsonStr = jsonObject.toString();
                WebSocketManager.getInstance().sendMessage(jsonStr);
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
        else if (message.matches(".*: '.*'")) {
            // It matches the pattern "Username: 'message'"
            processStringMessage(message);
        }

        // Unknown format
        else {
            Log.e("WebSocket", "Received message in unknown format: " + message);
        }
    }
    //how to handle chat data
    private void processStringMessage(String message) {

        // Extract the username and message from the string
        int colonIndex = message.indexOf(":");
        if (colonIndex != -1) {
            String username = message.substring(0, colonIndex).trim();
            String messageContent = message.substring(colonIndex + 1).trim().replaceAll("^'(.*)'$", "$1");

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Update UI components with the username and message
                    // ...
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
            // Read the player array
            final JSONArray playerArray = game.getJSONArray("playerArrayList");

            // This will run on the UI thread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
//                        lastMoveMade = game.getString("currentMove");

                        //set player order before anything
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
                                    }
                                    else if(j == 1){
                                        Player2 = viewEmail;
                                    }
                                    else if(j == 2){
                                        Player3 = viewEmail;
                                    }
                                    else if(j == 3){
                                        Player4 = viewEmail;
                                    }
                                }
                                // Updating player state and cards for the current -player
                                playerState = player.getString("playerState");
                                card1 = player.getString("cardOne");
                                card2 = player.getString("cardTwo");
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

                            // Compare the current player's email with the logged-in user's email
//                            if (playerEmail.equals(Const.getCurrentEmail())) {
//                                for (int j = 0; j < playerView.length(); j++) {
//                                    String viewEmail = playerView.getString(j);
//                                    if(j == 0){
//                                        Player1 = viewEmail;
//                                    }
//                                    else if(j == 1){
//                                        Player2 = viewEmail;
//                                    }
//                                    else if(j == 2){
//                                        Player3 = viewEmail;
//                                    }
//                                    else if(j == 3){
//                                        Player4 = viewEmail;
//                                    }
//                                }
//                                // Updating player state and cards for the current -player
//                                playerState = player.getString("playerState");
//                                card1 = player.getString("cardOne");
//                                card2 = player.getString("cardTwo");
//                            }

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
                                updatePlayerTurnUi(playerEmail);
                            }
                        }
                        // Call updatePlayerStateUi separately to update the UI based on the player state
                        updatePlayerStateUi();
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

    }
    //check if player is waiting
    public void updatePlayerStateUi(){
        //if not player turn then display waiting
        if (playerState.equals("wait")){
            //hide contest layout
            bigBlock.setVisibility(View.GONE);
            smallwhite1.setVisibility(View.GONE);
            smallwhite1Text.setVisibility(View.GONE);
            smallwhite2.setVisibility(View.GONE);
            smallwhite2Text.setVisibility(View.GONE);
            longwhite.setVisibility(View.GONE);
            longwhitetext.setVisibility(View.GONE);
            //show waiting on screen
            waitingOverlay.setVisibility(View.VISIBLE);
            //disable listeners if not turn
            gameBoard.setOnClickListener(null);
            smallwhite1.setOnClickListener(null);
            smallwhite2.setOnClickListener(null);
            longwhite.setOnClickListener(null);
        }
        //if player turn
        else if (playerState.equals("turn")){
            //hide contest layout
            bigBlock.setVisibility(View.GONE);
            smallwhite1.setVisibility(View.GONE);
            smallwhite1Text.setVisibility(View.GONE);
            smallwhite2.setVisibility(View.GONE);
            smallwhite2Text.setVisibility(View.GONE);
            longwhite.setVisibility(View.GONE);
            longwhitetext.setVisibility(View.GONE);
            //hide waiting layout
            waitingOverlay.setVisibility(View.GONE);
            //set on game board listener
            gameBoard.setOnClickListener(gameBoardClickListener);
            //disable contest listeners
            smallwhite1.setOnClickListener(null);
            smallwhite2.setOnClickListener(null);
            longwhite.setOnClickListener(null);
        }
        //implement later
        else if(playerState.equals("contest")){
            //hide waiting layout
            waitingOverlay.setVisibility(View.GONE);
            //show contest mode layout
            bigBlock.setVisibility(View.VISIBLE);
            smallwhite1.setVisibility(View.VISIBLE);
            smallwhite1Text.setVisibility(View.VISIBLE);
            smallwhite2.setVisibility(View.VISIBLE);
            smallwhite2Text.setVisibility(View.VISIBLE);
            longwhite.setVisibility(View.VISIBLE);
            longwhitetext.setVisibility(View.VISIBLE);
            //disable game listeners if not turn
            gameBoard.setOnClickListener(null);
            //set contest listeners
            smallwhite1.setOnClickListener(blockButtonListener);
            smallwhite2.setOnClickListener(bluffButtonListener);
            longwhite.setOnClickListener(skipButtonListener);
        }
    }
    //update player coins
//    public void updatePlayerCoinsUi(int totalCoins, int numOfTurn){
//        if (numOfTurn == 1){
//            numCoins1.setText(String.valueOf(totalCoins));
//        }
//        else if (numOfTurn == 2){
//            numCoins2.setText(String.valueOf(totalCoins));
//        }
//        else if (numOfTurn == 3){
//            numCoins3.setText(String.valueOf(totalCoins));
//        }
//        else if (numOfTurn == 4){
//            numCoins4.setText(String.valueOf(totalCoins));
//        }
//    }
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
        }
        //one life
        else if (lives ==  1){
            greenCard1.setVisibility(View.GONE);
            greenCard2.setVisibility(View.GONE);
            greenCard3.setVisibility(View.VISIBLE);
        }
        else if (lives ==  0){
            greenCard1.setVisibility(View.GONE);
            greenCard2.setVisibility(View.GONE);
            greenCard3.setVisibility(View.GONE);
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
        ImageView playerIcon1 = findViewById(R.id.person1);
        ImageView playerIcon2 = findViewById(R.id.person2);
        ImageView playerIcon3 = findViewById(R.id.person3);
        ImageView playerIcon4 = findViewById(R.id.person4);

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
            // Handle game board click
            Intent intent = new Intent(PlayActivity.this, ActionActivity.class);
            startActivity(intent);
        }
    };
    //listeners for contest mode
    private View.OnClickListener blockButtonListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
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
                                    jsonObject.put("move", "*Block Ambassador");
                                    jsonObject.put("targetPlayer", null);
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
                                    jsonObject.put("move", "*Block Captain");
                                    jsonObject.put("targetPlayer", null);
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
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            String jsonStr = jsonObject.toString();
            WebSocketManager.getInstance().sendMessage(jsonStr);
        }
    };
    private View.OnClickListener skipButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("playerEmail", Const.getCurrentEmail());
                jsonObject.put("move", "Allow");
                jsonObject.put("targetPlayer", "null");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            String jsonStr = jsonObject.toString();
            WebSocketManager.getInstance().sendMessage(jsonStr);
        }
    };
    private void addMessageToLayout(String username, String message) {
        //create a new view with xml layout and indicate where to add but dont attach yet
        View messageView = getLayoutInflater().inflate(R.layout.friends_msg_item, layoutMessages, false);
        //find these views in the layout
        TextView textView = messageView.findViewById(R.id.placement);
        Button usernameButton = messageView.findViewById(R.id.btnUsername);
        //assign these views
        textView.setText(message);
        usernameButton.setText(username);
        //add view to linear layout
        layoutMessages.addView(messageView);
        // After adding the message, scroll to the bottom of the scrollViewMessages to show the latest message.
        scrollViewMessages.post(() -> scrollViewMessages.fullScroll(ScrollView.FOCUS_DOWN));
    };
}

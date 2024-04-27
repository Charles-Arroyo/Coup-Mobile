package com.example.coupv2;

import android.os.Bundle;
import android.os.CountDownTimer;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.coupv2.utils.Const;

import java.util.List;
import java.util.Objects;

public class PlayActivity extends AppCompatActivity implements WebSocketListener{
    //has player order been determined (not using at current moment but might later)
//    boolean playerOrder = false;
    //timer
    private CountDownTimer countDownTimer;
    private TextView timerTextView;
    //game chat Views
    private LinearLayout layoutMessages1;
    private ScrollView scrollViewMessages;
    private ImageButton submitText;
    private EditText chatMessage;
    private ImageButton openChat;
    //views for when player is waiting
    ImageView waitingOverlay;
    ImageView deadOverLay;
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
    int totalCoins = 0;
//    @Override
    //keep websocket open from LobbyActivity
//@Override
//protected void onStart() {
//    super.onStart();
//    loadMessages();
//    Intent intent = getIntent();
//
//    // If it contains the extra data, process it
//    if (intent.hasExtra("json_data")) {
//
//        try {
//            String jsonData = intent.getStringExtra("json_data");
//            JSONObject jsonObject2 = new JSONObject(jsonData);
//            String jsonStr2 = jsonObject2.toString();
//            WebSocketManager.getInstance().sendMessage(jsonStr2);
//            Log.d("WebSocket", "Income sent");
//            // Use the jsonObject as needed
//        } catch (JSONException e) {
//            e.printStackTrace();
//            // Handle the error appropriately
//        }
//    }
//
//}
@Override
protected void onPause() {
    super.onPause();
    Log.d("ActivityLifecycle", "onPause called");
}
    @Override
    protected void onStop() {
        super.onStop();
        Log.d("ActivityLifecycle", "onStop called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("ActivityLifecycle", "onDestroy called");
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadMessages();
        //set listener to this class
        WebSocketManager.getInstance().setWebSocketListener(this);

        Log.d("ActivityLifecycle", "onResume called");
        // Get the intent that started this activity
        Intent intent = getIntent();
        if (intent.hasExtra("playerEmail") && intent.hasExtra("move") && intent.hasExtra("targetPlayer")) {
            Log.d("GameDebug", "We in ");
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

                // Use jsonObject.toString() to get JSON string representation
                String jsonString = jsonObject.toString();
                WebSocketManager.getInstance().sendMessage(jsonString);
                Log.d("Websocket", "MoveMade:" + mov);
                // Now you can use jsonString for your purposes, like sending it over a network

            } catch (JSONException e) {
                throw new RuntimeException(e);
//            e.printStackTrace();
            }


        }

//        JSONObject jsonObject = new JSONObject();
        //let backend know that player is ready to receive data
//        try {
//            jsonObject.put("playerEmail", Const.getCurrentEmail());
//            jsonObject.put("move", "ready");
//            jsonObject.put("targetPlayer", "null");
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        }
//        String jsonStr = jsonObject.toString();
//        WebSocketManager.getInstance().sendMessage(jsonStr);


        // Get the intent that started this activity
//        Intent intent = getIntent();
//
//        // If it contains the extra data, process it
//        if (intent.hasExtra("json_data")) {
//
//            try {
//                String jsonData = intent.getStringExtra("json_data");
//                JSONObject jsonObject2 = new JSONObject(jsonData);
//                String jsonStr2 = jsonObject2.toString();
//                WebSocketManager.getInstance().sendMessage(jsonStr2);
//                Log.d("WebSocket", "Income sent");
//                // Use the jsonObject as needed
//            } catch (JSONException e) {
//                e.printStackTrace();
//                // Handle the error appropriately
//            }
//        }
//


        Log.d("GameDebug", "Player State Resume: " + playerState);
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("ActivityLifecycle", "onStart called");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ActivityLifecycle", "onCreate called");
//        loadMessages();
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

        startTimer();
//        loadMessages();
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
        layoutMessages1 = findViewById(R.id.layoutMessages1);
        chatMessage =  findViewById(R.id.chatText);
        submitText =  findViewById(R.id.submitText);
        openChat = findViewById(R.id.imageButton2);
        //assign view for when player is waiting
        waitingOverlay = findViewById(R.id.gameBoard_wait);
        //assign view for when player is dead
        deadOverLay = findViewById(R.id.gameBoard_dead);
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
        //assign timer view
        timerTextView = findViewById(R.id.timerText);
        loadMessages();
        openChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                go to new activity when clicked on
                Intent intent = new Intent(PlayActivity.this, GameChatActivity.class);
                startActivity(intent);
            }
        });
//        Button testButton = findViewById(R.id.btn123);
//        testButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                ImageView playerIcon = findViewById(R.id.person3); // Replace with your actual ImageView ID.
////                // Apply the pulse animation.
////                Animation pulse = AnimationUtils.loadAnimation(PlayActivity.this, R.anim.pulse_animation);
////                playerIcon.startAnimation(pulse);
//                updatePlayerStateUi();
//            }
//        });
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
        else if (message.matches(".*: .*")) {
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
            String usernameMessage = message.substring(0, colonIndex).trim();
            String messageContent = message.substring(colonIndex + 1).trim().replaceAll("^'(.*)'$", "$1");

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Update UI components with the username and message
                    // ...
//                    addMessageToLayout(usernameMessage, messageContent);
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
//            JSONObject deck = jsonObject.getJSONObject("deck");
            JSONObject currentPlayer = game.getJSONObject("currentPlayer");
            // Read the player array
            final JSONArray playerArray = game.getJSONArray("playerArrayList");
            lastMoveMade = currentPlayer.getString("currentMove");  // Use optString to handle null cases

            // This will run on the UI thread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
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
                                //check lives of other 3 players to know if they are still alive(this is for action against other players)
//                                if (player.getInt("lives") == 0){
//                                    //dont need to check themselves
//                                    if (playerEmail.equals(Player2)){
//                                        Player2 = null;
//                                    }
//                                    else if (playerEmail.equals(Player3)){
//                                        Player3 = null;
//                                    }
//                                    else if (playerEmail.equals(Player4)){
//                                        Player4 = null;
//                                    }
//                                }
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
                                updatePlayerTurnUi(playerEmail);
                            }
//                            if (player.getBoolean("")) {
//                                updatePlayerTurnUi(playerEmail);
//                            }
                        }
                        // Call updatePlayerStateUi separately to update the UI based on the player state

                        updatePlayerStateUi();
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
//    public void onWebSocketClose(int code, String reason, boolean remote) {
//        Log.e("WebSocketListener", Player1 + " disconnected");
//    }
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

        // Do any additional cleanup or state updates needed on closure
        // ...
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
            //hide waiting and dead layout
            waitingOverlay.setVisibility(View.GONE);
            deadOverLay.setVisibility(View.GONE);
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
        //implement later
        else if(playerState.equals("challenge")){
            //hide waiting and dead layout
            waitingOverlay.setVisibility(View.GONE);
            deadOverLay.setVisibility(View.GONE);
            //show challenge mode layout
            bigBlock.setVisibility(View.VISIBLE);
            //hide block
            smallwhite1.setVisibility(View.GONE);
            smallwhite1Text.setVisibility(View.GONE);
            smallwhite2.setVisibility(View.VISIBLE);
            smallwhite2Text.setVisibility(View.VISIBLE);
            longwhite.setVisibility(View.VISIBLE);
            longwhitetext.setVisibility(View.VISIBLE);
            //disable game listeners if not turn
            gameBoard.setOnClickListener(null);
            //set contest listeners
            smallwhite1.setOnClickListener(null);
            smallwhite2.setOnClickListener(bluffButtonListener);
            longwhite.setOnClickListener(skipButtonListener);
        }
        else if (playerState.equals("dead")){  //hide contest layout
        bigBlock.setVisibility(View.GONE);
        smallwhite1.setVisibility(View.GONE);
        smallwhite1Text.setVisibility(View.GONE);
        smallwhite2.setVisibility(View.GONE);
        smallwhite2Text.setVisibility(View.GONE);
        longwhite.setVisibility(View.GONE);
        longwhitetext.setVisibility(View.GONE);
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
                                    jsonObject.put("move", "*Block Ambassador");
                                    jsonObject.put("targetPlayer", null);
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
                                    jsonObject.put("move", "*Block Captain");
                                    jsonObject.put("targetPlayer", null);
                                    Log.d("Websocket", "MoveMade: *Block Captain");
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
    //timer
    private void startTimer() {
        countDownTimer = new CountDownTimer(60000, 1000) { // 60 seconds with 1 second tick
            public void onTick(long millisUntilFinished) {
                timerTextView.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                timerTextView.setText("done!");
            }
        }.start();
    };
    private void loadMessages() {
        List<String[]> existingMessages = ChatManager.getInstance().getMessages();
        for (String[] messageData : existingMessages) {
            addMessageToLayout(messageData[0], messageData[1]);
        }
    };
//    private void showUserCards() {
//        if(card1 == "assassin"){
//
//        }
//        else if(card1 == "ambassador"){
//
//        }
//        else if(card1 == "captain"){
//
//        }
//        else if(card1 == "contra"){
//
//        }
//        else if(card1 == "duke"){
//
//        }
//    };

//    private void addCardToLayoutLeft(String cardName) {
////        R.layout.activity_play
//        ConstraintLayout layout = findViewById(R.id.centralGridArea); // Replace with your ConstraintLayout's ID
//        ImageView cardView = new ImageView(this);
//
//        // Set an ID to the ImageView
//        cardView.setId(View.generateViewId());
//        int drawableId = getResources().getIdentifier(cardName.toLowerCase(), "drawable", getPackageName());
//        // Set the image resource based on the card name
//        cardView.setImageResource(drawableId);
//
//        // Add ImageView to the ConstraintLayout
//        layout.addView(cardView, new ConstraintLayout.LayoutParams(
//                ConstraintLayout.LayoutParams.WRAP_CONTENT,
//                ConstraintLayout.LayoutParams.WRAP_CONTENT));
//
//        // Use ConstraintSet to apply constraints programmatically
//        ConstraintSet set = new ConstraintSet();
//        set.clone(layout);
//
//        // Copy constraints from greenIcon1 to cardView
//        set.connect(cardView.getId(), ConstraintSet.START, R.id.greenIcon1, ConstraintSet.START, 0);
//        set.connect(cardView.getId(), ConstraintSet.END, R.id.greenIcon1, ConstraintSet.END, 0);
//        set.connect(cardView.getId(), ConstraintSet.TOP, R.id.greenIcon1, ConstraintSet.TOP, 0);
//        set.connect(cardView.getId(), ConstraintSet.BOTTOM, R.id.greenIcon1, ConstraintSet.BOTTOM, 0);
//
//        // Apply the constraints to the layout
//        set.applyTo(layout);
//    }

    public void sendMessage(String username, String message) {
        // Add message to layout
        addMessageToLayout(username, message);
        // Save message to Singleton
        ChatManager.getInstance().addMessage(username, message);
    }

}

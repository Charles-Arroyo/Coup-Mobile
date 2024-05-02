
package com.example.coupv2;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.coupv2.app.AppController;
import com.example.coupv2.utils.Const;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class FriendsActivity extends AppCompatActivity implements WebSocketListener


{

    private EditText friendEmailEditText;
    private LinearLayout friendsLayout;
    private Button addFriendButton, exitButton, deleteFriendButton, refreshButton, requestButton;
    private RequestQueue requestQueue;
    private String userEmail;

    /*
    Server URLS

    -------------------------------
    PORT 8443
    private static final String URL_ADD_FRIEND = "http://coms-309-023.class.las.iastate.edu:8443/sendRequest/";
    private static final String URL_DELETE_FRIEND = "http://coms-309-023.class.las.iastate.edu:8443/deleteFriend/";
    private static final String URL_REFRESH_FRIENDS = "http://coms-309-023.class.las.iastate.edu:8443/getAcceptedFriends/";
    private static final String URL_CHECK_FRIEND_REQUESTS = "http://coms-309-023.class.las.iastate.edu:8443/gotFriendRequest/";
    private static final String URL_ACCEPT_REQUESTS = "http://coms-309-023.class.las.iastate.edu:8443/acceptFriendOrNot/true/";
    private static final String URL_DECLINE_REQUESTS = "http://coms-309-023.class.las.iastate.edu:8443/acceptFriendOrNot/false/";
    -------------------------------------------------------------------------------------------------------------------------------
    Port 8443
    private static final String URL_ADD_FRIEND = "http://coms-309-023.class.las.iastate.edu:8443/sendRequest/";
    private static final String URL_DELETE_FRIEND = "http://coms-309-023.class.las.iastate.edu:8443/deleteFriend/";
    private static final String URL_REFRESH_FRIENDS = "http://coms-309-023.class.las.iastate.edu:8443/getAcceptedFriends/";
    private static final String URL_CHECK_FRIEND_REQUESTS = "http://coms-309-023.class.las.iastate.edu:8443/gotFriendRequest/";
    private static final String URL_ACCEPT_REQUESTS = "http://coms-309-023.class.las.iastate.edu:8443/acceptFriendOrNot/true/";
    private static final String URL_DECLINE_REQUESTS = "http://coms-309-023.class.las.iastate.edu:8443/acceptFriendOrNot/false/";
    ---------------------------------------------------------------------------------------------------
    Mock URLS
    private static final String URL_ADD_FRIEND = "https://3a856af0-b6ac-48f3-a93a-06d2cd454e01.mock.pstmn.io/success";
    private static final String URL_DELETE_FRIEND = "https://3a856af0-b6ac-48f3-a93a-06d2cd454e01.mock.pstmn.io/success";
    private static final String URL_REFRESH_FRIENDS = "https://3a856af0-b6ac-48f3-a93a-06d2cd454e01.mock.pstmn.io/friendlist";
    private static final String URL_CHECK_FRIEND_REQUESTS = "https://3a856af0-b6ac-48f3-a93a-06d2cd454e01.mock.pstmn.io/friendrequest/";
    private static final String URL_ACCEPT_REQUESTS = "https://3a856af0-b6ac-48f3-a93a-06d2cd454e01.mock.pstmn.io/success";
    private static final String URL_DECLINE_REQUESTS = "https://3a856af0-b6ac-48f3-a93a-06d2cd454e01.mock.pstmn.io/success";
    ---------------------------------------------------------------------------------------------------
    Websockets

     */

    private static final String URL_ADD_FRIEND = "http://coms-309-023.class.las.iastate.edu:8443/sendRequest/";
    private static final String URL_DELETE_FRIEND = "http://coms-309-023.class.las.iastate.edu:8443/deleteFriend/";
    private static final String URL_REFRESH_FRIENDS = "http://coms-309-023.class.las.iastate.edu:8443/getAcceptedFriends/";
    private static final String URL_CHECK_FRIEND_REQUESTS = "http://coms-309-023.class.las.iastate.edu:8443/gotFriendRequest/";
    private static final String URL_ACCEPT_REQUESTS = "http://coms-309-023.class.las.iastate.edu:8443/acceptFriendOrNot/true/";
    private static final String URL_DECLINE_REQUESTS = "http://coms-309-023.class.las.iastate.edu:8443/acceptFriendOrNot/false/";

    /**
     * Method that runs and mostly intialize the functions in the menu
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Const.getCurrentTheme());


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        friendsLayout = findViewById(R.id.friendsLayout);

        userEmail = Const.getCurrentEmail();

        WebSocketManager2.getInstance().setWebSocketListener(this);

        WebSocketManager2.getInstance().sendMessage("getfriend");

        friendEmailEditText = findViewById(R.id.friend_email_edittext);
        friendsLayout = findViewById(R.id.friendsLayout);
        exitButton = findViewById(R.id.exit_btn);
        addFriendButton = findViewById(R.id.add_friend_btn);
        deleteFriendButton = findViewById(R.id.delete_friend_btn);
        refreshButton = findViewById(R.id.refresh_btn);
        requestButton = findViewById(R.id.request_btn);

        requestQueue = AppController.getInstance().getRequestQueue();

        exitButton.setOnClickListener(v ->{
            Intent intent = new Intent(FriendsActivity.this, MenuActivity.class);
            startActivity(intent);
        });


        addFriendButton.setOnClickListener(this::onAddFriendClick);
        deleteFriendButton.setOnClickListener(this::onDeleteFriendClick);

        requestButton.setOnClickListener(v -> {
            displayFriendRequestsPopup();
            WebSocketManager2.getInstance().sendMessage("getfriend");
        });

        refreshButton.setOnClickListener(v -> {
            displayFriendRequestsPopup();
            WebSocketManager2.getInstance().sendMessage("getfriend");

        });


        checkForFriendRequests();

    }

    /**
     * method to gather email to add friend
     *
     * @param view the layout to get the email from
     */

    public void onAddFriendClick(View view) {
        String friendEmail = friendEmailEditText.getText().toString();
        if (!friendEmail.isEmpty()) {
            performAddFriendRequest(friendEmail);
        } else {
            Toast.makeText(this, "Please enter a friend's email", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * POST request to add a friend in a json format
     *
     * @param friendEmail the user you want to add
     */

    private void performAddFriendRequest(String friendEmail) {

        String fullUrl = URL_ADD_FRIEND + userEmail + "/" + friendEmail;


        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("friendEmail1", userEmail);
            requestBody.put("friendEmail2", friendEmail);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, fullUrl, requestBody,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            Toast.makeText(FriendsActivity.this, "Friend added successfully", Toast.LENGTH_SHORT).show();
                            WebSocketManager2.getInstance().sendMessage("getfriend");

                        } else {
                            String errorMessage = response.getString("message");
                            Toast.makeText(FriendsActivity.this, "Failed to add friend: " + errorMessage, Toast.LENGTH_SHORT).show();
                            WebSocketManager2.getInstance().sendMessage("getfriend");

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(FriendsActivity.this, "Invalid response from server", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(FriendsActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show());

        requestQueue.add(jsonObjectRequest);
    }

    /**
     * Gets the users email to send to the delete method
     *
     * @param view layout to get the friends email
     */

    public void onDeleteFriendClick(View view) {
        String friendEmail = friendEmailEditText.getText().toString();
        if (!friendEmail.isEmpty()) {
            performDeleteFriendRequest(friendEmail);
            WebSocketManager2.getInstance().sendMessage("getfriend");

        } else {
            Toast.makeText(this, "Please enter a friend's email", Toast.LENGTH_SHORT).show();
            WebSocketManager2.getInstance().sendMessage("getfriend");

        }
    }

    /**
     * Method to delete the users selected friend in DELETE request in JSON
     *
     * @param friendEmail user email to delete
     */

    private void performDeleteFriendRequest(String friendEmail) {

        String deleteUrl = URL_DELETE_FRIEND + userEmail + "/" + friendEmail;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, deleteUrl, null,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            Toast.makeText(FriendsActivity.this, "Friend deleted successfully", Toast.LENGTH_SHORT).show();
                            WebSocketManager2.getInstance().sendMessage("getfriend");

                        } else {
                            String errorMessage = response.getString("message");
                            Toast.makeText(FriendsActivity.this, "Failed to delete friend: " + errorMessage, Toast.LENGTH_SHORT).show();
                            WebSocketManager2.getInstance().sendMessage("getfriend");

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(FriendsActivity.this, "Invalid response from server", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(FriendsActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show());

        requestQueue.add(jsonObjectRequest);
    }

    /**
     * Simple method to connnect view to refresh
     *
     * @param view layout to click to connect refresh
     */

    public void onRefreshClick(View view) {
//        performRefreshRequest();
        checkForFriendRequests();
        WebSocketManager2.getInstance().sendMessage("getfriend");

    }


    /**
     * Method to get users stats and displays them in a HTTP GET Request
     *
     * @param email
     */
    private void showUserStats(String email) {
        Toast.makeText(this, "Stats for: " + email, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(FriendsActivity.this, StatsActivity.class);
        intent.putExtra("USER", email);
        startActivity(intent);
    }

    /**
     *A method that connects user to a specific friends chat in the friend list
     *
     * @param email
     */
    private void startMessageActivity(String email) {
        Intent intent = new Intent(this, MessageActivity.class);
        intent.putExtra("friend", email);
        startActivity(intent);
    }

    /**
     * A GET request method to help you gather information those sending friend request in the
     * database
     */
    private void checkForFriendRequests() {
        String fullUrl = URL_CHECK_FRIEND_REQUESTS + userEmail;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, fullUrl, null,
                response -> {
                    try {
                        if (response.has("requests") && !response.isNull("requests")) {
                            JSONArray requestsArray = response.getJSONArray("requests");
                            if (requestsArray.length() > 0) {
                                requestButton.setVisibility(View.VISIBLE);
                                requestButton.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));
                            } else {
                                requestButton.setVisibility(View.INVISIBLE);
                                requestButton.getBackground().clearColorFilter();
                            }
                        } else {
                            requestButton.setVisibility(View.INVISIBLE);
                            requestButton.getBackground().clearColorFilter();
                            Toast.makeText(FriendsActivity.this, "No friend requests", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(FriendsActivity.this, "Invalid response from server", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(FriendsActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show());

        requestQueue.add(jsonObjectRequest);
    }

    /**
     * popup method to show the list of friends, parsing and dynamically adding them to scroll view
     */

    private void displayFriendRequestsPopup() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.popup_friend_request, null);
        bottomSheetDialog.setContentView(view);

        LinearLayout requestsLayout = view.findViewById(R.id.friend_requests_layout);

        String fullUrl = URL_CHECK_FRIEND_REQUESTS + userEmail;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, fullUrl, null,
                response -> {
                    try {
                        if (response.has("requests") && !response.isNull("requests")) {
                            JSONArray requestsArray = response.getJSONArray("requests");
                            for (int i = 0; i < requestsArray.length(); i++) {
                                JSONObject request = requestsArray.getJSONObject(i);
                                String email = request.getString("email");
                                View friendRequestView = getLayoutInflater().inflate(R.layout.friend_request_item, null);

                                TextView emailTextView = friendRequestView.findViewById(R.id.friend_request_email);
                                Button acceptButton = friendRequestView.findViewById(R.id.button_accept);
                                Button denyButton = friendRequestView.findViewById(R.id.button_deny);

                                emailTextView.setText(email);

                                acceptButton.setOnClickListener(v -> {
                                    acceptFriendRequest(email);
                                    bottomSheetDialog.dismiss();
                                });

                                denyButton.setOnClickListener(v -> {
                                    denyFriendRequest(email);
                                    bottomSheetDialog.dismiss();
                                });

                                requestsLayout.addView(friendRequestView);
                            }
                        } else {
                            Toast.makeText(FriendsActivity.this, "No friend requests to display", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(FriendsActivity.this, "Error parsing friend requests", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(FriendsActivity.this, "Error fetching friend requests: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        requestQueue.add(jsonObjectRequest);
        bottomSheetDialog.show();
    }

    /**
     * Method to accept friend request from popup
     *
     * @param friendEmail
     */

    private void acceptFriendRequest(final String friendEmail) {
        String fullUrl = URL_ACCEPT_REQUESTS + userEmail + "/" + friendEmail ;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, fullUrl, null,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            Toast.makeText(FriendsActivity.this, "Friend request accepted: " + friendEmail, Toast.LENGTH_SHORT).show();
                            WebSocketManager2.getInstance().sendMessage("getfriend");

                            checkForFriendRequests();
                        } else {
                            Toast.makeText(FriendsActivity.this, "Friend request acceptance failed.", Toast.LENGTH_SHORT).show();
                            WebSocketManager2.getInstance().sendMessage("getfriend");

                        }
                    } catch (JSONException e) {
                        Toast.makeText(FriendsActivity.this, "Error parsing success response.", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e("AcceptFriendRequest", "Error accepting friend request", error);
                    if (error.networkResponse != null) {
                        String errorMessage = new String(error.networkResponse.data);
                        Toast.makeText(FriendsActivity.this, "Error accepting friend request: " + errorMessage, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(FriendsActivity.this, "Error accepting friend request: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    /**
     * Method to deny friend request
     *
     * @param friendEmail
     */

    private void denyFriendRequest(final String friendEmail) {

        String fullUrl = URL_DECLINE_REQUESTS + userEmail + "/" + friendEmail ;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, fullUrl, null,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            Toast.makeText(FriendsActivity.this, "Friend request denied: " + friendEmail, Toast.LENGTH_SHORT).show();
                            checkForFriendRequests();
                        } else {
                            Toast.makeText(FriendsActivity.this, "Friend request denial failed.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(FriendsActivity.this, "Error parsing success response.", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e("DenyFriendRequest", "Error denying friend request", error);
                    if (error.networkResponse != null) {
                        String errorMessage = new String(error.networkResponse.data);
                        Toast.makeText(FriendsActivity.this, "Error denying friend request: " + errorMessage, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(FriendsActivity.this, "Error denying friend request: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    /**
     * Method to disconnect when server breaks in the Web Socket
     */

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WebSocketManager2.getInstance().removeWebSocketListener();
    }

    /**
     * Websocket method to connect the user
     *
     * @param handshakedata Information about the server handshake.
     */

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
        Log.d("FriendsActivity", "WebSocket connected");
    }

    /**
     * Method to write to the websocket
     *
     * @param message The received WebSocket message.
     */

    @Override
    public void onWebSocketMessage(String message) {
        Log.d("FriendsActivity", "Message received: " + message);
        runOnUiThread(() -> updateFriendList(message));
    }

    /**
     * Websocket method to close connection from user to server or vice versa
     *
     * @param code   The status code indicating the reason for closure.
     * @param reason A human-readable explanation for the closure.
     * @param remote Indicates whether the closure was initiated by the remote endpoint.
     */

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        Log.d("FriendsActivity", "WebSocket closed");

    }

    /**
     * Websocket method that sends error when something happens
     *
     * @param ex The exception that describes the error.
     */

    @Override
    public void onWebSocketError(Exception ex) {
        Log.d("FriendsActivity", "WebSocket error: " + ex.getMessage());

    }

    /**
     * Websocket method to reconnect the user when leaving activity
     */

    @Override
    protected void onResume() {
        super.onResume();
        WebSocketManager2.getInstance().setWebSocketListener(this);
    }

    /**
     * Websocket method to pause websocket connection when leaving activity
     */

    @Override
    protected void onPause() {
        super.onPause();
        WebSocketManager2.getInstance().removeWebSocketListener();
    }

    /**
     * Update friend list to connnect with a websocket,
     * Recieve a JSON object to parse through it in the code
     *
     * @param message
     */

    private void updateFriendList(String message) {
        try {
            JSONObject jsonObject = new JSONObject(message);
            JSONArray friendsArray = jsonObject.getJSONArray("friends");
            friendsLayout.removeAllViews();

            for (int i = 0; i < friendsArray.length(); i++) {
                JSONObject friend = friendsArray.getJSONObject(i);
                String email = friend.getString("email");
                String isActive = friend.getString("active");

                View friendView = getLayoutInflater().inflate(R.layout.friend_item, friendsLayout, false);
                Button emailButton = friendView.findViewById(R.id.email);
                ImageButton activeButton = friendView.findViewById(R.id.active);

                emailButton.setText(email);
                emailButton.setOnClickListener(v -> showUserStats(email));

                ImageButton messageButton = friendView.findViewById(R.id.msgButton);
                messageButton.setOnClickListener(v -> startMessageActivity(email));

                emailButton.setText(email);

                //animation
                Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.spinning);
                activeButton.startAnimation(rotateAnimation);

                if (isActive.equals("true")) {
                    activeButton.setBackground(ContextCompat.getDrawable(this, R.drawable.online_icon));
                }else  if (isActive.equals("false")) {
                    activeButton.setBackground(ContextCompat.getDrawable(this, R.drawable.offline_icon));

                }

                friendsLayout.addView(friendView);
            }
        } catch (JSONException e) {
            Log.e("FriendsActivity", "Error parsing friend list", e);
            Toast.makeText(this, "Error parsing friend list", Toast.LENGTH_SHORT).show();
        }
    }

}

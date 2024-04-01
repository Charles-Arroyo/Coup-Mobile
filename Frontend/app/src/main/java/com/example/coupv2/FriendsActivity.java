package com.example.coupv2;

import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.coupv2.app.AppController;
import com.example.coupv2.utils.Const;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FriendsActivity extends AppCompatActivity {

    private EditText friendEmailEditText;
    private LinearLayout friendsLayout; // Use LinearLayout for dynamic view addition
    private Button addFriendButton, exitButton, deleteFriendButton, refreshButton, requestButton;
    private RequestQueue requestQueue;
    private String userEmail;

    /*
    Server URLS

        private static final String URL_ADD_FRIEND = "http://coms-309-023.class.las.iastate.edu:8080/sendRequest/";
        private static final String URL_DELETE_FRIEND = "http://coms-309-023.class.las.iastate.edu:8080/deleteFriend/";
        private static final String URL_REFRESH_FRIENDS = "http://coms-309-023.class.las.iastate.edu:8080/getAcceptedFriends/";
        private static final String URL_CHECK_FRIEND_REQUESTS = "http://coms-309-023.class.las.iastate.edu:8080/gotFriendRequest/";
        private static final String URL_ACCEPT_REQUESTS = "http://coms-309-023.class.las.iastate.edu:8080/acceptFriendOrNot/true/";
        private static final String URL_DECLINE_REQUESTS = "http://coms-309-023.class.las.iastate.edu:8080/acceptFriendOrNot/false/";

    -------------------------------------------------------------------------------------------------------------------------------
    Mock URLS

        private static final String URL_ADD_FRIEND = "https://3a856af0-b6ac-48f3-a93a-06d2cd454e01.mock.pstmn.io/success";
        private static final String URL_DELETE_FRIEND = "https://3a856af0-b6ac-48f3-a93a-06d2cd454e01.mock.pstmn.io/success";
        private static final String URL_REFRESH_FRIENDS = "https://3a856af0-b6ac-48f3-a93a-06d2cd454e01.mock.pstmn.io/friendlist";
        private static final String URL_CHECK_FRIEND_REQUESTS = "https://3a856af0-b6ac-48f3-a93a-06d2cd454e01.mock.pstmn.io/friendrequest/";
        private static final String URL_ACCEPT_REQUESTS = "https://3a856af0-b6ac-48f3-a93a-06d2cd454e01.mock.pstmn.io/success";
        private static final String URL_DECLINE_REQUESTS = "https://3a856af0-b6ac-48f3-a93a-06d2cd454e01.mock.pstmn.io/success";
     */

    private static final String URL_ADD_FRIEND = "http://coms-309-023.class.las.iastate.edu:8080/sendRequest/";
    private static final String URL_DELETE_FRIEND = "http://coms-309-023.class.las.iastate.edu:8080/deleteFriend/";
    private static final String URL_REFRESH_FRIENDS = "http://coms-309-023.class.las.iastate.edu:8080/getAcceptedFriends/";
    private static final String URL_CHECK_FRIEND_REQUESTS = "http://coms-309-023.class.las.iastate.edu:8080/gotFriendRequest/";
    private static final String URL_ACCEPT_REQUESTS = "http://coms-309-023.class.las.iastate.edu:8080/acceptFriendOrNot/true/";
    private static final String URL_DECLINE_REQUESTS = "http://coms-309-023.class.las.iastate.edu:8080/acceptFriendOrNot/false/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        //Setting variables

        friendEmailEditText = findViewById(R.id.friend_email_edittext);
        friendsLayout = findViewById(R.id.friendsLayout);
        exitButton = findViewById(R.id.exit_btn);
        addFriendButton = findViewById(R.id.add_friend_btn);
        deleteFriendButton = findViewById(R.id.delete_friend_btn);
        refreshButton = findViewById(R.id.refresh_btn);
        requestButton = findViewById(R.id.request_btn);

        userEmail = Const.getCurrentEmail();
        requestQueue = AppController.getInstance().getRequestQueue();

        exitButton.setOnClickListener(v -> onBackPressed());

        //Listeners

        addFriendButton.setOnClickListener(this::onAddFriendClick);
        deleteFriendButton.setOnClickListener(this::onDeleteFriendClick);
        refreshButton.setOnClickListener(v -> performRefreshRequest());
        requestButton.setOnClickListener(v -> displayFriendRequestsPopup());

        checkForFriendRequests();

        performRefreshRequest();
    }

    public void onAddFriendClick(View view) {
        String friendEmail = friendEmailEditText.getText().toString();
        if (!friendEmail.isEmpty()) {
            performAddFriendRequest(friendEmail);
        } else {
            Toast.makeText(this, "Please enter a friend's email", Toast.LENGTH_SHORT).show();
        }
    }

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
                            performRefreshRequest();
                        } else {
                            String errorMessage = response.getString("message");
                            Toast.makeText(FriendsActivity.this, "Failed to add friend: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(FriendsActivity.this, "Invalid response from server", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(FriendsActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show());

        requestQueue.add(jsonObjectRequest);
    }

    public void onDeleteFriendClick(View view) {
        String friendEmail = friendEmailEditText.getText().toString();
        if (!friendEmail.isEmpty()) {
            performDeleteFriendRequest(friendEmail);
        } else {
            Toast.makeText(this, "Please enter a friend's email", Toast.LENGTH_SHORT).show();
        }
    }

    private void performDeleteFriendRequest(String friendEmail) {

        String deleteUrl = URL_DELETE_FRIEND + userEmail + "/" + friendEmail;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, deleteUrl, null,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            Toast.makeText(FriendsActivity.this, "Friend deleted successfully", Toast.LENGTH_SHORT).show();
                            performRefreshRequest();
                        } else {
                            String errorMessage = response.getString("message");
                            Toast.makeText(FriendsActivity.this, "Failed to delete friend: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(FriendsActivity.this, "Invalid response from server", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(FriendsActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show());

        requestQueue.add(jsonObjectRequest);
    }

    public void onRefreshClick(View view) {
        performRefreshRequest();
        checkForFriendRequests();
    }

    private void performRefreshRequest() {
        String fullUrl = URL_REFRESH_FRIENDS + userEmail;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, fullUrl, null,
                response -> {
                     friendsLayout.removeAllViews();

                    try {
                        JSONArray friendsArray = response.optJSONArray("friend");

                         if (friendsArray == null || friendsArray.length() == 0) {
                            Toast.makeText(FriendsActivity.this, "No friends found.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                         for (int i = 0; i < friendsArray.length(); i++) {
                            JSONObject friend = friendsArray.getJSONObject(i);
<<<<<<< HEAD
<<<<<<< HEAD
                            String email = friend.getString("friendEmail2"); // Use "friendEmail2" to get the email
//                            boolean isActive = friend.getBoolean("active"); // Get active status
=======
                            String email = friend.optString("friendEmail2", "No email");
>>>>>>> active_friends
=======
                            String email = friend.optString("friendEmail2", "No email");
>>>>>>> aa8e7d3d0459e4ee174bf61f3c1e3b9b1b6f0e5d

                            View friendView = getLayoutInflater().inflate(R.layout.friend_item, friendsLayout, false);
                            Button emailButton = friendView.findViewById(R.id.email);
                            emailButton.setText(email);
                            emailButton.setOnClickListener(v -> showUserStats(email));

<<<<<<< HEAD
<<<<<<< HEAD
                            View activeButton = friendView.findViewById(R.id.active);
//                            activeButton.setBackgroundTintList(ColorStateList.valueOf(isActive ? Color.GREEN : Color.RED));

                            // Set onClickListener for the activeButton to show a toast
                            activeButton.setOnClickListener(v -> {
//                                String statusMessage = isActive ? "Online" : "Offline";
//                                Toast.makeText(FriendsActivity.this, email + " is " + statusMessage, Toast.LENGTH_SHORT).show();
                            });


=======
>>>>>>> active_friends
=======
>>>>>>> aa8e7d3d0459e4ee174bf61f3c1e3b9b1b6f0e5d
                            ImageButton messageButton = friendView.findViewById(R.id.msgButton);
                            messageButton.setOnClickListener(v -> startMessageActivity(email));

                            friendsLayout.addView(friendView);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(FriendsActivity.this, "Error parsing friend list", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                     friendsLayout.removeAllViews();
                    Toast.makeText(FriendsActivity.this, "Error fetching friend list: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        requestQueue.add(jsonObjectRequest);
    }

    private void showUserStats(String email) {
        Toast.makeText(this, "Stats for: " + email, Toast.LENGTH_SHORT).show();
        // Here you could launch an activity or a dialog showing stats for the user
    }

    private void startMessageActivity(String email) {
        Intent intent = new Intent(this, MessageActivity.class);
        intent.putExtra("friend", email);
        startActivity(intent);
    }

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

    private void acceptFriendRequest(final String friendEmail) {
        String fullUrl = URL_ACCEPT_REQUESTS + friendEmail + "/" + userEmail;

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("friendEmail1", userEmail);
            jsonRequest.put("friendEmail2", friendEmail);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, fullUrl, jsonRequest,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            Toast.makeText(FriendsActivity.this, "Friend request accepted: " + friendEmail, Toast.LENGTH_SHORT).show();
                            performRefreshRequest();
                            checkForFriendRequests();
                        } else {
                            // Handle case where success is false
                            Toast.makeText(FriendsActivity.this, "Friend request acceptance failed.", Toast.LENGTH_SHORT).show();
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

    private void denyFriendRequest(final String friendEmail) {

        String fullUrl = URL_DECLINE_REQUESTS + friendEmail + "/" + userEmail;

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("friendEmail1", userEmail);
            jsonRequest.put("friendEmail2", friendEmail);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, fullUrl, jsonRequest,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            Toast.makeText(FriendsActivity.this, "Friend request denied: " + friendEmail, Toast.LENGTH_SHORT).show();
                            checkForFriendRequests();
                        } else {
                            // Handle case where success is false
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

}

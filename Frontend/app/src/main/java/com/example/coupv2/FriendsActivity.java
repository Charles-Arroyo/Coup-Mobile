package com.example.coupv2;

import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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

    // Mock URLs
    private static final String URL_ADD_FRIEND = "https://3a856af0-b6ac-48f3-a93a-06d2cd454e01.mock.pstmn.io/success";
    private static final String URL_DELETE_FRIEND = "https://3a856af0-b6ac-48f3-a93a-06d2cd454e01.mock.pstmn.io/success";
    private static final String URL_REFRESH_FRIENDS = "https://3a856af0-b6ac-48f3-a93a-06d2cd454e01.mock.pstmn.io/friendlist";
    private static final String URL_CHECK_FRIEND_REQUESTS = "https://3a856af0-b6ac-48f3-a93a-06d2cd454e01.mock.pstmn.io/friendrequest";
    private static final String URL_ACCEPT_REQUESTS = "https://3a856af0-b6ac-48f3-a93a-06d2cd454e01.mock.pstmn.io/success";
    private static final String URL_DECLINE_REQUESTS = "https://3a856af0-b6ac-48f3-a93a-06d2cd454e01.mock.pstmn.io/success";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        friendEmailEditText = findViewById(R.id.friend_email_edittext);
        friendsLayout = findViewById(R.id.friendsLayout);
        exitButton = findViewById(R.id.exit_btn);
        addFriendButton = findViewById(R.id.add_friend_btn);
        deleteFriendButton = findViewById(R.id.delete_friend_btn);
        refreshButton = findViewById(R.id.refresh_btn);
        requestButton = findViewById(R.id.request_btn);

        userEmail = Const.getCurrentEmail();
        requestQueue = AppController.getInstance().getRequestQueue();

        exitButton.setOnClickListener(v -> finish());

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
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("friendEmail1", userEmail);
            requestBody.put("friendEmail2", friendEmail);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL_ADD_FRIEND, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
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
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FriendsActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

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
    }


    private void performRefreshRequest() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL_REFRESH_FRIENDS, null,
                response -> {
                    try {
                        JSONArray friendsArray = response.getJSONArray("friend");
                        friendsLayout.removeAllViews(); // Clear existing views
                        for (int i = 0; i < friendsArray.length(); i++) {
                            JSONObject friend = friendsArray.getJSONObject(i);
                            String email = friend.getString("friendEmail2");
                            View friendView = getLayoutInflater().inflate(R.layout.friend_item, friendsLayout, false);

                            TextView emailTextView = friendView.findViewById(R.id.friend_request_email);
                            emailTextView.setText(email);

                            ImageButton messageButton = friendView.findViewById(R.id.msgButton);
                            messageButton.setOnClickListener(v -> {
                                // Intent to start chat activity, or whatever action you want
                            });

                            friendsLayout.addView(friendView);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(FriendsActivity.this, "Error parsing friend list", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(FriendsActivity.this, "Error fetching friend list: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        requestQueue.add(jsonObjectRequest);
    }



    private void checkForFriendRequests() {
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL_CHECK_FRIEND_REQUESTS + "/" + userEmail, null,
        //Postman Test
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL_CHECK_FRIEND_REQUESTS, null,

                response -> {
                    try {
                        JSONArray requestsArray = response.getJSONArray("requests");
                        if (requestsArray.length() > 0) {
                            // There are friend requests, make the button "glow"
                            requestButton.setVisibility(View.VISIBLE);
                            requestButton.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));
                            // You could also change the text color or set a badge here
                        } else {
                            // No friend requests, set button to default appearance
                            requestButton.setVisibility(View.INVISIBLE);
                            requestButton.getBackground().clearColorFilter();
                            // Reset text color or hide badge if you added one earlier
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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL_CHECK_FRIEND_REQUESTS, null,
                response -> {
                    try {
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

    private void acceptFriendRequest(final String email) {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("requesterEmail", email); // Assuming the email of the person who sent the request
            requestBody.put("accepterEmail", userEmail); // Your own email
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL_ACCEPT_REQUESTS, requestBody,
                response -> {
                    Toast.makeText(FriendsActivity.this, "Friend request accepted: " + email, Toast.LENGTH_SHORT).show();
                    // Update the UI or refresh the data as needed
                },
                error -> Toast.makeText(FriendsActivity.this, "Error accepting friend request: " + error.getMessage(), Toast.LENGTH_SHORT).show());

        requestQueue.add(jsonObjectRequest);
    }


    private void denyFriendRequest(final String email) {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("requesterEmail", email); // Assuming the email of the person who sent the request
            requestBody.put("denierEmail", userEmail); // Your own email
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL_DECLINE_REQUESTS, requestBody,
                response -> {
                    Toast.makeText(FriendsActivity.this, "Friend request denied: " + email, Toast.LENGTH_SHORT).show();
                    // Update the UI or refresh the data as needed
                },
                error -> Toast.makeText(FriendsActivity.this, "Error denying friend request: " + error.getMessage(), Toast.LENGTH_SHORT).show());

        requestQueue.add(jsonObjectRequest);
    }

}

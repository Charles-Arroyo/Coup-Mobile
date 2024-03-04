package com.example.coupv2;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.coupv2.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FriendsActivity extends AppCompatActivity {

    // Private static URL variables

    private static final String URL_ADD_FRIEND = "https://343ca1be-668d-45a0-ab82-92792e2021a3.mock.pstmn.io?fail=";
    private static final String URL_DELETE_FRIEND = "https://343ca1be-668d-45a0-ab82-92792e2021a3.mock.pstmn.io?deleteF=";
    private static final String URL_REFRESH_FRIENDS = "https://343ca1be-668d-45a0-ab82-92792e2021a3.mock.pstmn.io?list2";

    private EditText friendEmailEditText; // Changed from friendNumberEditText
    private ListView friendsListView;
    private Button addFriendButton;
    private Button deleteFriendButton;
    private Button refreshButton;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        // Initialize views
        friendEmailEditText = findViewById(R.id.friend_email_edittext); // Updated ID
        friendsListView = findViewById(R.id.friends_list);
        addFriendButton = findViewById(R.id.add_friend_btn);
        deleteFriendButton = findViewById(R.id.delete_friend_btn);
        refreshButton = findViewById(R.id.refresh_btn);

        // Initialize the RequestQueue
        requestQueue = AppController.getInstance().getRequestQueue();

        // Set click listeners
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddFriendClick(v);
            }
        });

        deleteFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteFriendClick(v);
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefreshClick(v);
            }
        });
    }

    // Method to handle "Add Friend" button click
    public void onAddFriendClick(View view) {
        String friendEmail = friendEmailEditText.getText().toString(); // Changed from friendNumber
        if (!friendEmail.isEmpty()) {
            performAddFriendRequest(friendEmail);
        } else {
            Toast.makeText(this, "Please enter a friend's email", Toast.LENGTH_SHORT).show(); // Updated message
        }
    }

    // Method to perform POST request to add a friend
    private void performAddFriendRequest(String friendEmail) {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("friendEmail", friendEmail);
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

    // Method to handle "Delete Friend" button click
    public void onDeleteFriendClick(View view) {
        String friendEmail = friendEmailEditText.getText().toString(); // Changed from friendNumber
        if (!friendEmail.isEmpty()) {
            performDeleteFriendRequest(friendEmail);
        } else {
            Toast.makeText(this, "Please enter a friend's email", Toast.LENGTH_SHORT).show(); // Updated message
        }
    }
    // Method to perform DELETE request to delete a friend
    private void performDeleteFriendRequest(String friendEmail) {
        String deleteUrl = URL_DELETE_FRIEND + "?friendEmail=" + friendEmail;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, deleteUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
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

    // Method to handle "Refresh" button click
    public void onRefreshClick(View view) {
        Toast.makeText(this, "Refreshing friend list", Toast.LENGTH_SHORT).show();
        performRefreshRequest();
    }

    // Method to perform GET request to refresh the friend list
    private void performRefreshRequest() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL_REFRESH_FRIENDS, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray friendsArray = response.getJSONArray("friends");
                            List<String> formattedFriendsList = new ArrayList<>();
                            for (int i = 0; i < friendsArray.length(); i++) {
                                JSONObject friend = friendsArray.getJSONObject(i);
                                int id = friend.getInt("id");
                                String name = friend.getString("name");
                                String formattedFriend = "ID: " + id + "\t\t\tName: " + name;
                                formattedFriendsList.add(formattedFriend);
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(FriendsActivity.this, android.R.layout.simple_list_item_1, formattedFriendsList);
                            friendsListView.setAdapter(adapter);
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

}

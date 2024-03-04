package com.example.coupv2;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.ParseError;
import com.example.coupv2.utils.Const;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.coupv2.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class SettingActivity extends AppCompatActivity {
    //variables
    private EditText userNameText;
    private Button updateUser;


    private static final String URL_JSON_OBJECT = "http://coms-309-023.class.las.iastate.edu:8080/changeEmail/24";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings); // link to Login activity XML
        userNameText = findViewById(R.id.settings_username_edt);
        updateUser = findViewById(R.id.settings_login_btn);
        updateUser.setOnClickListener(v -> {
            String username = userNameText.getText().toString();
            if (!username.isEmpty()) {
                //this fetches id and updates it
                fetchPrimaryKeyForEmail(username);
            } else {
                Toast.makeText(SettingActivity.this, "Please enter a username", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void fetchPrimaryKeyForEmail(String email) {
        String url = "http://yourbackend.com/api/getUserPrimaryKey?email=";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        int primaryKey = response.getInt("primaryKey");
                        // Now that you have the primary key, you can update the user settings
                        updateUserSettings(primaryKey, email);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(SettingActivity.this, "Error parsing primary key", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // Handle error
                    Log.e("SettingActivity", "Error fetching primary key: " + error.toString());
                    Toast.makeText(SettingActivity.this, "Error fetching primary key", Toast.LENGTH_SHORT).show();
                }
        );

        RequestQueue requestQueue = AppController.getInstance().getRequestQueue();
        requestQueue.add(jsonObjectRequest);

    }

    private void updateUserSettings(int primaryKey, String userEmail) {
        String url = "http://coms-309-023.class.las.iastate.edu:8080/changeEmail/" + primaryKey;
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("updateEmail", userEmail); // newEmail is the updated email provided by the user
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(SettingActivity.this, "Error creating update request", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue requestQueue = AppController.getInstance().getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, URL_JSON_OBJECT, jsonRequest,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            Toast.makeText(SettingActivity.this, "Settings updated successfully", Toast.LENGTH_SHORT).show();
                            // Start MenuActivity since the update was successful
                            Intent menuIntent = new Intent(SettingActivity.this, MenuActivity.class);
                            startActivity(menuIntent);
                            finish(); // Close the current activity
                        } else {
                            Toast.makeText(SettingActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(SettingActivity.this, "Invalid response from server", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    if (error.networkResponse != null) {
                        String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                        Log.e("SettingActivity", "Network Response Body: " + responseBody);
                        try {
                            new JSONObject(responseBody); // Attempt to parse the response body as JSON
                            // Handle the JSON object or array here
                        } catch (JSONException e) {
                            // The response is not in JSON format
                            Log.e("SettingActivity", "Response is not valid JSON", e);
                        }
                    } else {
                        Log.e("SettingActivity", "Network Error: " + error.toString());
                    }
                    Toast.makeText(SettingActivity.this, "Network Error: " + error.toString(), Toast.LENGTH_LONG).show();
                }

        );

        requestQueue.add(jsonObjectRequest);
    }

}

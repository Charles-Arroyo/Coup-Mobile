package com.example.coupv2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.coupv2.app.AppController;
import com.example.coupv2.utils.Const;
import org.json.JSONException;
import org.json.JSONObject;
import java.nio.charset.StandardCharsets;

public class SettingActivity extends AppCompatActivity {
    //variables
    private EditText userNameText;
    private EditText userPassText;
    private Button updateUser;
    private Button updatePass;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings); // link to Login activity XML
        userNameText = findViewById(R.id.settings_username_edt);
        userPassText = findViewById(R.id.settings_password_edt);
        updateUser = findViewById(R.id.settings_login_btn);
        updatePass = findViewById(R.id.settings_pass_btn);
        backButton = findViewById(R.id.back_button);

        updateUser.setOnClickListener(v -> {
            String username = userNameText.getText().toString();
            String currentUser = Const.getCurrentEmail();
            if (!username.isEmpty()) {
                //this fetches id and updates it
                fetchPrimaryKeyForEmail(username, currentUser);
            } else {
                Toast.makeText(SettingActivity.this, "Please enter a username", Toast.LENGTH_SHORT).show();
            }
        });
        updatePass.setOnClickListener(v -> {
            String password = userPassText.getText().toString();
            String currentUser = Const.getCurrentEmail();
            if (!password.isEmpty()) {
                fetchPrimaryKeyForPassword(password, currentUser);
            } else {
                Toast.makeText(SettingActivity.this, "Please enter a password", Toast.LENGTH_SHORT).show();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the rules activity
                Intent intent = new Intent(SettingActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });
    }

    private void fetchPrimaryKeyForEmail(String emailToChange, String firstEmail) {
        String url = "http://coms-309-023.class.las.iastate.edu:8080/getId/" + Uri.encode(firstEmail);

        // Create a request for a response that expects a raw string
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            int primaryKey = Integer.parseInt(response); // Convert the response to an integer
                            updateUserSettings(primaryKey, emailToChange);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            Toast.makeText(SettingActivity.this, "Error parsing primary key", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("SettingActivity", "Error fetching primary key: " + error.toString());
                        Toast.makeText(SettingActivity.this, "Error fetching primary key", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Use AppController to add the request to the queue
        RequestQueue requestQueue = AppController.getInstance().getRequestQueue();
        requestQueue.add(stringRequest);
    }

    private void fetchPrimaryKeyForPassword(String passwordToChange, String firstEmail) {
//        String url = "http://coms-309-023.class.las.iastate.edu:8080/getId/" + Uri.encode(firstEmail) + "?password=" + Uri.encode(passwordToChange);
        String url = "http://coms-309-023.class.las.iastate.edu:8080/getId/" + Uri.encode(firstEmail);

        // Create a request for a response that expects a raw string
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            int primaryKey = Integer.parseInt(response); // Convert the response to an integer
                            updateUserPassword(primaryKey, passwordToChange);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            Toast.makeText(SettingActivity.this, "Error parsing primary key", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("SettingActivity", "Error fetching primary key: " + error.toString());
                        Toast.makeText(SettingActivity.this, "Error fetching primary key", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Use AppController to add the request to the queue
        RequestQueue requestQueue = AppController.getInstance().getRequestQueue();
        requestQueue.add(stringRequest);
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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonRequest,
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

    private void updateUserPassword(int primaryKey, String userPassword) {
        String url = "http://coms-309-023.class.las.iastate.edu:8080/changePass/" + primaryKey;

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("updatePassword", userPassword); // newPassword is the updated password provided by the user
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(SettingActivity.this, "Error creating update request", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue requestQueue = AppController.getInstance().getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonRequest,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            Toast.makeText(SettingActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
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

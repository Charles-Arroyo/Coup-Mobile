package com.example.coupv2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.coupv2.app.AppController;
import com.example.coupv2.utils.Const;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class SettingActivity extends AppCompatActivity {
    //variables
    private EditText userNameText, userPassText, userEmailText;
    private Button updateUser, updatePass, updateEmail, backButton;
    private ImageButton uploadImg;
    private ImageView settingsPicture;
    private String USER_EMAIL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Const.getCurrentTheme());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        userNameText = findViewById(R.id.settings_username_edt);
        userPassText = findViewById(R.id.settings_password_edt);
        updateUser = findViewById(R.id.settings_login_btn);
        updatePass = findViewById(R.id.settings_pass_btn);
        backButton = findViewById(R.id.backBtn);
        USER_EMAIL = Const.getCurrentEmail();
        userEmailText = findViewById(R.id.settings_email_edt);
        updateEmail = findViewById(R.id.settings_email_btn);

        updateUser.setOnClickListener(v -> {
            String username = userNameText.getText().toString();
            if (!username.isEmpty()) {
                updateUserSettings(username);
            } else {
                Toast.makeText(SettingActivity.this, "Please enter a username", Toast.LENGTH_SHORT).show();
            }
        });

        updatePass.setOnClickListener(v -> {
            String password = userPassText.getText().toString();
            if (!password.isEmpty()) {
                updateUserPassword(password);
            } else {
                Toast.makeText(SettingActivity.this, "Please enter a password", Toast.LENGTH_SHORT).show();
            }
        });

        updateEmail.setOnClickListener(v -> {
            String email = userEmailText.getText().toString();
            if (!email.isEmpty()) {
                updateUserEmail(email);
            } else {
                Toast.makeText(SettingActivity.this, "Please enter a password", Toast.LENGTH_SHORT).show();
            }
        });

        backButton.setOnClickListener(v -> {
            // Start the rules activity
            Intent intent = new Intent(SettingActivity.this, MenuActivity.class);
            startActivity(intent);
        });
    }


    private void updateUserSettings(String username) {
        String url = "http://coms-309-023.class.las.iastate.edu:8443/changeName/" + USER_EMAIL;

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("name", username); // newEmail is the updated email provided by the user
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
                            Intent menuIntent = new Intent(SettingActivity.this, MenuActivity.class);
                            startActivity(menuIntent);
                            Const.setCurrentEmail(username);
                            finish();
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
                            new JSONObject(responseBody);
                        } catch (JSONException e) {
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

    private void updateUserPassword(String userPassword) {
        String url = "http://coms-309-023.class.las.iastate.edu:8443/changePass/" + USER_EMAIL;

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("password", userPassword);
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

    private void updateUserEmail(String email) {
        String url = "http://coms-309-023.class.las.iastate.edu:8443/changeEmail/" + USER_EMAIL;

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("userEmail", email); // newPassword is the updated password provided by the user
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
                            Toast.makeText(SettingActivity.this, "email updated successfully", Toast.LENGTH_SHORT).show();
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

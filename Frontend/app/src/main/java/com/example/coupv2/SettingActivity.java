
package com.example.coupv2;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.coupv2.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Set;

public class SettingActivity extends AppCompatActivity {
    //variables
    private EditText userNameText;
    private Button updateUser;
    private static final String URL_JSON_OBJECT = "https://1e7a5334-cd98-471e-9f20-6136ce8bf7ec.mock.pstmn.io/updateSettings";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);            // link to Login activity XML
        userNameText = findViewById(R.id.settings_username_edt);
        updateUser = findViewById(R.id.settings_login_btn);
        updateUser.setOnClickListener(v -> {
            String username = userNameText.getText().toString();
            if (!username.isEmpty()) {
                //pass this into perform function down
                updateUserSettings(username);
            } else {
                Toast.makeText(SettingActivity.this, "Please enter a username", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void updateUserSettings(String username) {
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("username", username); // Adjust the key to what your API expects for updating the username
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(SettingActivity.this, "Error creating update request", Toast.LENGTH_SHORT).show();
            return;
        }

//        String updateEndpoint = "http://10.90.73.176:8080/updateSettings"; // Adjust this to your API's update endpoint
        RequestQueue requestQueue = AppController.getInstance().getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, URL_JSON_OBJECT, jsonRequest,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            // Update was successful
                            Toast.makeText(SettingActivity.this, "Settings updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            // Update failed with a message from the server
                            Toast.makeText(SettingActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(SettingActivity.this, "Invalid response from server", Toast.LENGTH_SHORT).show();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors here
                        if (error.networkResponse != null) {
                            String responseBody;
                            try {
                                responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                                // Here you can convert responseBody to a JSONObject or just display it as is.
                                Toast.makeText(SettingActivity.this, "Error: " + responseBody, Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                // handle exception
                                Toast.makeText(SettingActivity.this, "Error parsing network response", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            // When networkResponse is null, use error.toString() as it contains useful information
                            Toast.makeText(SettingActivity.this, "Network Error: " + error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });


        requestQueue.add(jsonObjectRequest);
    }

}
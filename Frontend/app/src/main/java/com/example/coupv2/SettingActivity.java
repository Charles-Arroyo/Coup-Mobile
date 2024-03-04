package com.example.coupv2;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
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
    private static final String URL_JSON_OBJECT = "http://coms-309-023.class.las.iastate.edu:8080/changeEmail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings); // link to Login activity XML
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

    private void updateUserSettings(String userEmail) {
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("userEmail", Const.getCurrentEmail());// Assuming you have the userId stored locally after login
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
                        int statusCode = error.networkResponse.statusCode;
                        String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                        Log.e("SettingActivity", "Error status code: " + statusCode + " Response body: " + responseBody);
                        Toast.makeText(SettingActivity.this, "Error: " + responseBody, Toast.LENGTH_LONG).show();
                    } else {
                        Log.e("SettingActivity", "Error: " + error.toString());
                        Toast.makeText(SettingActivity.this, "Network Error: " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }
}

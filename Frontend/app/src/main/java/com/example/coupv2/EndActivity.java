package com.example.coupv2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.coupv2.app.AppController;
import com.example.coupv2.utils.Const;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class EndActivity extends AppCompatActivity {
    private Button winButton;
    private Button lossButton;
//    private static final String URL_UPDATE_GAME_RESULT = "http://coms-309-023.class.las.iastate.edu:8443/gameTotal/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        //connect xml ids
        winButton = findViewById(R.id.win_btn);
        lossButton = findViewById(R.id.loss_btn);

        winButton.setOnClickListener(v -> {
            fetchPrimaryKeyAndUpdateGameResult(true); // true for win
            navigateToMenu();
        });

        lossButton.setOnClickListener(v -> {
            fetchPrimaryKeyAndUpdateGameResult(false); // false for loss
            navigateToMenu();
        });

    }
    //go back to main menu function
    private void navigateToMenu() {
        Intent menuIntent = new Intent(EndActivity.this, MenuActivity.class);
        startActivity(menuIntent);
    }

    private void fetchPrimaryKeyAndUpdateGameResult(boolean won) {
        String currentUser = Const.getCurrentEmail();
        String url = "http://coms-309-023.class.las.iastate.edu:8443/getId/" + Uri.encode(currentUser);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        int primaryKey = Integer.parseInt(response);
                        updateGameResult(primaryKey, won);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        Toast.makeText(EndActivity.this, "Error parsing primary key", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    if (error.networkResponse != null) {
                        String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                        Toast.makeText(EndActivity.this, "Error fetching primary key: " + responseBody, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EndActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        AppController.getInstance().getRequestQueue().add(stringRequest);
    }
    private void updateGameResult(int primaryKey, boolean won) {
        String URL_UPDATE_GAME_RESULT = "http://coms-309-023.class.las.iastate.edu:8443/gameTotal/" + primaryKey;
        JSONObject jsonRequest = new JSONObject();
        try {
//            jsonRequest.put("primaryKey", primaryKey);
            jsonRequest.put("gameResult", won ? "Win" : "Loss");
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(EndActivity.this, "Error creating game result update request", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL_UPDATE_GAME_RESULT, jsonRequest,
                response -> Toast.makeText(EndActivity.this, "Game result updated successfully", Toast.LENGTH_SHORT).show(),
                error -> {
                    if (error.networkResponse != null) {
                        String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                        Toast.makeText(EndActivity.this, "Error: " + responseBody, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(EndActivity.this, "Network Error: " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                });

        AppController.getInstance().getRequestQueue().add(jsonObjectRequest);
    }


}
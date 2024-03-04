package com.example.coupv2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class PlayActivity extends AppCompatActivity {
    private Button winButton;
    private Button lossButton;
    private static final String URL_UPDATE_GAME_RESULT = "http://yourbackend.com/api/updateGameResult";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        //connect xml ids
        winButton = findViewById(R.id.win_btn);
        lossButton = findViewById(R.id.loss_btn);

        winButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateGameResult(true); // true for win
                navigateToMenu();
            }
        });

        lossButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateGameResult(false); // false for loss
                navigateToMenu();
            }
        });
    }
    //go back to main menu function
    private void navigateToMenu() {
        Intent menuIntent = new Intent(PlayActivity.this, MenuActivity.class);
        startActivity(menuIntent);
    }

    private void updateGameResult(boolean won) {
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("gameResult", won ? "win" : "loss");
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(PlayActivity.this, "Error creating game result update request", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL_UPDATE_GAME_RESULT, jsonRequest,
                response -> Toast.makeText(PlayActivity.this, "Game result updated successfully", Toast.LENGTH_SHORT).show(),
                error -> {
                    if (error.networkResponse != null) {
                        String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                        Toast.makeText(PlayActivity.this, "Error: " + responseBody, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(PlayActivity.this, "Network Error: " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                });

        AppController.getInstance().getRequestQueue().add(jsonObjectRequest);
    }
}

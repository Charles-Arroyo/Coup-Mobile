package com.example.coupv2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.coupv2.app.AppController;
import com.example.coupv2.utils.Const;
import org.json.JSONObject;

public class StatsActivity extends AppCompatActivity {

    private TextView playerEmail, playerWins, playerLosses, playerGamesPlayed;
    private String currentUserEmail;
//    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        // Initialize TextViews
//        playerEmail = findViewById(R.id.stats_email);
        playerWins = findViewById(R.id.stats_wins);
        playerLosses = findViewById(R.id.stats_losses);
        playerGamesPlayed = findViewById(R.id.stats_games_played);

        // Get the current user's email or other identifier
        currentUserEmail = Const.getCurrentEmail(); // Replace with actual method to get the current user's email

        // Now fetch the primary key using the current user's email
        fetchPrimaryKey(currentUserEmail);
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Start the rules activity
//                Intent intent = new Intent(StatsActivity.this, MenuActivity.class);
//                startActivity(intent);
//            }
//        });
    }

    private void fetchPrimaryKey(String email) {
        String url = "http://coms-309-023.class.las.iastate.edu:8080/getId/" + Uri.encode(email);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            int primaryKey = Integer.parseInt(response); // Convert the response to an integer
                            // Now fetch the stats using the primary key
                            fetchUserStats(primaryKey);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            Log.e("StatsActivity", "Error parsing primary key");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("StatsActivity", "Error fetching primary key: " + error.toString());
                    }
                }
        );

        // Add the request to the RequestQueue.
        RequestQueue requestQueue = AppController.getInstance().getRequestQueue();
        requestQueue.add(stringRequest);
    }

    private void fetchUserStats(int primaryKey) {
        String urlWithPrimaryKey = "http://coms-309-023.class.las.iastate.edu:8080/getStats/" + primaryKey;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlWithPrimaryKey, null,
                response -> {
                    Log.d("StatsActivity", "Response received: " + response.toString()); // Debug log
                    try {
                        int wins = response.optInt("gameWon", 0); // Use optInt for default value
                        int losses = response.optInt("gameLost", 0);
                        int gamesPlayed = response.optInt("gamePlayed", wins + losses); // Use optInt

                        playerWins.setText(String.format("Wins: %d", wins));
                        playerLosses.setText(String.format("Losses: %d", losses));
                        playerGamesPlayed.setText(String.format("Games Played: %d", gamesPlayed));
                    } catch (Exception e) {
                        Log.e("StatsActivity", "Parsing error: " + e.getMessage(), e);
                    }
                },
                error -> {
                    Log.e("StatsActivity", "Volley error: " + error.toString());
                    if (error.networkResponse != null) {
                        Log.e("StatsActivity", "Error Response body: " + new String(error.networkResponse.data));
                    }
                }
        );

        RequestQueue requestQueue = AppController.getInstance().getRequestQueue();
        requestQueue.add(jsonObjectRequest);
    }



    // Handle the back button click by finishing the StatsActivity
//    public void onBackButtonClick(View view) {
//        finish();
//    }
}


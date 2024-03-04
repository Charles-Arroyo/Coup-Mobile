package com.example.coupv2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.coupv2.app.AppController;

import org.json.JSONObject;

public class StatsActivity extends AppCompatActivity {

    private static final String URL_FETCH_STATS = "http://yourbackend.com/api/fetchStats";
    private TextView playerEmail, playerWins, playerLosses, playerGamesPlayed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        // Initialize TextViews
        playerEmail = findViewById(R.id.stats_email);
        playerWins = findViewById(R.id.stats_wins);
        playerLosses = findViewById(R.id.stats_losses);
        playerGamesPlayed = findViewById(R.id.stats_games_played);

        fetchUserStats();
    }

    private void fetchUserStats() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL_FETCH_STATS, null,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            // Assuming the JSON response has a 'stats' JSONObject
                            JSONObject stats = response.getJSONObject("stats");
                            String email = stats.getString("email");
                            int wins = stats.getInt("wins");
                            int losses = stats.getInt("losses");
                            int gamesPlayed = stats.getInt("gamesPlayed");

                            // Set the text of the TextViews to the fetched stats
                            playerEmail.setText(String.format("Email: %s", email));
                            playerWins.setText(String.format("Wins: %d", wins));
                            playerLosses.setText(String.format("Losses: %d", losses));
                            playerGamesPlayed.setText(String.format("Games Played: %d", gamesPlayed));
                        } else {
                            Log.e("StatsActivity", "Fetching stats failed: " + response.getString("message"));
                        }
                    } catch (Exception e) {
                        Log.e("StatsActivity", "Parsing error: " + e.getMessage(), e);
                    }
                },
                error -> Log.e("StatsActivity", "Volley error: " + error.toString())
        );

        // Add the request to the RequestQueue.
        RequestQueue requestQueue = AppController.getInstance().getRequestQueue();
        requestQueue.add(jsonObjectRequest);
    }

    // Handle the back button click by finishing the StatsActivity
    public void onBackButtonClick(View view) {
        finish();
    }
}

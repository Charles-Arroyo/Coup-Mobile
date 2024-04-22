package com.example.coupv2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.coupv2.app.AppController;
import com.example.coupv2.utils.Const;

public class StatsActivity extends AppCompatActivity {

    private TextView  playerWins, playerLosses, playerGamesPlayed, playerScore,
            playerRank, playerAverage;
    private String currentUserEmail;
    private Button back, email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(Const.getCurrentTheme());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        Intent intent = getIntent();
        String currEmail = intent.getStringExtra("USERNAME");



        // Initialize TextViews
        email = findViewById(R.id.stats_user);
        playerWins = findViewById(R.id.stats_wins);
        playerLosses = findViewById(R.id.stats_losses);
        playerGamesPlayed = findViewById(R.id.stats_games_played);
        playerScore = findViewById(R.id.stats_score);
        playerRank = findViewById(R.id.stats_rank);
        playerAverage = findViewById(R.id.stats_average);
        back = findViewById(R.id.back_stats);



        back.setOnClickListener(v -> {
            finish();
        });

        email.setText(currEmail);

        Animation pulse = AnimationUtils.loadAnimation(this, R.anim.pulse_animation);

        email.setAnimation(pulse);
        // Now fetch the primary key using the current user's email
        getUserStats(currEmail);
    }

    private void getUserStats(String email) {
        String STATS_URL = "https://3a856af0-b6ac-48f3-a93a-06d2cd454e01.mock.pstmn.io/stats/" + email;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, STATS_URL, null,
                response -> {
                    Log.d("StatsActivity", "Response received: " + response.toString()); // Debug log
                    try {
                        int wins = response.getInt("wins");
                        int losses = response.getInt("losses");
                        double average =  (wins / (wins + losses)) * 100;
                        int score = response.getInt("score");
                        int rank = response.optInt("rank");
                        int gamesPlayed = wins + losses;

                        playerWins.setText(String.format("Wins: %d", wins));
                        playerLosses.setText(String.format("Losses: %d", losses));
                        playerGamesPlayed.setText(String.format("Games Played: %d", gamesPlayed));
                        playerAverage.setText(String.format("Average Wins: %d", average));
                        playerScore.setText(String.format("Score: %d", score));
                        playerRank.setText(String.format("Rank: %d", rank));
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
}


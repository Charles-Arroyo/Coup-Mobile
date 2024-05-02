package com.example.coupv2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.coupv2.app.AppController;
import com.example.coupv2.utils.Const;

public class StatsActivity extends AppCompatActivity {

    private TextView  playerWins, playerloses, playerGamesPlayed, playerScore,
            playerRank, playerAverage;
    private String currEmail;
    private Button back, email;
//    public static final String URL_IMAGE = "http://10.0.2.2:8443/images/1";

    private ImageView pfp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(Const.getCurrentTheme());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        Intent intent = getIntent();
        currEmail = intent.getStringExtra("USER");

        // Initialize TextViews
        email = findViewById(R.id.stats_user);
        playerWins = findViewById(R.id.stats_wins);
        playerloses = findViewById(R.id.stats_losses);
        playerGamesPlayed = findViewById(R.id.stats_games_played);
        playerScore = findViewById(R.id.stats_score);
        playerRank = findViewById(R.id.stats_rank);
        playerAverage = findViewById(R.id.stats_average);
        back = findViewById(R.id.back_stats);
        pfp = (ImageView) findViewById(R.id.stats_picture);

        makeImageRequest();


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
        String STATS_URL = "http://coms-309-023.class.las.iastate.edu:8443/getStats/" + email;

//        String STATS_URL = "https://3a856af0-b6ac-48f3-a93a-06d2cd454e01.mock.pstmn.io/stats/";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, STATS_URL, null,
                response -> {
                    Log.d("StatsActivity", "Response received: " + response.toString()); // Debug log
                    try {
                        int wins = response.getInt("wins");
                        int loses = response.getInt("loses");
                        double average = ((double) wins / (wins + loses)) * 100; // Corrected average calculation
                        int score = response.getInt("score");
                        int rank = response.getInt("rank");
                        int gamesPlayed = wins + loses;

                        playerWins.setText(String.format("Wins: %d", wins));
                        playerloses.setText(String.format("loses: %d", loses));
                        playerGamesPlayed.setText(String.format("Games Played: %d", gamesPlayed));
                        playerAverage.setText(String.format("Average Wins: %.2f%%", average));
                        playerScore.setText(String.format("Score: %d", score));
                        playerRank.setText(String.format("Rank: %d", rank));
                    } catch (Exception e) {
                        Log.e("StatsActivity", "Parsing error: " + e.getMessage(), e);
                    }
                },
                error -> {
                    Log.e("StatsActivity", "Volley error: " + error.toString());
                    Toast.makeText(getApplicationContext(), "Failed to load stats", Toast.LENGTH_SHORT).show();
                    if (error.networkResponse != null) {
                        Log.e("StatsActivity", "Error Response body: " + new String(error.networkResponse.data));
                    }
                }
        );

        RequestQueue requestQueue = AppController.getInstance().getRequestQueue();
        requestQueue.add(jsonObjectRequest);
    }

    /**
     * Making image request
     * */
    private void makeImageRequest() {
        String URL_IMAGE = "http://coms-309-023.class.las.iastate.edu:8443/getProfile/" + currEmail;

        ImageRequest imageRequest = new ImageRequest(
                URL_IMAGE,
                response -> {
                    // Handle the response Bitmap
                    if (response != null) {
                        // Display the image in the ImageView
                        pfp.setImageBitmap(response);
                    } else {
                        Log.e("Bitmap Error", "Received null Bitmap");
                    }
                },
                0, // Width, set to 0 to get the original width
                0, // Height, set to 0 to get the original height
                ImageView.ScaleType.FIT_XY, // ScaleType
                Bitmap.Config.RGB_565, // Bitmap config
                error -> {
                    // Handle errors here
                    Log.e("Volley Error", error.toString());
                }
        );

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(imageRequest);
    }



}


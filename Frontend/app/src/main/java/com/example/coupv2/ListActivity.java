package com.example.coupv2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ListActivity extends AppCompatActivity {

    private LinearLayout peopleLayout;
    private EditText searchEditText;
    private Button searchButton, backButton;
    private RequestQueue requestQueue;
    private String ALL_PLAYERS_URL = "https://3a856af0-b6ac-48f3-a93a-06d2cd454e01.mock.pstmn.io/players";
    private String RESET_SCORE_URL = "https://3a856af0-b6ac-48f3-a93a-06d2cd454e01.mock.pstmn.io/success";
    private String USER_STATS_URL = "https://3a856af0-b6ac-48f3-a93a-06d2cd454e01.mock.pstmn.io/stats";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        peopleLayout = findViewById(R.id.peopleLayout);
        searchEditText = findViewById(R.id.editTextText2);
        searchButton = findViewById(R.id.search);
        backButton = findViewById(R.id.back);

        requestQueue = Volley.newRequestQueue(this);
        fetchUsers();

        searchButton.setOnClickListener(view -> filterPeople(searchEditText.getText().toString()));
        backButton.setOnClickListener(view -> finish());
    }

    private void fetchUsers() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ALL_PLAYERS_URL, null,
                response -> {
                    try {
                        for (int i = 0; i < response.getJSONArray("people").length(); i++) {
                            JSONObject person = response.getJSONArray("people").getJSONObject(i);
                            addPersonToLayout(person.getString("name"));
                        }
                    } catch (JSONException e) {
                        Toast.makeText(ListActivity.this, "Error parsing JSON data", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(ListActivity.this, "Error fetching data: " + error.getMessage(), Toast.LENGTH_SHORT).show());

        requestQueue.add(jsonObjectRequest);
    }

    private void addPersonToLayout(String name) {
        View userList = getLayoutInflater().inflate(R.layout.person_item, peopleLayout, false);
        TextView textView = userList.findViewById(R.id.tvPersonName);
        Button detailsButton = userList.findViewById(R.id.btnDetails);

        textView.setText(name);
        detailsButton.setOnClickListener(v -> showUserPopup(name));

        peopleLayout.addView(userList);
    }

    private void filterPeople(String query) {
        for (int i = 0; i < peopleLayout.getChildCount(); i++) {
            View view = peopleLayout.getChildAt(i);
            TextView textView = view.findViewById(R.id.tvPersonName);
            if (!textView.getText().toString().toLowerCase().contains(query.toLowerCase())) {
                view.setVisibility(View.GONE);
            } else {
                view.setVisibility(View.VISIBLE);
            }
        }
    }

    private void showUserPopup(String username) {
        LayoutInflater inflater = getLayoutInflater();
        View statsView = inflater.inflate(R.layout.admin_activity_stats, null);

        TextView userEmail = statsView.findViewById(R.id.stats_email);
        TextView statsWins = statsView.findViewById(R.id.stats_wins);
        TextView statsLosses = statsView.findViewById(R.id.stats_losses);
        TextView statsGamesPlayed = statsView.findViewById(R.id.stats_games_played);
        TextView statsScore = statsView.findViewById(R.id.stats_score);
        TextView statsAverage = statsView.findViewById(R.id.stats_average);
        TextView statsRank = statsView.findViewById(R.id.stats_rank);
        Button resetStatsButton = statsView.findViewById(R.id.reset_stats);

        String userStatsUrl = USER_STATS_URL + username;  // Correctly create a local variable for the URL

        JsonObjectRequest statsRequest = new JsonObjectRequest(Request.Method.GET, userStatsUrl, null,
                response -> {
                    try {
                        String email = response.getString("email");
                        int wins = response.getInt("wins");
                        int losses = response.getInt("losses");
                        int score = response.getInt("score");
                        int rank = response.getInt("rank");
                        int gamesPlayed = wins + losses;
                        double average;
                        if (gamesPlayed > 0) {
                            average = (double) score / gamesPlayed;
                        } else {
                            average = 0;
                        }

                        userEmail.setText("Email: " + email);
                        statsWins.setText("Wins: " + wins);
                        statsLosses.setText("Losses: " + losses);
                        statsGamesPlayed.setText("Games Played: " + gamesPlayed);
                        statsScore.setText("Score: " + score);
                        statsAverage.setText("Average: " + average);
                        statsRank.setText("Rank: " + rank);
                    } catch (JSONException e) {
                        Toast.makeText(ListActivity.this, "Failed to parse statistics", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(ListActivity.this, "Error fetching statistics: " + error.toString(), Toast.LENGTH_SHORT).show();
                    if (error.networkResponse != null) {
                        Log.e("StatsActivity", "Error Response body: " + new String(error.networkResponse.data));
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(statsRequest);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(statsView);
        builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        resetStatsButton.setOnClickListener(v -> resetUserStats(username));
    }


    private void resetUserStats(String username) {
        String resetUrl = RESET_SCORE_URL + username;  // Correctly create a local variable for the URL

        JSONObject params = new JSONObject();
        try {
            params.put("wins", 0);
            params.put("losses", 0);
            params.put("score", 0);
            params.put("rank", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest resetRequest = new JsonObjectRequest(Request.Method.PUT, resetUrl, params,
                response -> Toast.makeText(ListActivity.this, "Statistics reset successfully.", Toast.LENGTH_SHORT).show(),
                error -> {
                    Toast.makeText(ListActivity.this, "Failed to reset statistics.", Toast.LENGTH_SHORT).show();
                    if (error.networkResponse != null) {
                        Log.e("StatsActivity", "Error Response body: " + new String(error.networkResponse.data));
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(resetRequest);
    }

}

package com.example.coupv2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    // UI elements
    private Button playButton;
    private Button friendsButton;
    private Button settingsButton;
    private Button statsButton;
    private Button rulesButton;
    private Button backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu); // Set the layout for this activity

        // Initialize UI elements
        playButton = findViewById(R.id.play_btn);
        friendsButton = findViewById(R.id.friends_btn);
        settingsButton = findViewById(R.id.settings_btn);
        statsButton = findViewById(R.id.stats_btn);
        rulesButton = findViewById(R.id.rules_btn);
        backButton = findViewById(R.id.back_button);
        // Set up click listeners for each button


        // Play Button
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the play activity
                Intent intent = new Intent(MenuActivity.this, EndActivity.class);
                startActivity(intent);
            }
        });

        // Friends Button
        friendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the friends activity
                Intent intent = new Intent(MenuActivity.this, FriendsActivity.class);
                startActivity(intent);
            }
        });

        // Settings Button
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the settings activity
                Intent intent = new Intent(MenuActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        // Stats Button
        statsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the statistics activity
                Intent intent = new Intent(MenuActivity.this, StatsActivity.class);
                startActivity(intent);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the rules activity
                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Rules Button
        rulesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the rules activity
                Intent intent = new Intent(MenuActivity.this, RulesActivity.class);
                startActivity(intent);
            }
        });
    }
}

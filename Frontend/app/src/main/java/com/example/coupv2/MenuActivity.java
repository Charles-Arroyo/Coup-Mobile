package com.example.coupv2;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;

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
                // Start the looby activity
                Intent intent = new Intent(MenuActivity.this, LobbyActivity.class);
                //for going straight to PlayActivity
//                Intent intent = new Intent(MenuActivity.this, PlayActivity.class);
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

                showRules();
            }
        });


    }


    private void showRules() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.activity_rules, null);
        bottomSheetDialog.setContentView(view);

        // Set up the click listener for the assassin ImageButton
        ImageButton assassinButton = view.findViewById(R.id.assassin);
        assassinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePopup(R.drawable.assassin);
            }
        });

        // Set up the click listener for the captain ImageButton
        ImageButton captainButton = view.findViewById(R.id.captain);
        captainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePopup(R.drawable.captain);
            }
        });

        // Set up the click listener for the duke ImageButton
        ImageButton dukeButton = view.findViewById(R.id.duke);
        dukeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePopup(R.drawable.duke);
            }
        });

//        // Set up the click listener for the ambassador ImageButton
//        ImageButton ambassadorButton = view.findViewById(R.id.ambassador);
//        ambassadorButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showImagePopup(R.drawable.ambassador); // Make sure this drawable resource exists
//            }
//        });
//
//        // Set up the click listener for the contra ImageButton
//        ImageButton contraButton = view.findViewById(R.id.contra);
//        contraButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showImagePopup(R.drawable.contra); // Make sure this drawable resource exists
//            }
//        });

        // Close button inside the BottomSheetDialog
        Button closeButton = view.findViewById(R.id.close_rules_coup_overlay_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
    }


    private void showImagePopup(int imageResource) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogLayout = getLayoutInflater().inflate(R.layout.popup_image, null);
        ImageView imageView = dialogLayout.findViewById(R.id.popup_image);

        imageView.setImageResource(imageResource);
        builder.setView(dialogLayout);
        AlertDialog dialog = builder.create();

        // When the dialog's main layout is clicked, dismiss the dialog
        dialogLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // This will allow the dialog to close when the ImageView (or its parent layout) is clicked
                dialog.dismiss();
            }
        });

        // Make sure the dialog's window background is transparent
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        dialog.show();
    }


}

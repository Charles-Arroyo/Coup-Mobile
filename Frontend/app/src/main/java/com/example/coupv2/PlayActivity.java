package com.example.coupv2;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;

public class PlayActivity extends AppCompatActivity {
    private Button winButton;
    private Button lossButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);            // link to Login activity XML
        //connect xml ids
        winButton = findViewById(R.id.win_btn);
        lossButton = findViewById(R.id.loss_btn);
        winButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Grab strings from user inputs


                // go back to main menu
                Intent menuIntent = new Intent(PlayActivity.this, MenuActivity.class);
                startActivity(menuIntent);

            }
        });
        lossButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // go back to main menu
                Intent menuIntent = new Intent(PlayActivity.this, MenuActivity.class);
                startActivity(menuIntent);
            }
        });
    }
}
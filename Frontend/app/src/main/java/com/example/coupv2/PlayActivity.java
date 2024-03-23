package com.example.coupv2;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class PlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play); // link to Play activity XML
        //variables
        ImageView gameBoard = findViewById(R.id.gameBoard);
        gameBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                go to new activity when clicked on
                Intent intent = new Intent(PlayActivity.this, ActionActivity.class);
                startActivity(intent);
            }
        });
    }
}

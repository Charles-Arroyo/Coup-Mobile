package com.example.androidexample;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MitraActivity extends AppCompatActivity {

    private TextView messageText;   // define message textview variable
    private ImageView mitra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mitra);

        /* initialize UI elements */
        messageText = findViewById(R.id.main_msg_txt);
        String x = " Your not mitra!!!" + "\n";
        mitra = findViewById(R.id.mitra);
        messageText.setText(x);

        // Set the image for the ImageView
        mitra.setImageResource(R.drawable.mitra);
    }
}
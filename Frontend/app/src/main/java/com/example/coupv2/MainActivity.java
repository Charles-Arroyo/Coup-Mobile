package com.example.coupv2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    //take in last saved state
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //build layout off xml file
        setContentView(R.layout.activity_main);
    }
}
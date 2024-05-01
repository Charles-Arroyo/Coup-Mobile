package com.example.coupv2;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ImageButton loginButton;     // define login button variable
    private ImageButton signupButton;    // define signup button variable

    private ImageView COUP;

    /**
     * Main Oncreate to intialize elements in the title screen
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.DarkTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_charles);

        /* initialize UI elements */



        loginButton = findViewById(R.id.main_login_btn);    // link to login button in the Main activity XML
        signupButton = findViewById(R.id.main_signup_btn);  // link to signup button in the Main activity XML

//        COUP = findViewById(R.id.COUP);

        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        Animation fadeOutAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);

//        COUP.startAnimation(fadeInAnimation);
//        loginButton.startAnimation(fadeInAnimation);
//        signupButton.startAnimation(fadeInAnimation);

//        fadeInAnimation.setDuration(3000);
        /* click listener on login button pressed */
        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        signupButton.setOnClickListener(v -> {
            /* when signup button is pressed, use intent to switch to Signup Activity */
            Intent intent = new Intent(MainActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }
}
package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    private EditText usernameEditText;  // define username edittext variable
    private EditText passwordEditText;  // define password edittext variable
    private EditText confirmEditText;   // define confirm edittext variable
    private Button loginButton;         // define login button variable
    private Button signupButton;        // define signup button variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        /* initialize UI elements */
        usernameEditText = findViewById(R.id.signup_username_edt);  // link to username edtext in the Signup activity XML
        passwordEditText = findViewById(R.id.signup_password_edt);  // link to password edtext in the Signup activity XML
        confirmEditText = findViewById(R.id.signup_confirm_edt);    // link to confirm edtext in the Signup activity XML
        loginButton = findViewById(R.id.signup_login_btn);    // link to login button in the Signup activity XML
        signupButton = findViewById(R.id.signup_signup_btn);  // link to signup button in the Signup activity XML

        /* click listener on login button pressed */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* when login button is pressed, use intent to switch to Login Activity */
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);  // go to LoginActivity
            }
        });

        /* click listener on signup button pressed */
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* grab strings from user inputs */
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirm = confirmEditText.getText().toString();
                //check if password has number
                boolean containsNumber = false;
                boolean containsCapitalLetter = false;
                String specialCharsPattern = "[!@#]";
                //check if password contains special char
                Pattern pattern = Pattern.compile(specialCharsPattern);
                Matcher matcher = pattern.matcher(password);
                boolean containsSymbol = matcher.find();
                //look through password
                for (int i = 0; i < password.length(); i++){
                    //check for number
                    if (Character.isDigit(password.charAt(i))){
                        //letter is found
                        containsNumber = true;
                    }
                    //look for uppercase
                    if (Character.isUpperCase(password.charAt(i))){
                        containsCapitalLetter = true;
                    }
                }
                //look through username for number
                boolean containsUNumber = false;
                boolean containsUCapitalLetter = false;
                for (int i = 0; i < username.length(); i++){
                    //check for number
                    if (Character.isDigit(username.charAt(i))){
                        //letter is found
                        containsUNumber = true;
                    }
                    //look for uppercase
                    if (Character.isUpperCase(username.charAt(i))){
                        containsUCapitalLetter = true;
                    }
                }
                //check criteria
                if(username.length() < 8){
                    usernameEditText.setError("username length is less than 8 characters");
                    return;
                }
                if(containsUNumber == false){
                    usernameEditText.setError("username does not contain number");
                    return;
                }
                if(containsUCapitalLetter == false){
                    usernameEditText.setError("username does not contain capital letter");
                    return;
                }
                if (containsSymbol == false){
                    passwordEditText.setError("password  does not contain symbol such as !, @, #");
                    return; // Don't continue executing the rest of this method
                }
                if (containsNumber == false){
                    passwordEditText.setError("password does not contain number");
                    return; // Don't continue executing the rest of this method
                }
                if (containsCapitalLetter == false){
                    passwordEditText.setError("password does not contain capital letter");
                    return; // Don't continue executing the rest of this method
                }
                if (password.length() < 8){
                    passwordEditText.setError("password  is less than 8 characters");
                    return; // Don't continue executing the rest of this method
                }
                if (password.equals(confirm)){
                    Toast.makeText(getApplicationContext(), "Signing up", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Password don't match", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
package com.example.coupv2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.coupv2.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

public class SignupActivity extends AppCompatActivity {

    private EditText emailIdEditText, usernameEditText;
    private EditText passwordEditText;
    private ImageButton loginButton;
    private ImageButton signupButton;



//    private static final String URL_SIGNUP = "http://10.90.73.176:8080/signup";
//    private static final String URL_SIGNUP = "http://coms-309-023.class.las.iastate.edu:8080/signup";
private static final String URL_SIGNUP = "http://coms-309-023.class.las.iastate.edu:8080/signup";
    // success
//    private static final String URL_SIGNUP = "https://3a856af0-b6ac-48f3-a93a-06d2cd454e01.mock.pstmn.io/success";
    //fail
//    private static final String URL_SIGNUP = "https://fc027c91-5b3c-49e3-8239-a0223c763b2a.mock.pstmn.io";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_charles);

        usernameEditText = findViewById(R.id.signup_name_edt);

        emailIdEditText = findViewById(R.id.signup_email_edt);
        passwordEditText = findViewById(R.id.signup_password_edt);
        loginButton = findViewById(R.id.signup_login_btn);
        signupButton = findViewById(R.id.signup_signup_btn);

        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        signupButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String email = emailIdEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            if (!email.isEmpty() && !password.isEmpty() && !username.isEmpty()) {

                if (email.contains("@") && (email.endsWith(".com") || email.endsWith(".edu") || email.endsWith(".org")))
                {
                    performSignup(username,email, password);
                } else {
                    Toast.makeText(SignupActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(SignupActivity.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void performSignup(String username, String email, String password) {
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("name", username);
            jsonRequest.put("userEmail", email);
            jsonRequest.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = AppController.getInstance().getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL_SIGNUP, jsonRequest,
                response -> {
                    try {

                        boolean success = response.getBoolean("success");

                        if (success) {
                            // Successful signup
                            Toast.makeText(SignupActivity.this, "Signup successful!", Toast.LENGTH_SHORT).show();
                            Intent mainIntent = new Intent(SignupActivity.this, LoginActivity.class);
                            startActivity(mainIntent);
                        } else {
                            // Failed signup
                            Toast.makeText(SignupActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(SignupActivity.this, "Invalid response from server", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors here
                        Toast.makeText(SignupActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }
}

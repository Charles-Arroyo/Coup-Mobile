package com.example.coupv2;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    private EditText emailIdEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button signupButton;

//    private static final String URL_SIGNUP = "http://10.90.73.176:8080/signup";
    private static final String URL_SIGNUP = "http://coms-309-023.class.las.iastate.edu:8080/signup";
    // success
//    private static final String URL_SIGNUP = "https://c0552578-2f02-4669-999e-030ad7fa3950.mock.pstmn.io";
    //fail
//    private static final String URL_SIGNUP = "https://fc027c91-5b3c-49e3-8239-a0223c763b2a.mock.pstmn.io";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailIdEditText = findViewById(R.id.signup_email_edt);
        passwordEditText = findViewById(R.id.signup_password_edt);
        loginButton = findViewById(R.id.signup_login_btn);
        signupButton = findViewById(R.id.signup_signup_btn);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailIdEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if (!email.isEmpty() && !password.isEmpty()) {
                    performSignup(email, password);
                } else {
                    Toast.makeText(SignupActivity.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void performSignup(String emailId, String password) {
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("emailId", emailId);
            jsonRequest.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = AppController.getInstance().getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL_SIGNUP, jsonRequest,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

//                            String message = response.getString("message");
//
//                            if (message.equals("success"))

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

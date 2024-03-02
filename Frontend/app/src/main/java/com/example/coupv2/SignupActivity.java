package com.example.coupv2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmEditText;
    private Button loginButton;
    private Button signupButton;

    private static final String URL_SIGNUP = "https://example.com/signup"; // Adjust the URL_SIGNUP according to your server's signup endpoint
//    private static final String URL_JSON_OBJECT = "https://coms-309-023.class.las.iastate.edu";
    // success
    private static final String URL_JSON_OBJECT = "https://a9d64c4f-e136-411d-9914-ca9fdc127577.mock.pstmn.io";
    //fail
//    private static final String URL_JSON_OBJECT = "https://e240d7cb-1f58-4450-aafc-17819ecd7566.mock.pstmn.io";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        usernameEditText = findViewById(R.id.signup_username_edt);
        passwordEditText = findViewById(R.id.signup_password_edt);
        confirmEditText = findViewById(R.id.signup_confirm_edt);
        loginButton = findViewById(R.id.signup_login_btn);
        signupButton = findViewById(R.id.signup_signup_btn);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

//        signupButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String username = usernameEditText.getText().toString();
//                String password = passwordEditText.getText().toString();
//                String confirm = confirmEditText.getText().toString();
//
//                if (!username.isEmpty() && !password.isEmpty() && !confirm.isEmpty()) {
//                    if (password.equals(confirm)) {
//                        performSignup(username, password);
//                    } else {
//                        Toast.makeText(SignupActivity.this, "Password does not match confirmation", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(SignupActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//
//    private void performSignup(String username, String password) {
//        JSONObject jsonRequest = new JSONObject();
//        try {
//            jsonRequest.put("username", username);
//            jsonRequest.put("password", password);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        RequestQueue requestQueue = AppController.getInstance().getRequestQueue();
////        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL_SIGNUP, jsonRequest,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            boolean success = response.getBoolean("success");
//                            if (success) {
//                                Toast.makeText(SignupActivity.this, "Signup successful!", Toast.LENGTH_SHORT).show();
//                                Intent loginIntent = new Intent(SignupActivity.this, LoginActivity.class);
//                                startActivity(loginIntent);
//                            } else {
//                                Toast.makeText(SignupActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toast.makeText(SignupActivity.this, "Invalid response from server", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(SignupActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//        requestQueue.add(jsonObjectRequest);
//    }
}}

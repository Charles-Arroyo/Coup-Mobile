
package com.example.coupv2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.coupv2.app.AppController;
import com.example.coupv2.utils.Const;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements WebSocketListener {

    private EditText emailIdEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button signupButton;

//    private static final String URL_JSON_OBJECT = "http://10.90.73.176:8080/signin";
    private static final String URL_JSON_OBJECT = "https://3a856af0-b6ac-48f3-a93a-06d2cd454e01.mock.pstmn.io/success";
    // success


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailIdEditText = findViewById(R.id.login_email_edt);
        passwordEditText = findViewById(R.id.login_password_edt);
        loginButton = findViewById(R.id.login_login_btn);
        signupButton = findViewById(R.id.login_signup_btn);

        loginButton.setOnClickListener(v -> {
            String username = emailIdEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            if (!username.isEmpty() && !password.isEmpty()) {
                performLogin(username, password);
            } else {
                Toast.makeText(LoginActivity.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            }
        });

        signupButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }

    private void performLogin(String emailId, String password) {
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("userEmail", emailId);
            jsonRequest.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = AppController.getInstance().getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL_JSON_OBJECT, jsonRequest,
                response -> {
                    try {


                        boolean success = response.getBoolean("success");

                        if (success) {
                            // Successful login
                            Intent mainIntent = new Intent(LoginActivity.this, MenuActivity.class);
                            mainIntent.putExtra("EMAIL", emailId);
                            startActivity(mainIntent);
                            Const.setCurrentEmail(emailId);
                        } else {
                            // Failed login
                            Toast.makeText(LoginActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "Invalid response from server", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // Handle errors here
                    Toast.makeText(LoginActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {

    }

    @Override
    public void onWebSocketMessage(String message) {

    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {

    }

    @Override
    public void onWebSocketError(Exception ex) {

    }
}

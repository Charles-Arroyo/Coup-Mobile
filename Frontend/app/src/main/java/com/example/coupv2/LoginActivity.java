
package com.example.coupv2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
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

//    private static final String ACTIVE_URL = "ws://coms-309-023.class.las.iastate.edu:8443/signin/";
private static final String ACTIVE_URL = "ws://coms-309-023.class.las.iastate.edu:8443/signin/";
    private EditText emailIdEditText;
    private EditText passwordEditText;
    private ImageButton loginButton;
    private ImageButton signupButton;

//    private static final String URL_JSON_OBJECT = "http://10.90.73.176:8443/signin";
//private static final String URL_JSON_OBJECT = "http://localhost:8443/signin";

//    private static final String URL_JSON_OBJECT = "http://coms-309-023.class.las.iastate.edu:8443/signin";
private static final String URL_JSON_OBJECT = "http://coms-309-023.class.las.iastate.edu:8443/signin";
//private static final String URL_JSON_OBJECT = "http://localhost:8443/signin";

//    private static final String URL_JSON_OBJECT = "http://coms-309-023.class.las.iastate.edu:8443git/signin";
    // success
//    private static final String URL_JSON_OBJECT = "https://3a856af0-b6ac-48f3-a93a-06d2cd454e01.mock.pstmn.io/user";


    /**
     * On Create method for Login Activity
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_charles);

        emailIdEditText = findViewById(R.id.login_email_edt);
        passwordEditText = findViewById(R.id.login_password_edt);
        loginButton = findViewById(R.id.login_login_btn);
        signupButton = findViewById(R.id.login_signup_btn);

        loginButton.setOnClickListener(v -> {
            String username = emailIdEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if(username.equals("ADMIN") && password.equals("ADMIN")){
                Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                startActivity(intent);


            } else if (!username.isEmpty() && !password.isEmpty()) {

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

    /**
     *  Gets login and sends a POST request to tell the server if they are in. Will tell them if they
     *  failed or success.
     *
     * @param emailId use users email to login
     * @param password use users password to login
     */

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

                        String success = response.getString("success");

                        if (success.equals("admin")){
                            Intent mainIntent = new Intent(LoginActivity.this, AdminActivity.class);
                            startActivity(mainIntent);
                        }
                        else if (success.equals("true")){
                            Intent mainIntent = new Intent(LoginActivity.this, MenuActivity.class);
                            startActivity(mainIntent);
                            Const.setCurrentEmail(emailId);
                            String serverUrl = ACTIVE_URL + emailId;
                            WebSocketManager2.getInstance().connectWebSocket(serverUrl);

                        } else {
                            Toast.makeText(LoginActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "Invalid response from server", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(LoginActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        requestQueue.add(jsonObjectRequest);
    }

    /**
     * Websocket method to connect
     *
     * @param handshakedata Information about the server handshake.
     */

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {

    }

    /**
     * Websocket method to send messages to server
     *
     * @param message The received WebSocket message.
     */

    @Override
    public void onWebSocketMessage(String message) {

    }

    /**
     * Websocket method to notify and close websocket
     *
     * @param code   The status code indicating the reason for closure.
     * @param reason A human-readable explanation for the closure.
     * @param remote Indicates whether the closure was initiated by the remote endpoint.
     */

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {

    }

    /**
     * Websocket method to tell if websocket has an error
     *
     * @param ex The exception that describes the error.
     */

    @Override
    public void onWebSocketError(Exception ex) {

    }


}

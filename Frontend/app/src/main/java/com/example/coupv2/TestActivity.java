
package com.example.coupv2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.coupv2.app.AppController;
import com.example.coupv2.utils.Const;

import org.json.JSONException;
import org.json.JSONObject;

public class TestActivity extends AppCompatActivity {

    private Button Player1;
    private Button Player2;
    private Button Player3;
    private Button Player4;

//    private static final String URL_JSON_OBJECT = "http://coms-309-023.class.las.iastate.edu:8443/signin";
private static final String URL_JSON_OBJECT = "http://coms-309-023.class.las.iastate.edu:8443/signin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Player1 = findViewById(R.id.testBtn1);
        Player2 = findViewById(R.id.testBtn2);
        Player3 = findViewById(R.id.testBtn3);
        Player4 = findViewById(R.id.testBtn4);


        Player1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Const.setCurrentEmail("A");
                performLogin("A");
            }
        });
        Player2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Const.setCurrentEmail("B");
                performLogin("B");
            }
        });
        Player3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Const.setCurrentEmail("C");
                performLogin("C");
            }
        });
        Player4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Const.setCurrentEmail("D");
                performLogin("D");
            }
        });
        }

    private void performLogin(String emailId) {
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("userEmail", emailId);
            jsonRequest.put("password", "123");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = AppController.getInstance().getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL_JSON_OBJECT, jsonRequest,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

//                            String message = response.getString("message");
//
//                            if (message.equals("success"))

                            boolean success = response.getBoolean("success");

                            if (success) {
                                // Successful login
                                Intent mainIntent = new Intent(TestActivity.this, LobbyActivity.class);
                                mainIntent.putExtra("EMAIL", emailId);
                                startActivity(mainIntent);
                                Const.setCurrentEmail(emailId);
                            } else {
                                // Failed login
                                Toast.makeText(TestActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(TestActivity.this, "Invalid response from server", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors here
                        Toast.makeText(TestActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

}
package com.example.coupv2;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ListActivity extends AppCompatActivity {

    private LinearLayout peopleLayout;
    private EditText searchEditText;
    private Button searchButton, backButton;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        peopleLayout = findViewById(R.id.peopleLayout);
        searchEditText = findViewById(R.id.editTextText2);
        searchButton = findViewById(R.id.search);
        backButton = findViewById(R.id.back);

        requestQueue = Volley.newRequestQueue(this);
        fetchPeople();

        searchButton.setOnClickListener(view -> filterPeople(searchEditText.getText().toString()));
        backButton.setOnClickListener(view -> finish());
    }

    private void fetchPeople() {
        String url = "http://your.api/endpoint";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject person = jsonArray.getJSONObject(i);
                            addPersonToLayout(person.getString("name"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
        });

        requestQueue.add(stringRequest);
    }

    private void addPersonToLayout(String name) {
        View personView = getLayoutInflater().inflate(R.layout.person_item, peopleLayout, false);
        TextView textView = personView.findViewById(R.id.tvPersonName);
        Button detailsButton = personView.findViewById(R.id.btnDetails);
        textView.setText(name);
        detailsButton.setOnClickListener(v -> showUserPopup(name));
        peopleLayout.addView(personView);
        ScrollView scrollView = findViewById(R.id.scrollView3);
        scrollView.post(() -> scrollView.fullScroll(ScrollView.FOCUS_DOWN));
    }

    private void filterPeople(String query) {
        for (int i = 0; i < peopleLayout.getChildCount(); i++) {
            View view = peopleLayout.getChildAt(i);
            TextView textView = view.findViewById(R.id.tvPersonName);
            if (!textView.getText().toString().toLowerCase().contains(query.toLowerCase())) {
                view.setVisibility(View.GONE);
            } else {
                view.setVisibility(View.VISIBLE);
            }
        }
    }

    private void showUserPopup(String username) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(username);
        builder.setMessage("More info about " + username + "\nStats: XYZ");
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.setNegativeButton("View Profile", (dialog, which) -> {
            Intent profileIntent = new Intent(ListActivity.this, StatsActivity.class);
            profileIntent.putExtra("USERNAME", username);
            startActivity(profileIntent);
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

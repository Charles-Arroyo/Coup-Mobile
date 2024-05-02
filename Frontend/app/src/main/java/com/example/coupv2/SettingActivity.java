package com.example.coupv2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.coupv2.app.AppController;
import com.example.coupv2.utils.Const;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SettingActivity extends AppCompatActivity {
    //variables
    private EditText userNameText, userPassText, userEmailText;
    private Button updateUser, updatePass, updateEmail, backButton, upload;
    private ImageView uploadImg;
    private ImageView settingsPicture;
    private String USER_EMAIL;
    private ActivityResultLauncher<String> mGetContent;
    private Uri selectiedUri;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        uploadImg = findViewById(R.id.uploadImage);
        userNameText = findViewById(R.id.settings_username_edt);
        userPassText = findViewById(R.id.settings_password_edt);
        updateUser = findViewById(R.id.settings_login_btn);
        updatePass = findViewById(R.id.settings_pass_btn);
        backButton = findViewById(R.id.backBtn);
        USER_EMAIL = Const.getCurrentEmail();
        userEmailText = findViewById(R.id.settings_email_edt);
        updateEmail = findViewById(R.id.settings_email_btn);
        settingsPicture = findViewById(R.id.settings_icon);
        Animation spin = AnimationUtils.loadAnimation(this, R.anim.spinning);
        upload = findViewById(R.id.settings_image_upload_btn);
        settingsPicture.startAnimation(spin);

        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    // Handle the returned Uri
                    if (uri != null) {
                        selectiedUri = uri;
                        uploadImg.setImageURI(uri);
                        upload.setVisibility(View.VISIBLE);

                    }
        });
        uploadImg.setOnClickListener(v -> {
            mGetContent.launch("image/*");
        });

        if (selectiedUri == null) {
            upload.setVisibility(View.GONE);
        }


        upload.setOnClickListener(v -> uploadImage());

        updateUser.setOnClickListener(v -> {
            String username = userNameText.getText().toString();
            if (!username.isEmpty()) {
                updateUserSettings(username);
            } else {
                Toast.makeText(SettingActivity.this, "Please enter a username", Toast.LENGTH_SHORT).show();
            }
        });

        updatePass.setOnClickListener(v -> {
            String password = userPassText.getText().toString();
            if (!password.isEmpty()) {
                updateUserPassword(password);
            } else {
                Toast.makeText(SettingActivity.this, "Please enter a password", Toast.LENGTH_SHORT).show();
            }
        });

        updateEmail.setOnClickListener(v -> {
            String email = userEmailText.getText().toString();
            if (!email.isEmpty()) {
                updateUserEmail(email);
            } else {
                Toast.makeText(SettingActivity.this, "Please enter a password", Toast.LENGTH_SHORT).show();
            }
        });

        backButton.setOnClickListener(v -> {
            // Start the rules activity
            Intent intent = new Intent(SettingActivity.this, MenuActivity.class);
            startActivity(intent);
        });
    }


    /**
     * Uploads an image to a remote server using a multipart Volley request.
     *
     * This method creates and executes a multipart request using the Volley library to upload
     * an image to a predefined server endpoint. The image data is sent as a byte array and the
     * request is configured to handle multipart/form-data content type. The server is expected
     * to accept the image with a specific key ("image") in the request.
     *
     */
//    private void uploadImage() {
//        String UPLOAD_URL = "http://coms-309-023.class.las.iastate.edu:8080/updateProfile/" + USER_EMAIL;
//        byte[] imageData = convertImageUriToBytes(selectiedUri);
//
//        if (imageData != null && imageData.length > 0) {
//            ImageRequest multipartRequest = new ImageRequest(
//                    UPLOAD_URL,
//                    response -> {
//                        Log.d("Upload", "Response: " + response);
//                        Toast.makeText(this, "Image uploaded successfully!", Toast.LENGTH_SHORT).show();
//                    },
//                    0, // Width, set to 0 for original width
//                    0, // Height, set to 0 for original height
//                    ImageView.ScaleType.CENTER_INSIDE, // ScaleType
//                    Bitmap.Config.ARGB_8888, // Bitmap config
//                    error -> {
//                        // Handle error
//                        Toast.makeText(this, "Failed to upload image: " + error.getMessage(), Toast.LENGTH_LONG).show();
//                        Log.e("Upload", "Error: " + error.getMessage());
//                    }
//            ) {
//                @Override
//                public byte[] getBody() {
//                    return imageData; // Set the image data as the body of the request
//                }
//
//                @Override
//                public String getBodyContentType() {
//                    return "image/jpeg"; // Set the content type according to your image format (e.g., "image/jpeg", "image/png")
//                }
//            };
//
//            // Add the request to the request queue
//            AppController.getInstance().addToRequestQueue(multipartRequest);
//        } else {
//            Toast.makeText(this, "Image data is null or empty", Toast.LENGTH_SHORT).show();
//        }
//    }




    /**
     * Converts the given image URI to a byte array.
     *
     * This method takes a URI pointing to an image and converts it into a byte array. The conversion
     * involves opening an InputStream from the content resolver using the provided URI, and then
     * reading the content into a byte array. This byte array represents the binary data of the image,
     * which can be used for various purposes such as uploading the image to a server.
     *
     * @param imageUri The URI of the image to be converted. This should be a content URI that points
     *                 to an image resource accessible through the content resolver.
     * @return A byte array representing the image data, or null if the conversion fails.
     * @throws IOException If an I/O error occurs while reading from the InputStream.
     */
    private byte[] convertImageUriToBytes(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }

            return byteBuffer.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void uploadImage() {
        String UPLOAD_URL = "http://coms-309-023.class.las.iastate.edu:8080/updateProfile/" + USER_EMAIL;
        byte[] imageData = convertImageUriToBytes(selectiedUri);

        if (imageData != null && imageData.length > 0) {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, UPLOAD_URL, null,
                    response -> {
                        Log.d("Upload", "Response: " + response);
                        Toast.makeText(this, "Image uploaded successfully!", Toast.LENGTH_SHORT).show();
                    },
                    error -> {
                        Toast.makeText(this, "Failed to upload image: " + error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("Upload", "Error: " + error.getMessage());
                    }
            ) {
                @Override
                public byte[] getBody() {
                    return imageData;
                }

                @Override
                public String getBodyContentType() {
                    return "image/jpeg";
                }
            };

            // Add the JsonObjectRequest to the request queue
            AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        } else {
            Toast.makeText(this, "Image data is null or empty", Toast.LENGTH_SHORT).show();
        }
    }


    private void updateUserSettings(String username) {
        String url = "http://coms-309-023.class.las.iastate.edu:8080/changeName/" + USER_EMAIL;

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("name", username);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(SettingActivity.this, "Error creating update request", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue requestQueue = AppController.getInstance().getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonRequest,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            Toast.makeText(SettingActivity.this, "Settings updated successfully", Toast.LENGTH_SHORT).show();
                            Intent menuIntent = new Intent(SettingActivity.this, MenuActivity.class);
                            startActivity(menuIntent);
                            finish();
                        } else {
                            Toast.makeText(SettingActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(SettingActivity.this, "Invalid response from server", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    if (error.networkResponse != null) {
                        String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                        Log.e("SettingActivity", "Network Response Body: " + responseBody);
                        try {
                            new JSONObject(responseBody);
                        } catch (JSONException e) {
                            Log.e("SettingActivity", "Response is not valid JSON", e);
                        }
                    } else {
                        Log.e("SettingActivity", "Network Error: " + error.toString());
                    }
                    Toast.makeText(SettingActivity.this, "Network Error: " + error.toString(), Toast.LENGTH_LONG).show();
                }

        );

        requestQueue.add(jsonObjectRequest);
    }

    private void updateUserPassword(String userPassword) {
        String url = "http://coms-309-023.class.las.iastate.edu:8080/changePass/" + USER_EMAIL;

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("password", userPassword);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(SettingActivity.this, "Error creating update request", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue requestQueue = AppController.getInstance().getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonRequest,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            Toast.makeText(SettingActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                            // Start MenuActivity since the update was successful
                            Intent menuIntent = new Intent(SettingActivity.this, MenuActivity.class);
                            startActivity(menuIntent);
                            finish(); // Close the current activity
                        } else {
                            Toast.makeText(SettingActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(SettingActivity.this, "Invalid response from server", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    if (error.networkResponse != null) {
                        String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                        Log.e("SettingActivity", "Network Response Body: " + responseBody);
                        try {
                            new JSONObject(responseBody); // Attempt to parse the response body as JSON
                            // Handle the JSON object or array here
                        } catch (JSONException e) {
                            // The response is not in JSON format
                            Log.e("SettingActivity", "Response is not valid JSON", e);
                        }
                    } else {
                        Log.e("SettingActivity", "Network Error: " + error.toString());
                    }
                    Toast.makeText(SettingActivity.this, "Network Error: " + error.toString(), Toast.LENGTH_LONG).show();
                }

        );

        requestQueue.add(jsonObjectRequest);
    }

    private void updateUserEmail(String email) {
        String url = "http://coms-309-023.class.las.iastate.edu:8080/changeEmail/" + USER_EMAIL;

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("userEmail", email); // newPassword is the updated password provided by the user
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(SettingActivity.this, "Error creating update request", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue requestQueue = AppController.getInstance().getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonRequest,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            Toast.makeText(SettingActivity.this, "email updated successfully", Toast.LENGTH_SHORT).show();
                            // Start MenuActivity since the update was successful
                            Intent menuIntent = new Intent(SettingActivity.this, MenuActivity.class);
                            startActivity(menuIntent);
                            Const.setCurrentEmail(email);
                            finish(); // Close the current activity
                        } else {
                            Toast.makeText(SettingActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(SettingActivity.this, "Invalid response from server", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    if (error.networkResponse != null) {
                        String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                        Log.e("SettingActivity", "Network Response Body: " + responseBody);
                        try {
                            new JSONObject(responseBody); // Attempt to parse the response body as JSON
                            // Handle the JSON object or array here
                        } catch (JSONException e) {
                            // The response is not in JSON format
                            Log.e("SettingActivity", "Response is not valid JSON", e);
                        }
                    } else {
                        Log.e("SettingActivity", "Network Error: " + error.toString());
                    }
                    Toast.makeText(SettingActivity.this, "Network Error: " + error.toString(), Toast.LENGTH_LONG).show();
                }

        );

        requestQueue.add(jsonObjectRequest);
    }
}

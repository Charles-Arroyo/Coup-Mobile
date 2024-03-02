package com.example.coupv2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class FriendsActivity extends AppCompatActivity {

    private EditText friendNumberEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        friendNumberEditText = findViewById(R.id.friend_number_edittext);
        // You can add additional setup code for FriendsActivity if needed
    }

    // Method to handle friend button clicks
    public void onFriendButtonClick(View view) {
        // You can customize this method based on what you want to do when a friend button is clicked
        // For example, you might want to perform some action related to the selected friend
        // You can use the view's ID or tag to identify which friend button was clicked

        // For now, let's just print a message
        Button clickedButton = (Button) view;
        String friendName = clickedButton.getText().toString();
        Toast.makeText(this, "Clicked on friend: " + friendName, Toast.LENGTH_SHORT).show();
    }

    // Method to handle "Add Friend" button click
    public void onAddFriendClick(View view) {
        // Retrieve the friend's number from the EditText
        String friendNumber = friendNumberEditText.getText().toString();

        if (!friendNumber.isEmpty()) {
            // You can implement logic to add a friend here
            Toast.makeText(this, "Adding friend with number: " + friendNumber, Toast.LENGTH_SHORT).show();
            // Clear the EditText after adding the friend
            friendNumberEditText.setText("");
        } else {
            // Display a message if the EditText is empty
            Toast.makeText(this, "Please enter a friend's number", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to handle "Delete Friend" button click
    public void onDeleteFriendClick(View view) {
        // You can implement logic to delete a friend here
        Toast.makeText(this, "Delete Friend clicked", Toast.LENGTH_SHORT).show();
    }
}

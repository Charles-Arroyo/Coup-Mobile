package com.example.coupv2;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class RulesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        // You can add additional setup code for RulesActivity if needed

        // Show rules overlay initially if desired
        showRulesOverlay();
    }

    private void showRulesOverlay() {
        // Create a BottomSheetDialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        // Inflate the layout for the rules overlay
        View view = getLayoutInflater().inflate(R.layout.activity_rules, null);
        // Set the layout to the dialog
        bottomSheetDialog.setContentView(view);

        // Customize the view and handle interactions

        // Show the dialog
        bottomSheetDialog.show();
    }

    // Method to handle the "Close Rules Overlay" button click
    public void onCloseButtonClick(View view) {
        // Add any logic needed before closing the overlay

        // Close the BottomSheetDialog
        finish();
    }
}

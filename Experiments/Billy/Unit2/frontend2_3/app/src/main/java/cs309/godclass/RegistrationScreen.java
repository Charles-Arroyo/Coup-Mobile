package cs309.godclass;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import cs309.godclass.Logic.RegistrationLogic;
import cs309.godclass.Network.ServerRequest;

public class RegistrationScreen extends AppCompatActivity implements IView {
    TextView nameTextView;
    TextView emailTextView;
    TextView passwordTextView;
    public TextView registerErrorTextView;
    Button registerSubmitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new AppController();
        setContentView(R.layout.activity_registration_screen);
        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        nameTextView = findViewById(R.id.nameTextField);
        emailTextView = findViewById(R.id.emailTextField);
        passwordTextView = findViewById(R.id.passwordTextField);
        registerErrorTextView = findViewById(R.id.registerErrorMessageField);
        registerSubmitButton = findViewById(R.id.registerSubmitButton);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            nameTextView.setAutofillHints(View.AUTOFILL_HINT_NAME);
            emailTextView.setAutofillHints(View.AUTOFILL_HINT_USERNAME);
            passwordTextView.setAutofillHints(View.AUTOFILL_HINT_PASSWORD);
        }

        ServerRequest serverRequest = new ServerRequest();
        final RegistrationLogic logic = new RegistrationLogic(this, serverRequest);

        registerSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String name = nameTextView.getText().toString();
                    String email = emailTextView.getText().toString();
                    String password = passwordTextView.getText().toString();
                    logic.registerUser(name, email, password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void showText(String s) {
        registerErrorTextView.setText(s);
        registerErrorTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void toastText(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
}

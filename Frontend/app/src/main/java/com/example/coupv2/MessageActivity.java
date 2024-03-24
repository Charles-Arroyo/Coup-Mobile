package com.example.coupv2;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coupv2.utils.Const;

import org.java_websocket.handshake.ServerHandshake;

import java.util.ArrayList;


public class MessageActivity extends AppCompatActivity implements WebSocketListener {

    private String BASE_URL = "ws://coms-309-023.class.las.iastate.edu:8080/chat/";
//    private String BASE_URL = "ws://10.0.2.2:8080/chat/";

    private Button sendBtn;
    private EditText msg;
    private String user;
    private ListView lvMessages;
    private ArrayList<String> messagesList = new ArrayList<>();
    private ArrayAdapter<String> messagesAdapter;



    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glb_msg);

        user = Const.getCurrentEmail();
        sendBtn = findViewById(R.id.send_btn);
        msg = findViewById(R.id.msg);
        lvMessages = findViewById(R.id.messages);

        messagesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messagesList);
        lvMessages.setAdapter(messagesAdapter);

        String serverUrl = BASE_URL + user;

        WebSocketManager.getInstance().connectWebSocket(serverUrl);
        WebSocketManager.getInstance().setWebSocketListener(MessageActivity.this);


        sendBtn.setOnClickListener(v -> {
            try {
                // Send only the message text to the server
                String messageToSend = msg.getText().toString().trim();
                WebSocketManager.getInstance().sendMessage(messageToSend);
                // Clear the input field after sending the message
                msg.setText("");
            } catch (Exception e) {
                Log.d("ExceptionSendMessage:", e.getMessage());
            }
        });


    }


    @Override
    public void onWebSocketMessage(String message) {
        runOnUiThread(() -> {
            messagesList.add(message);
            messagesAdapter.notifyDataSetChanged();
            lvMessages.setSelection(messagesList.size() - 1);
        });
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        runOnUiThread(() -> {
            messagesList.add("Connection closed by " + (remote ? "server" : "local") + ". Reason: " + reason);
            messagesAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onWebSocketError(Exception ex) {
        runOnUiThread(() -> {
            messagesList.add("WebSocket error: " + ex.getMessage());
            messagesAdapter.notifyDataSetChanged();
        });
    }
}

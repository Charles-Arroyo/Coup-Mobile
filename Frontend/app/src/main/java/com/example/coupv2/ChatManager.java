package com.example.coupv2;

import java.util.ArrayList;
import java.util.List;

public class ChatManager {
    private static ChatManager instance;
    private List<String[]> messages = new ArrayList<>();

    private ChatManager() {} // Private constructor to ensure Singleton pattern

    public static synchronized ChatManager getInstance() {
        if (instance == null) {
            instance = new ChatManager();
        }
        return instance;
    }

    public void addMessage(String username, String message) {
        messages.add(new String[]{username, message});
    }

    public List<String[]> getMessages() {
        return messages;
    }

    public void clearMessages() {
        messages.clear();
    }
}

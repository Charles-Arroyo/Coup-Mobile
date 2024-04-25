package com.example.coupv2;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coupv2.utils.Const;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

public class ActionActivity extends AppCompatActivity implements WebSocketListener {
    //        need coins here atleast
    //keep websocket open from LobbyActivity
    @Override
    protected void onResume() {
        super.onResume();
        //dont think i need this
//        WebSocketManager.getInstance().setWebSocketListener(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action); // link to Play activity XML

        // Get the intent that started this activity
        Intent intent = getIntent();
        // Retrieve the data from the intent
        String player2 = intent.getStringExtra("Player2Key");
        String player3 = intent.getStringExtra("Player3Key");
        String player4 = intent.getStringExtra("Player4Key");
        //Views
        ImageView incomeIcon1 = findViewById(R.id.whiteshape1);
        ImageView aidIcon2 =  findViewById(R.id.whiteshape2);
        ImageView taxIcon3 = findViewById(R.id.whiteshape3);
        ImageView stealIcon4 = findViewById(R.id.whiteshape4);
        ImageView exchangeIcon5 = findViewById(R.id.whiteshape5);
        ImageView assassinIcon6 = findViewById(R.id.whiteshape6);
        ImageView coupIcon7 = findViewById(R.id.whiteshape7);
        Button backBtn = findViewById(R.id.back_button);

        incomeIcon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("playerEmail", Const.getCurrentEmail());
                    jsonObject.put("move", "*Income");
                    jsonObject.put("targetPlayer", "null");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                String jsonStr = jsonObject.toString();
                WebSocketManager.getInstance().sendMessage(jsonStr);
                Log.d("WebSocket", "sent");
//                go to new activity when clicked on
                Intent intent = new Intent(ActionActivity.this, PlayActivity.class);
                startActivity(intent);
            }
        });

        aidIcon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("playerEmail", Const.getCurrentEmail());
                    jsonObject.put("move", " *Aid");
                    jsonObject.put("targetPlayer", "null");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                String jsonStr = jsonObject.toString();
                WebSocketManager.getInstance().sendMessage(jsonStr);
//                go to new activity when clicked on
                Intent intent = new Intent(ActionActivity.this, PlayActivity.class);
                startActivity(intent);
            }
        });
        taxIcon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("playerEmail", Const.getCurrentEmail());
                    jsonObject.put("move", "*Tax");
                    jsonObject.put("targetPlayer", "null");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                String jsonStr = jsonObject.toString();
                WebSocketManager.getInstance().sendMessage(jsonStr);
//                go to new activity when clicked on
                Intent intent = new Intent(ActionActivity.this, PlayActivity.class);
                startActivity(intent);
            }
        });
        stealIcon4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creating the PopupMenu
                PopupMenu popup = new PopupMenu(ActionActivity.this, stealIcon4);
                popup.getMenuInflater().inflate(R.menu.player_options_menu, popup.getMenu());
                // Dynamically set the menu items visibility based on player names
                if (player2 != null && !player2.isEmpty()) {
                    MenuItem item1 = popup.getMenu().findItem(R.id.player2Item);
                    item1.setTitle(player2);
                } else {
                    popup.getMenu().removeItem(R.id.player2Item);  //i
                }

                if (player3 != null && !player3.isEmpty()) {
                    MenuItem item2 = popup.getMenu().findItem(R.id.player3Item);
                    item2.setTitle(player3);
                } else {
                    popup.getMenu().removeItem(R.id.player3Item);
                }

                if (player4 != null && !player4.isEmpty()) {
                    MenuItem item3 = popup.getMenu().findItem(R.id.player4Item);
                    item3.setTitle(player4);
                } else {
                    popup.getMenu().removeItem(R.id.player4Item);
                }
                // Implementing click events on popup menu items
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        // Variable to hold the selected player
                        String whatPlayer = null;
                        // Check which item was clicked and set the selected player accordingly
                        int id = item.getItemId();
                        if (id == R.id.player2Item) {
                            whatPlayer = player2;
                        } else if (id == R.id.player3Item) {
                            whatPlayer = player3;
                        } else if (id == R.id.player4Item) {
                            whatPlayer = player4;
                        }
                        // After picking the player, send the WebSocket message
                        if (whatPlayer != null) {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("playerEmail", Const.getCurrentEmail());
                                jsonObject.put("move", "*Steal");
                                jsonObject.put("targetPlayer", whatPlayer);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            String jsonStr = jsonObject.toString();
                            WebSocketManager.getInstance().sendMessage(jsonStr);

                            // Now, move to the new activity
                            Intent intent = new Intent(ActionActivity.this, PlayActivity.class);
                            startActivity(intent);
                        }
                        return true;
                    }
                });

                // Showing the popup
                popup.show();
            }
        });
        exchangeIcon5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("playerEmail", Const.getCurrentEmail());
                    jsonObject.put("move", "*Exchange");
                    jsonObject.put("targetPlayer", "null");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                String jsonStr = jsonObject.toString();
                WebSocketManager.getInstance().sendMessage(jsonStr);
//                go to new activity when clicked on
                Intent intent = new Intent(ActionActivity.this, PlayActivity.class);
                startActivity(intent);
            }
        });
        assassinIcon6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creating the PopupMenu
                PopupMenu popup = new PopupMenu(ActionActivity.this, assassinIcon6);
                popup.getMenuInflater().inflate(R.menu.player_options_menu, popup.getMenu());
                // Dynamically set the menu items visibility based on player names
                if (player2 != null && !player2.isEmpty()) {
                    MenuItem item1 = popup.getMenu().findItem(R.id.player2Item);
                    item1.setTitle(player2);
                } else {
                    popup.getMenu().removeItem(R.id.player2Item);  //i
                }

                if (player3 != null && !player3.isEmpty()) {
                    MenuItem item2 = popup.getMenu().findItem(R.id.player3Item);
                    item2.setTitle(player3);
                } else {
                    popup.getMenu().removeItem(R.id.player3Item);
                }

                if (player4 != null && !player4.isEmpty()) {
                    MenuItem item3 = popup.getMenu().findItem(R.id.player4Item);
                    item3.setTitle(player4);
                } else {
                    popup.getMenu().removeItem(R.id.player4Item);
                }
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        // Variable to hold the selected player
                        String whatPlayer = null;
                        // Check which item was clicked and set the selected player accordingly
                        int id = item.getItemId();
                        if (id == R.id.player2Item) {
                            whatPlayer = player2;
                        } else if (id == R.id.player3Item) {
                            whatPlayer = player3;
                        } else if (id == R.id.player4Item) {
                            whatPlayer = player4;
                        }
                        // After picking the player, send the WebSocket message
                        if (whatPlayer != null) {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("playerEmail", Const.getCurrentEmail());
                                jsonObject.put("move", "*Assassinate");
                                jsonObject.put("targetPlayer", whatPlayer);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            String jsonStr = jsonObject.toString();
                            WebSocketManager.getInstance().sendMessage(jsonStr);

                            // Now, move to the new activity
                            Intent intent = new Intent(ActionActivity.this, PlayActivity.class);
                            startActivity(intent);
                        }
                        return true;
                    }
                });

                // Showing the popup
                popup.show();
            }
        });
        coupIcon7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creating the PopupMenu
                PopupMenu popup = new PopupMenu(ActionActivity.this, coupIcon7);
                popup.getMenuInflater().inflate(R.menu.player_options_menu, popup.getMenu());
                // Dynamically set the menu items visibility based on player names
                if (player2 != null && !player2.isEmpty()) {
                    MenuItem item1 = popup.getMenu().findItem(R.id.player2Item);
                    item1.setTitle(player2);
                } else {
                    popup.getMenu().removeItem(R.id.player2Item);  //i
                }

                if (player3 != null && !player3.isEmpty()) {
                    MenuItem item2 = popup.getMenu().findItem(R.id.player3Item);
                    item2.setTitle(player3);
                } else {
                    popup.getMenu().removeItem(R.id.player3Item);
                }

                if (player4 != null && !player4.isEmpty()) {
                    MenuItem item3 = popup.getMenu().findItem(R.id.player4Item);
                    item3.setTitle(player4);
                } else {
                    popup.getMenu().removeItem(R.id.player4Item);
                }
                // Implementing click events on popup menu items
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        // Variable to hold the selected player
                        String whatPlayer = null;
                        // Check which item was clicked and set the selected player accordingly
                        int id = item.getItemId();
                        if (id == R.id.player2Item) {
                            whatPlayer = player2;
                        } else if (id == R.id.player3Item) {
                            whatPlayer = player3;
                        } else if (id == R.id.player4Item) {
                            whatPlayer = player4;
                        }
                        // After picking the player, send the WebSocket message
                        if (whatPlayer != null) {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("playerEmail", Const.getCurrentEmail());
                                jsonObject.put("move", "#Coup");
                                jsonObject.put("targetPlayer", whatPlayer);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            String jsonStr = jsonObject.toString();
                            WebSocketManager.getInstance().sendMessage(jsonStr);

                            // Now, move to the new activity
                            Intent intent = new Intent(ActionActivity.this, PlayActivity.class);
                            startActivity(intent);
                        }
                        return true;
                    }
                });

                // Showing the popup
                popup.show();
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                go to new activity when clicked on
                Intent intent = new Intent(ActionActivity.this, PlayActivity.class);
                startActivity(intent);
            }
        });


    }

    public void createPopup(){

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

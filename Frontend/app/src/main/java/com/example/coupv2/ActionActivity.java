package com.example.coupv2;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.FrameLayout;
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
                    jsonObject.put("move", "income");
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

        aidIcon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("playerEmail", Const.getCurrentEmail());
                    jsonObject.put("move", "foreign aid");
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
                    jsonObject.put("move", "taxes");
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
                PopupMenu popup = new PopupMenu(ActionActivity.this, incomeIcon1);
                popup.getMenuInflater().inflate(R.menu.player_options_menu, popup.getMenu());

                // Implementing click events on popup menu items
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        // Variable to hold the selected player
                        String whatPlayer = null;

                        // Check which item was clicked and set the selected player accordingly
                        int id = item.getItemId();
                        if (id == R.id.player1) {
                            whatPlayer = "Player 1";
                        } else if (id == R.id.player2) {
                            whatPlayer = "Player 2";
                        } else if (id == R.id.player3) {
                            whatPlayer = "Player 3";
                        }
                        // After picking the player, send the WebSocket message
                        if (whatPlayer != null) {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("playerEmail", Const.getCurrentEmail());
                                jsonObject.put("move", "income");
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
                    jsonObject.put("move", "steal");
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
                PopupMenu popup = new PopupMenu(ActionActivity.this, incomeIcon1);
                popup.getMenuInflater().inflate(R.menu.player_options_menu, popup.getMenu());

                // Implementing click events on popup menu items
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        // Variable to hold the selected player
                        String whatPlayer = null;

                        // Check which item was clicked and set the selected player accordingly
                        int id = item.getItemId();
                        if (id == R.id.player1) {
                            whatPlayer = "Player 1";
                        } else if (id == R.id.player2) {
                            whatPlayer = "Player 2";
                        } else if (id == R.id.player3) {
                            whatPlayer = "Player 3";
                        }
                        // After picking the player, send the WebSocket message
                        if (whatPlayer != null) {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("playerEmail", Const.getCurrentEmail());
                                jsonObject.put("move", "income");
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
                PopupMenu popup = new PopupMenu(ActionActivity.this, incomeIcon1);
                popup.getMenuInflater().inflate(R.menu.player_options_menu, popup.getMenu());

                // Implementing click events on popup menu items
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        // Variable to hold the selected player
                        String whatPlayer = null;

                        // Check which item was clicked and set the selected player accordingly
                        int id = item.getItemId();
                        if (id == R.id.player1) {
                            whatPlayer = "Player 1";
                        } else if (id == R.id.player2) {
                            whatPlayer = "Player 2";
                        } else if (id == R.id.player3) {
                            whatPlayer = "Player 3";
                        }
                        // After picking the player, send the WebSocket message
                        if (whatPlayer != null) {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("playerEmail", Const.getCurrentEmail());
                                jsonObject.put("move", "income");
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

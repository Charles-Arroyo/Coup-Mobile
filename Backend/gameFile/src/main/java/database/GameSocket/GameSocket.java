package database.GameSocket;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import database.Friends.FriendRepository;
import database.Users.UserRepository;
import database.Websocketconfig.WebsocketConfig;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

public class GameSocket {
}

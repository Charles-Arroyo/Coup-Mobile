//package database.Game;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Hashtable;
//import java.util.List;
//import java.util.Map;
//
//import database.Chat.ChatSocket;
//import database.Chat.Message;
//import database.Friends.FriendRepository;
//import database.Lobby.Lobby;
//import database.Lobby.LobbyRepository;
//import database.Users.UserRepository;
//import database.Websocketconfig.WebsocketConfig;
//import jakarta.websocket.OnClose;
//import jakarta.websocket.OnError;
//import jakarta.websocket.OnMessage;
//import jakarta.websocket.OnOpen;
//import jakarta.websocket.Session;
//import jakarta.websocket.server.PathParam;
//import jakarta.websocket.server.ServerEndpoint;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//
//@Controller //Signify this is a controller
//@ServerEndpoint(value = "/game/{lobbyId}") // getting lobby ID so when know what player to add to game
//public class GameSocket {
//
//
//    private static GameRepository gameRepository;
//
//
//    @Autowired
//    public void setGameRepository(GameRepository repo) {
//        gameRepository = repo;
//    }
//
//    private static Map<Session, String> sessionLobbyIdMap = new Hashtable<>();
//    private static Map<String, Session> lobbyIdSessionMap = new Hashtable<>();
//
//    private final Logger logger = LoggerFactory.getLogger(ChatSocket.class);
//
//    @OnOpen
//    public void onOpen(Session session, @PathParam("lobbyId") int lobbyId) throws IOException {
//
////        LobbyRepository lobbyRepository = WebsocketConfig.getLobbyRepository();
////
////        sessionLobbyIdMap.put(session, lobbyId+"");
////        lobbyIdSessionMap.put(lobbyId +"", session);
////
////        Lobby lobby = lobbyRepository.findById(lobbyId);
////
////        List<Player> players = new ArrayList<>(); // Create an Array list of Players
////        Game game = new Game(players); //Pass in Deck and Array List
////        game.initGame(lobby.getUser1(),lobby.getUser2(),lobby.getUser3(),lobby.getUser4()); // Sends four players, see init game method
////        broadcast(game.toString());
//////      System.out.println("Player 1 turn is over");
//////      game.nextTurn();
//////      System.out.println();
//////       ystem.out.println(game.toString());
//
//    }
//
//
//    @OnMessage
//    public void messageUsers(Session session, String message) throws IOException {
//
//    }
//
//    @OnClose
//    public void onClose(Session session) throws IOException {
//        logger.info("Entered into Close");
//
//        // remove the user connection information
//        String lobbyId = sessionLobbyIdMap.get(session);
//
//        sessionLobbyIdMap.remove(session);
//        lobbyIdSessionMap.remove(lobbyId);
//
//
//        // broadcast that the user disconnected
//        String message = "Game:" + lobbyId + " disconnected";
//        broadcast(message);
//    }
//
//
//    @OnError
//    public void onError(Session session, Throwable throwable) {
//        // Do error handling here
//        logger.info("Entered into Error");
//        throwable.printStackTrace();
//    }
//
//    private void broadcast(String message) {
//        sessionLobbyIdMap.forEach((session, lobbyId) -> {
//            try {
//                session.getBasicRemote().sendText(message);
//            } catch (IOException e) {
//                logger.info("Exception: " + e.getMessage().toString());
//                e.printStackTrace();
//            }
//
//        });
//
//    }
//}

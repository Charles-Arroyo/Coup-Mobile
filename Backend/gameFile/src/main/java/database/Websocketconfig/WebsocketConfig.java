
package database.Websocketconfig;
import database.Friends.FriendRepository;
import database.Lobby.LobbyRepository;
import database.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * Author: Charles Arroyo
 * This is the WebSocket Config Class for any websocket. Add Necessary Repos here.
 */

@Configuration
public class WebsocketConfig {
//    public static UserRepository userRepository;
//
//    @Autowired
//    public void setUserRepository(UserRepository userRepository) {
//        WebsocketConfig.userRepository = userRepository;
//    }
//
//    public static UserRepository getUserRepository() {
//        return userRepository;
//    }
//
//
//    public static FriendRepository friendRepository;
//
//    @Autowired
//    public void setFriendRepository(FriendRepository friendRepository) {
//        WebsocketConfig.friendRepository = friendRepository;
//    }
//
//    public static FriendRepository getFriendRepository() {
//        return friendRepository;
//    }
//
//
//    private static LobbyRepository lobbyRepository;
//
//    @Autowired
//    public void setLobbyRepository(LobbyRepository lobbyRepository) {
//        WebsocketConfig.lobbyRepository = lobbyRepository;
//    }
//
//    public static LobbyRepository getLobbyRepository() {
//        return lobbyRepository;
//    }
//
////    private static UserRepository userRepository;
////
////    @Autowired
////    public void setUserRepository(UserRepository userRepository) {
////        WebsocketConfig.userRepository = userRepository;
////    }
////
////    public static UserRepository getUserRepository() {
////        return userRepository;
////    }
////
////
////    private static FriendRepository friendRepository;
////
////    @Autowired
////    public void setFriendRepository(FriendRepository friendRepository) {
////        WebsocketConfig.friendRepository = friendRepository;
////    }
////
////    public static FriendRepository getFriendRepository() {
////        return friendRepository;
////    }
////
////
////    private static LobbyRepository lobbyRepository;
////
////    @Autowired
////    public void setLobbyRepository(LobbyRepository lobbyRepository) {
////        WebsocketConfig.lobbyRepository = lobbyRepository;
////    }
////
////    public static LobbyRepository getLobbyRepository() {
////        return lobbyRepository;
////    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}

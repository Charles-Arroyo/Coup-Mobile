package database.Websocketconfig;

import database.Friends.FriendRepository;
import database.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
@Configuration
public class WebsocketConfig {
    private static UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        WebsocketConfig.userRepository = userRepository;
    }

    public static UserRepository getUserRepository() {
        return userRepository;
    }


    private static FriendRepository friendRepository;

    @Autowired
    public void setFriendRepository(FriendRepository friendRepository) {
        WebsocketConfig.friendRepository = friendRepository;
    }

    public static FriendRepository getFriendRepository() {
        return friendRepository;
    }


    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}

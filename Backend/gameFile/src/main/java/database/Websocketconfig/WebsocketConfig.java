
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

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}

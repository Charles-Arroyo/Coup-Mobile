package database.Signup;


import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Controller;

@Controller      // this is needed for this to be an endpoint to springboot
@ServerEndpoint(value = "/signup/{username}")  // this is Websocket url
public class SignupSocket {
}

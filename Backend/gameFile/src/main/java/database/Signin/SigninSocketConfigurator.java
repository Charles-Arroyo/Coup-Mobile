package database.Signin;

import jakarta.websocket.server.ServerEndpointConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SigninSocketConfigurator extends ServerEndpointConfig.Configurator {
    private static ApplicationContext applicationContext;

    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
        return applicationContext.getBean(endpointClass);
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        SigninSocketConfigurator.applicationContext = applicationContext;
    }
}
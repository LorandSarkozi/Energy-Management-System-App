package ro.tuc.ds2020.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Configure the broker to handle messages sent to /topic/chat
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Register the chat WebSocket endpoint
        registry.addEndpoint("/chat")
                .setAllowedOriginPatterns("*"); // Allow cross-origin requests

        registry.addEndpoint("/typing")
                .setAllowedOrigins("*");

        registry.addEndpoint("/read")
                .setAllowedOrigins("*");

    }
}

package com.pb.tel.config;

import com.pb.tel.service.websocket.CustomHandshakeHandler;
import com.pb.tel.service.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.logging.Logger;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	
	private final Logger log = Logger.getLogger(WebSocketConfig.class.getCanonicalName());

	@Autowired
	private Environment environment;

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		log.info("WebSocketConfig 1:   StompEndpointRegistry: "+registry);
		registry
				.addEndpoint("/wss")
				.setHandshakeHandler(new CustomHandshakeHandler())
				.setAllowedOrigins("*")
				.withSockJS()
				.setClientLibraryUrl(environment.getProperty("main.address")+"/js/sockjs.js");
	}

	@Bean
	public WebSocketServer getWebSocketServer(SimpMessagingTemplate template){
		WebSocketServer webSocketServer = new WebSocketServer(template);
		return webSocketServer;
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		log.info("WebSocketConfig 2:   MessageBrokerRegistry: "+registry);
		registry.setApplicationDestinationPrefixes("/method");
		registry.enableSimpleBroker("/queue/", "/topic/");
//		registry.enableStompBrokerRelay("/queue/", "/topic/");
//		registry.setPreservePublishOrder(true);
	}
}

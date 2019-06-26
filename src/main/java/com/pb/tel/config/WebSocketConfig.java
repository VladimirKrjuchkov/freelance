package com.pb.tel.config;

import com.pb.tel.utils.Utils;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.logging.Logger;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	
	private final Logger log = Logger.getLogger(WebSocketConfig.class.getCanonicalName());	
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		log.info("WebSocketConfig 1:   StompEndpointRegistry: "+registry);			
		registry.addEndpoint("/checked/wss").setAllowedOrigins("*").withSockJS().setClientLibraryUrl(Utils.property.getProperty("main.domain")+"/js/sockjs.js");/*.setInterceptors(new HandshakeInterceptor() {  //Это просто опыт чтоб понять что тут можно поделать
			
			@Override
			public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
					Map<String, Object> attributes) throws Exception {
								
				// TODO Auto-generated method stub
				log.info("@$@   request instanceof HttpServletRequest: "+(request instanceof HttpServletRequest));
				if(request instanceof HttpServletRequest){
					HttpServletRequest req = (HttpServletRequest)request;
					req.getSession().setMaxInactiveInterval(120);
				}
				log.info("@$@   attributes: "+attributes);
				attributes.forEach((key, value)->log.info("key: "+key+"     value: "+value));				
				return true;
				
			}
			
			@Override
			public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
					Exception exception) {
				// TODO Auto-generated method stub
				log.info("@$@     request instanceof HttpServletRequest: "+(request instanceof HttpServletRequest));
				
			}
		});*/
	}
	
	//Это все опыт для того чтоб достучаться до WebSocketSession
//	@Override
//	public void configureWebSocketTransport(WebSocketTransportRegistration registry) {		
//		registry.addDecoratorFactory(new WebSocketHandlerDecoratorFactory() {
//			
//			@Override
//			public WebSocketHandler decorate(WebSocketHandler handler) {
//					return new WebSocketHandlerDecorator(handler){
//						
//						@Override
//						public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//							log.info(" ATAS  in  afterConnectionEstablished !!!");
//							log.info("WebSocketSession"+session);
//							log.info("userAccount: "+session.getPrincipal());							
//							UserAccount userAccount = Util.getAccountFromAuthentication((Authentication)session.getPrincipal());
//							log.info("userAccount: "+userAccount);
//							log.info("userAccount.getWebSocketSession: "+userAccount.getWebSocketSession());
//							userAccount.setWebSocketSession(session);
//							super.afterConnectionEstablished(session);
//							//session.
//							//this.delegate.afterConnectionEstablished(session);
//						}
//					};
//				//return null;
//			}
//		});		
//	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {  //Можно еще тут через registry поконфигурить brokerChanel для того чтоб не в один поток с брокером общалось апп это через registry.configureBrokerChannel();		
		log.info("WebSocketConfig 2:   MessageBrokerRegistry: "+registry);
		registry.setApplicationDestinationPrefixes("/method");///app
		registry.enableSimpleBroker("/queue/", "/topic/");		
//		registry.enableStompBrokerRelay("/queue/", "/topic/");		
//		registry.setPreservePublishOrder(true);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//Это все ниже был конфиг каналов. Но пока унас нет необходимости перехватывать сообщения
//	@Override
//	public void configureClientInboundChannel(ChannelRegistration registration) {			
//		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
//		taskExecutor.setCorePoolSize(Runtime.getRuntime().availableProcessors() * 2);
//		taskExecutor.setAllowCoreThreadTimeOut(true);
//		taskExecutor.setQueueCapacity(1500);	
//		taskExecutor.setTaskDecorator(new TaskDecorator() {			
//			@Override
//			public Runnable decorate(Runnable runnable) {				
//				return ()->{int thNumber = inInt.incrementAndGet(); log.info("		#### Start InboundChannel "+thNumber+" THREAD ###");  runnable.run(); log.info("		#### Finish InboundChannel "+thNumber+" THREAD ###");				
//				 };
//			}
//		});
//		registration.taskExecutor(taskExecutor);
//		
//		log.info("WebSocketConfig 3:   ChannelRegistration: "+registration);
//		log.info("WebSocketConfig 3:   logingChannelInterceptor 1: "+logingChannelInterceptor());		
//		//registration.interceptors(logingChannelInterceptor());
//		registration.interceptors(new LogingChannelInterceptor("IN"));				
//	}
//	
//	
//	@Override
//	public void configureClientOutboundChannel(ChannelRegistration registration) {		
//		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
//		taskExecutor.setCorePoolSize(Runtime.getRuntime().availableProcessors() * 2);
//		taskExecutor.setAllowCoreThreadTimeOut(true);
//		taskExecutor.setQueueCapacity(1500);	
//		taskExecutor.setTaskDecorator(new TaskDecorator() {			
//			@Override
//			public Runnable decorate(Runnable runnable) {				
//				return ()->{int thNumber = outInt.incrementAndGet(); log.info("		#### Start OutboundChannel "+thNumber+" THREAD ###");  runnable.run(); log.info("		#### Finish OutboundChannel "+thNumber+" THREAD ###");				
//				};
//				
//			}
//		});
//		registration.taskExecutor(taskExecutor);
//		
//		log.info("WebSocketConfig 4:   ChannelRegistration: "+registration);
//		log.info("WebSocketConfig 4:   logingChannelInterceptor 1: "+logingChannelInterceptor());			
//		//registration.interceptors(logingChannelInterceptor());	
//		registration.interceptors(new LogingChannelInterceptor("OUT"));
//	}
//					
//	@Bean
//	public LogingChannelInterceptor logingChannelInterceptor(){
//		return new LogingChannelInterceptor();
//	}	
}

package com.soc.network.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig(private val socketRelayProperties: SocketRelayProperties): WebSocketMessageBrokerConfigurer {

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableStompBrokerRelay("/topic", "/queue")
            .setAutoStartup(true)
            .setRelayHost(socketRelayProperties.host)
            .setRelayPort(socketRelayProperties.port)
            .setClientLogin(socketRelayProperties.login)
            .setClientPasscode(socketRelayProperties.passcode)
            .setSystemLogin(socketRelayProperties.login)
            .setSystemPasscode(socketRelayProperties.passcode)
            .setUserDestinationBroadcast("/topic/unresolved-user-destination")
        registry.setUserDestinationPrefix("/post/feed")
        registry.setApplicationDestinationPrefixes("/app")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry
            .addEndpoint("/stomp")
    }
}
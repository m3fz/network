package com.soc.network.configuration

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(SocketRelayProperties::class)
class AppPropsConfig {

    @Bean
    fun socketRelayProperties(): SocketRelayProperties {
        return SocketRelayProperties()
    }
}
package com.soc.network.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "socket.relay")
data class SocketRelayProperties(
    var host: String = "",
    var port: Int = 0,
    var login: String = "",
    var passcode: String = ""
)

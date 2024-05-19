package com.soc.network.core

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@SpringBootApplication
@EnableWebSecurity
@EnableScheduling
class NetworkApplication

fun main(args: Array<String>) {
	runApplication<NetworkApplication>(*args)
}

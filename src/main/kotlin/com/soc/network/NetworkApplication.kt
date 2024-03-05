package com.soc.network

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NetworkApplication

fun main(args: Array<String>) {
	runApplication<NetworkApplication>(*args)
}

package com.soc.network.counter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.retry.annotation.EnableRetry

@SpringBootApplication
@EnableRetry
class CounterApplication

fun main(args: Array<String>) {
	runApplication<CounterApplication>(*args)
}

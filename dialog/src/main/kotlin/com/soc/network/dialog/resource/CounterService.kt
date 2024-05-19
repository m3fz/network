package com.soc.network.dialog.resource

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import java.util.*

@Service
class CounterService(
    @Value("\${resource.counter.base-url}") private val baseUrl: String,
    private val restClient: RestClient
) {
    private val TRANSACTION_HEADER: String = "X-Tx-UUID";

    fun increaseCounter(userUuid: UUID, txUuid: UUID) {
        restClient.post()
            .uri("$baseUrl/increase/${userUuid}/1")
            .header(TRANSACTION_HEADER, txUuid.toString())
            .retrieve()
    }

    fun resetCounter(userUuid: UUID, txUuid: UUID) {
        restClient.post()
            .uri("$baseUrl/reset/${userUuid}")
            .header(TRANSACTION_HEADER, txUuid.toString())
            .retrieve()
    }

    fun rollbackCounterTx(userUuid: UUID, txUuid: UUID) {
        restClient.post()
            .uri("$baseUrl/rollback/${userUuid}")
            .header(TRANSACTION_HEADER, txUuid.toString())
            .retrieve()
    }

}
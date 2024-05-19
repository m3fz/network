package com.soc.network.counter.controller

import com.soc.network.counter.service.CounterService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class DialogMessageCounterController(
    private val counterService: CounterService
) {

    @PostMapping("/increase/{userUuid}/{count}")
    @Operation(description = "Увеличение счетчика непрочитанных сообщений для пользователя")
    fun increaseCount(
        @PathVariable("userUuid") userUuid: UUID,
        @PathVariable("count") count: Int,
        @RequestHeader(value = "X-Tx-UUID", required = true) transactionUuid: UUID
    ) {
        counterService.increase(userUuid, count, transactionUuid)
    }

    @PostMapping("/reset/{userUuid}")
    @Operation(description = "Сброс счетчика непрочитанных сообщений для пользователя")
    fun resetCount(
        @PathVariable("userUuid") userUuid: UUID,
        @RequestHeader(value = "X-Tx-UUID", required = true) transactionUuid: UUID
    ) {
        counterService.reset(userUuid, transactionUuid)
    }

    @PostMapping("/rollback/{userUuid}")
    @Operation(description = "Откат счетчика непрочитанных сообщений для пользователя")
    fun rollbackCount(
        @PathVariable("userUuid") userUuid: UUID,
        @RequestHeader(value = "X-Tx-UUID", required = true) transactionUuid: UUID
    ) {
        counterService.rollback(userUuid, transactionUuid)
    }

    @GetMapping("/get/{userUuid}")
    @Operation(description = "Получение счетчика непрочитанных сообщений для пользователя")
    fun getCount(@PathVariable("userUuid") userUuid: UUID): Int {
        return counterService.get(userUuid)
    }
}
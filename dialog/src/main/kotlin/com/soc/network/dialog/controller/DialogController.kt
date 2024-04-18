package com.soc.network.dialog.controller

import com.soc.network.dialog.model.dto.DialogMessageDto
import com.soc.network.dialog.service.DialogService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class DialogController(
    private val dialogService: DialogService
) {

    @PostMapping("/send")
    @Operation(description = "Послание сообщения пользователю")
    fun sendMessage(@RequestBody dialogMessageDto: DialogMessageDto) {
        dialogService.sendMessage(dialogMessageDto)
    }

    @GetMapping("/list")
    @Operation(description = "Диалог между двумя пользователями")
    fun getDialog(@RequestParam(name = "user1") user1Uuid: UUID,
                  @RequestParam(name = "user2") user2Uuid: UUID): List<DialogMessageDto> {
        return dialogService.getDialogBetween(user1Uuid, user2Uuid)
    }
}
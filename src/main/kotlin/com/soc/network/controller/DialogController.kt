package com.soc.network.controller

import com.soc.network.model.dto.DialogMessageDto
import com.soc.network.model.dto.DialogMessageTextDto
import com.soc.network.service.DialogService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/dialog")
@SecurityRequirement(name = "basicAuth")
class DialogController(
    private val dialogService: DialogService
) {

    @PostMapping("/{user_uuid}/send")
    @Operation(description = "Послание сообщения пользователю")
    fun sendMessage(@PathVariable(name = "user_uuid") receiverUUID: UUID,
                    @RequestBody dialogMessageTextDto: DialogMessageTextDto) {
        dialogService.sendMessage(receiverUUID, dialogMessageTextDto.text)
    }

    @GetMapping("/{user_uuid}/list")
    @Operation(description = "Диалог между двумя пользователями")
    fun getDialog(@PathVariable(name = "user_uuid") userUUID: UUID): List<DialogMessageDto> {
        return dialogService.getDialogWithUser(userUUID)
    }
}
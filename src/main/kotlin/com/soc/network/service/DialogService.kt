package com.soc.network.service

import com.soc.network.dao.DialogMessageInMemDao
import com.soc.network.model.dto.DialogMessageDto
import com.soc.network.model.entity.DialogMessageEntity
import org.springframework.stereotype.Service
import java.util.*

@Service
class DialogService(
    private val dialogMessageDao: DialogMessageInMemDao,
    private val userService: UserService
) {

    fun sendMessage(receiverUuid: UUID, message: String) {
        val dialogMessageEntity = DialogMessageEntity(
            senderUuid = userService.getCurrentUuid(),
            receiverUuid = receiverUuid,
            text = message
        )

        dialogMessageDao.createMessage(dialogMessageEntity)
    }

    fun getDialogWithUser(userUuid: UUID): List<DialogMessageDto> {
        return dialogMessageDao.getMessagesBetween(userService.getCurrentUuid(), userUuid).stream()
            .map { toDto(it) }
            .toList()
    }

    private fun toDto(entity: DialogMessageEntity): DialogMessageDto {
        return DialogMessageDto(entity.senderUuid, entity.receiverUuid, entity.text)
    }
}

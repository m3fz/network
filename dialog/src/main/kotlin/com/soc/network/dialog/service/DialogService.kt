package com.soc.network.dialog.service

import com.soc.network.dialog.dao.DialogMessageInMemDao
import com.soc.network.dialog.model.dto.DialogMessageDto
import com.soc.network.dialog.model.entity.DialogMessageEntity
import org.springframework.stereotype.Service
import java.util.*

@Service
class DialogService(
    private val dialogMessageDao: DialogMessageInMemDao,
) {

    fun sendMessage(dialogMessage: DialogMessageDto) {
        val dialogMessageEntity = DialogMessageEntity(
            senderUuid = dialogMessage.senderUuid,
            receiverUuid = dialogMessage.receiverUuid,
            text = dialogMessage.text
        )

        dialogMessageDao.createMessage(dialogMessageEntity)
    }

    fun getDialogBetween(user1Uuid: UUID, user2Uuid: UUID): List<DialogMessageDto> {
        return dialogMessageDao.getMessagesBetween(user1Uuid, user2Uuid).stream()
            .map { toDto(it) }
            .toList()
    }

    private fun toDto(entity: DialogMessageEntity): DialogMessageDto {
        return DialogMessageDto(entity.senderUuid, entity.receiverUuid, entity.text)
    }
}

package com.soc.network.dialog.service

import com.soc.network.dialog.dao.DialogMessageInMemDao
import com.soc.network.dialog.model.dto.DialogMessageDto
import com.soc.network.dialog.model.entity.DialogMessageEntity
import com.soc.network.dialog.resource.CounterService
import org.springframework.stereotype.Service
import java.util.*

@Service
class DialogService(
    private val dialogMessageDao: DialogMessageInMemDao,
    private val counterService: CounterService
) {

    fun sendMessage(dialogMessage: DialogMessageDto) {
        val dialogMessageEntity = DialogMessageEntity(
            senderUuid = dialogMessage.senderUuid,
            receiverUuid = dialogMessage.receiverUuid,
            text = dialogMessage.text
        )

        val transactionUUID: UUID = UUID.randomUUID()
        counterService.increaseCounter(dialogMessage.receiverUuid, transactionUUID)

        try {
        dialogMessageDao.createMessage(dialogMessageEntity)
        } catch (e: Exception) {
            counterService.rollbackCounterTx(dialogMessage.receiverUuid, transactionUUID)
        }
    }

    fun getDialogBetween(user1Uuid: UUID, user2Uuid: UUID): List<DialogMessageDto> {
        val transactionUUID: UUID = UUID.randomUUID()
        counterService.resetCounter(user1Uuid, transactionUUID)

        try {
            return dialogMessageDao.getMessagesBetween(user1Uuid, user2Uuid).stream()
                .map { toDto(it) }
                .toList()
        } catch (e: Exception) {
            counterService.rollbackCounterTx(user1Uuid, transactionUUID)
            throw e
        }
    }

    private fun toDto(entity: DialogMessageEntity): DialogMessageDto {
        return DialogMessageDto(entity.senderUuid, entity.receiverUuid, entity.text)
    }
}

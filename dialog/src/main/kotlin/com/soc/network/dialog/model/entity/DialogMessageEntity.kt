package com.soc.network.dialog.model.entity

import java.time.LocalDateTime
import java.util.*

data class DialogMessageEntity(
    var uuid: UUID = UUID.randomUUID(),
    var senderUuid: UUID,
    var receiverUuid: UUID,
    var text: String,
    var createDt: LocalDateTime = LocalDateTime.now()
)
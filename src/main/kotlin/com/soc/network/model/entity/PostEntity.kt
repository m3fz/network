package com.soc.network.model.entity

import java.time.LocalDateTime
import java.util.*

data class PostEntity(
    var uuid: UUID = UUID.randomUUID(),
    var authorUuid: UUID,
    var text: String,
    var editDt: LocalDateTime = LocalDateTime.now()
)
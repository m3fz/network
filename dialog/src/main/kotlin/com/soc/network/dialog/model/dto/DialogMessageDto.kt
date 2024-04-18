package com.soc.network.dialog.model.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class DialogMessageDto(
    var senderUuid: UUID,
    var receiverUuid: UUID,
    var text: String
)
package com.soc.network.core.model.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class UserUuidDto(
    @JsonProperty("user_id")
    var uuid: UUID
)

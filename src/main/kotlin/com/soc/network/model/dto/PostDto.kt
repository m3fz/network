package com.soc.network.model.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class PostDto(
    val uuid: UUID? = null,
    val text: String,
    val editDt: LocalDateTime = LocalDateTime.now()
): Serializable {
    @JsonProperty("author")
    var authorUuid: UUID? = null

    fun authorUuid(uuid: UUID) {
        authorUuid = uuid
    }
}
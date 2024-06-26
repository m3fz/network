package com.soc.network.core.model.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class DialogMessageTextDto(
    var text: String
)
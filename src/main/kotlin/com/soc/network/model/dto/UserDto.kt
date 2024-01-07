package com.soc.network.model.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.soc.network.model.GenderType
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserDto (
    @JsonIgnore
    var uuid: UUID = UUID.randomUUID(),
    var username: String,
    var password: String,
) {
    @JsonProperty("first_name")
    var firstname: String? = null
    @JsonProperty("second_name")
    var lastname: String? = null
    var age: Int? = null
    var gender: GenderType? = null
    var interests: String? = null
    var city: String? = null
}
package com.soc.network.model.error

class UserNotExistsException(override val message: String?): RuntimeException(message) {
}
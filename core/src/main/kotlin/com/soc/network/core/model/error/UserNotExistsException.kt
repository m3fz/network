package com.soc.network.core.model.error

class UserNotExistsException(override val message: String?): RuntimeException(message) {
}
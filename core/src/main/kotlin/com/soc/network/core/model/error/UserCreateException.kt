package com.soc.network.core.model.error

class UserCreateException(override val message: String?): RuntimeException(message) {
}
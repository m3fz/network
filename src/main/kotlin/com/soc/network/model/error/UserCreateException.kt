package com.soc.network.model.error

class UserCreateException(override val message: String?): RuntimeException(message) {
}
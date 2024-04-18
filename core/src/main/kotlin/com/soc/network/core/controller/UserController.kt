package com.soc.network.core.controller

import com.soc.network.core.model.dto.UserDto
import com.soc.network.core.model.dto.UserUuidDto
import com.soc.network.core.model.error.UserCreateException
import com.soc.network.core.model.error.UserNotExistsException
import com.soc.network.core.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/user")
@SecurityRequirement(name = "basicAuth")
class UserController(
    private val userService: UserService
) {

    @GetMapping("/get/{uuid}")
    @Operation(description = "Получение данных анкеты пользователя")
    fun getUser(@PathVariable("uuid") uuid: UUID): UserDto? {
        return userService.getUserByUuid(uuid)
    }

    @GetMapping("/search")
    @Operation(description = "Поиск анкет")
    fun findUser(
        @RequestParam("first_name") firstname: String,
        @RequestParam("last_name") lastname: String
    ): List<UserDto> {
        return userService.findUsers(firstname, lastname)
    }

    @PostMapping("/register")
    @SecurityRequirement(name = "none")
    @Operation(description = "Регистрация нового пользователя")
    fun createUser(@RequestBody userDto: UserDto): UserUuidDto {
        return userService.createUser(userDto)
    }

    @ExceptionHandler(UserNotExistsException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleUserCreateException(exception: UserNotExistsException): String? {
        return exception.message
    }

    @ExceptionHandler(UserCreateException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleUserCreateException(exception: UserCreateException): String? {
        return exception.message
    }
}
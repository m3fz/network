package com.soc.network.controller

import com.soc.network.model.entity.UserEntity
import com.soc.network.service.FriendService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/friend")
@SecurityRequirement(name = "basicAuth")
class FriendController(
    private val friendService: FriendService
) {

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Вывод списка UUID друзей")
    fun listFriends(@AuthenticationPrincipal user: UserEntity): List<UUID> {
        return friendService.getFriends(user.uuid)
    }

    @PutMapping("/add/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Добавление друга")
    fun addFriend(@PathVariable("uuid") friendUuid: UUID,
                  @AuthenticationPrincipal user: UserEntity
    ) {
        friendService.addFriend(user.uuid, friendUuid)
    }

    @PutMapping("/delete/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Удаление друга")
    fun deleteFriend(@PathVariable("uuid") friendUuid: UUID,
                     @AuthenticationPrincipal user: UserEntity
    ) {
        friendService.deleteFriend(user.uuid, friendUuid)
    }
}

package com.soc.network.core.resource

import com.soc.network.core.model.dto.DialogMessageDto
import com.soc.network.core.service.UserService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import java.util.*

@Service
class DialogService(
    @Value("\${resource.dialog.base-url}") private val baseUrl: String,
    private val userService: UserService,
    private val restClient: RestClient
) {

    fun sendMessage(receiverUuid: UUID, message: String) {
        val dialogMessage = DialogMessageDto(userService.getCurrentUuid(), receiverUuid, message)

        restClient.post()
            .uri("$baseUrl/send")
            .contentType(MediaType.APPLICATION_JSON)
            .body(dialogMessage)
            .retrieve()
    }

    fun getDialogWithUser(userUuid: UUID): Array<DialogMessageDto>? {
        val currentUserUuid = userService.getCurrentUuid()

        return restClient.get()
            .uri("$baseUrl/list?user1=$currentUserUuid&user2=$userUuid")
            .retrieve()
            .toEntity(Array<DialogMessageDto>::class.java)
            .body
    }
}

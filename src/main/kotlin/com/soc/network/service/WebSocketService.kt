package com.soc.network.service

import com.soc.network.model.dto.PostDto
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service

@Service
class WebSocketService(private val simpMessagingTemplate: SimpMessagingTemplate) {

    fun sendPostToUser(userName: String, postDto: PostDto) {
        simpMessagingTemplate.convertAndSendToUser(userName, "/queue/reply", postDto)
    }
}
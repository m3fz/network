package com.soc.network.dialog.dao

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.soc.network.dialog.model.entity.DialogMessageEntity
import jakarta.annotation.PostConstruct
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.script.RedisScript
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*

@Service
class DialogMessageInMemDao(
    private val redisTemplate: RedisTemplate<String, Any>,
    private val objectMapper: ObjectMapper
) {
    private lateinit var createMessageScript: RedisScript<String>
    private lateinit var getDialogScript: RedisScript<String>

    @PostConstruct
    private fun init() {
        val createMessageRes: Resource = ClassPathResource("db/script/create_message.lua")
        createMessageScript = RedisScript.of(createMessageRes, String::class.java)

        val getDialogRes: Resource = ClassPathResource("db/script/get_dialog.lua")
        getDialogScript = RedisScript.of(getDialogRes, String::class.java)
    }

    fun createMessage(dialogMessageEntity: DialogMessageEntity) {
        val distributionKey =
            generateDistributionMd5Key(dialogMessageEntity.senderUuid, dialogMessageEntity.receiverUuid)
        val payload = objectMapper.writeValueAsString(dialogMessageEntity)

        redisTemplate.execute(createMessageScript, listOf(), distributionKey, payload)
    }

    fun getMessagesBetween(user1: UUID, user2: UUID): List<DialogMessageEntity> {
        val distributionKey = generateDistributionMd5Key(user1, user2)

        val dialogString = redisTemplate.execute(getDialogScript, listOf(),  distributionKey)

        try {
            return objectMapper.readValue(dialogString)
        } catch (e: IOException) {
            throw RuntimeException("Cannot parse dialog between users: $user1 and $user2")
        }
    }

    /**
     * Generates arguments order-invariant md5 key for shard distribution
     */
    private fun generateDistributionMd5Key(uuid1: UUID, uuid2: UUID): String {
        val os = ByteArrayOutputStream()
        listOf(uuid1, uuid2).stream()
            .map { it.toString() }
            .sorted(Comparator.naturalOrder())
            .map { it.toByteArray() }
            .forEach { os.write(it) }

        val md = MessageDigest.getInstance("MD5")

        return BigInteger(1, md.digest(os.toByteArray())).toString(16).padStart(32, '0')
    }
}
package com.soc.network.dao

import com.soc.network.model.entity.DialogMessageEntity
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementSetter
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import java.math.BigInteger
import java.security.MessageDigest
import java.sql.Timestamp
import java.util.*

@Service
class DialogMessageDao(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
    @Qualifier("roJdbcTemplate") private val roJdbcTemplate: JdbcTemplate
) {
    private val createMessageSql = "insert into dialog_message(uuid, sender, receiver, text, create_dt, distribution_hash) " +
            "values(:uuid, :sender, :receiver, :text, :createDt, :distributionKey::uuid)"
    private val findMessagesBetweenUsersSql = "select uuid, sender, receiver, text, create_dt from dialog_message " +
            "where distribution_hash = ?::uuid order by create_dt asc"

    fun createMessage(dialogMessageEntity: DialogMessageEntity) {
        namedParameterJdbcTemplate.update(createMessageSql, mapOf(
            "uuid" to dialogMessageEntity.uuid,
            "sender" to dialogMessageEntity.senderUuid,
            "receiver" to dialogMessageEntity.receiverUuid,
            "text" to dialogMessageEntity.text,
            "createDt" to Timestamp.valueOf(dialogMessageEntity.createDt),
            "distributionKey" to generateDistributionMd5Key(dialogMessageEntity.senderUuid, dialogMessageEntity.receiverUuid)
        ))
    }

    fun getMessagesBetween(user1: UUID, user2: UUID): List<DialogMessageEntity> {
        return roJdbcTemplate.query(findMessagesBetweenUsersSql, PreparedStatementSetter { ps ->
            ps.setString(1, generateDistributionMd5Key(user1, user2))
        }, dialogMessageRowMapper)
    }

    private val dialogMessageRowMapper: RowMapper<DialogMessageEntity> = RowMapper { rs, rowNum ->
        return@RowMapper DialogMessageEntity(
            rs.getObject("uuid") as UUID,
            rs.getObject("sender") as UUID,
            rs.getObject("receiver") as UUID,
            rs.getString("text"),
            rs.getTimestamp("create_dt").toLocalDateTime()
        )
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
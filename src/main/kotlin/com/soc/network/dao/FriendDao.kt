package com.soc.network.dao

import com.soc.network.model.entity.UserEntity
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementSetter
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Service
import java.util.*

@Service
class FriendDao(
    private val jdbcTemplate: JdbcTemplate,
    @Qualifier("roJdbcTemplate") private val roJdbcTemplate: JdbcTemplate
) {
    private val addFriendSql = "insert into friend(user_uuid, friend_uuid) values(?,?)"
    private val deleteFriendSql = "delete from friend where user_uuid=? and friend_uuid=?"
    private val findFriendsSql = "select friend_uuid from friend where user_uuid=?"
    private val findFriendedUsersSql = "select user_uuid, username from friend " +
            "join users on friend.user_uuid = users.uuid where friend_uuid=?"

    fun addFriend(userUuid: UUID, friendUuid: UUID) {
        jdbcTemplate.update(addFriendSql) { ps ->
            ps.setObject(1, userUuid)
            ps.setObject(2, friendUuid)
        }
    }

    fun deleteFriend(userUuid: UUID, friendUuid: UUID) {
        jdbcTemplate.update(deleteFriendSql) { ps ->
            ps.setObject(1, userUuid)
            ps.setObject(2, friendUuid)
        }
    }

    fun findFriends(uuid: UUID): List<UUID> {
        return roJdbcTemplate.query(findFriendsSql, PreparedStatementSetter { ps -> ps.setObject(1, uuid) },
            RowMapper { rs, rowNum -> rs.getObject("friend_uuid") as UUID })
    }

    fun findFriendedUsers(uuid: UUID): List<UserEntity> {
        return roJdbcTemplate.query(findFriendedUsersSql, PreparedStatementSetter { ps -> ps.setObject(1, uuid) },
            RowMapper { rs, rowNum ->
                return@RowMapper UserEntity(
                    uuid = rs.getObject("user_uuid") as UUID,
                    username = rs.getString("username"),
                    passwordHash = ""
                )
            })
    }
}
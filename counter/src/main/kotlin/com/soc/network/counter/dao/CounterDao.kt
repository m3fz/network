package com.soc.network.counter.dao

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementSetter
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Service
import java.util.*

@Service
class CounterDao(
    private val jdbcTemplate: JdbcTemplate,
    @Qualifier("roJdbcTemplate") private val roJdbcTemplate: JdbcTemplate
) {
    private val getUnreadCountSql = "select unread_count from counter where user_uuid =?"
    private val createUnreadCountSql = "insert into counter(user_uuid, unread_count, last_tx) values(?,?,?)"
    private val updateUnreadCountSql = "update counter set unread_count=unread_count+?, last_tx=? where user_uuid=?"
    private val rollbackUnreadCount = "update counter set unread_count=" +
            "unread_count-(select coalesce((select count from counter_tx_log where tx_uuid=? and rolled_back=false), 0)) " +
            "where user_uuid=?"

    fun getUserUnreadCount(userUuid: UUID): Int? {
        return try {
            jdbcTemplate.query(getUnreadCountSql,
                PreparedStatementSetter { ps -> ps.setObject(1, userUuid) },
                countRowMapper).firstOrNull()
        } catch (e: DataAccessException) {
            null
        }
    }

    fun getUserUnreadCountRo(userUuid: UUID): Int? {
        return try {
            roJdbcTemplate.query(getUnreadCountSql,
                PreparedStatementSetter { ps -> ps.setObject(1, userUuid) },
                countRowMapper).firstOrNull()
        } catch (e: DataAccessException) {
            null
        }
    }

    fun createUserUnreadCount(userUuid: UUID, count: Int, transactionUuid: UUID) {
        jdbcTemplate.update(createUnreadCountSql) { ps ->
            ps.setObject(1, userUuid)
            ps.setInt(2, count)
            ps.setObject(3, transactionUuid)
        }
    }

    fun updateUserUnreadCount(userUuid: UUID, count: Int, transactionUuid: UUID) {
        jdbcTemplate.update(updateUnreadCountSql) { ps ->
            ps.setInt(1, count)
            ps.setObject(2, transactionUuid)
            ps.setObject(3, userUuid)
        }
    }

    fun rollbackUserUnreadCount(userUuid: UUID, transactionUuid: UUID) {
        jdbcTemplate.update(rollbackUnreadCount) { ps ->
            ps.setObject(1, transactionUuid)
            ps.setObject(2, userUuid)
        }
    }

    private val countRowMapper: RowMapper<Int> = RowMapper { rs, rowNum ->
        return@RowMapper rs.getInt("unread_count")
    }
}
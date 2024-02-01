package com.soc.network.dao

import com.soc.network.model.GenderType
import com.soc.network.model.UserEntity
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.sql.PreparedStatement
import java.sql.SQLException
import java.sql.Types
import java.util.*

@Service
class UserDao(
    private val jdbcTemplate: JdbcTemplate
) {
    val createUserSql = "insert into users(uuid, username, password_hash, enabled, firstname, lastname, age, gender, interests, city) " +
            "values(?,?,?,?,?,?,?,?,?,?)"

    fun hasUsers(): Boolean {
        return jdbcTemplate.queryForObject("select count(*) from users", Int::class.java)!! > 0
    }

    fun getUserByUuid(uuid: UUID): UserEntity? {
        val sql = "select * from users where uuid = '$uuid'"

        return try {
            jdbcTemplate.queryForObject(sql, userRowMapper)
        } catch (e: EmptyResultDataAccessException) {
            null
        }
    }

    fun getUserByUsername(username: String): UserEntity? {
        val sql = "select * from users where username = '$username'"

        return try {
            jdbcTemplate.queryForObject(sql, userRowMapper)
        } catch (e: EmptyResultDataAccessException) {
            null
        }
    }

    fun findUsersByName(firstname: String, lastname: String): List<UserEntity> {
        val sql = "select * from users where firstname like '$firstname%' and lastname like '$lastname%'"

        return try {
            jdbcTemplate.query(sql, userRowMapper)
        } catch (e: Exception) {
            emptyList()
        }
    }

    @Transactional
    fun createUsers(userEntities: List<UserEntity>) {
        jdbcTemplate.batchUpdate(createUserSql, object : BatchPreparedStatementSetter {
            @Throws(SQLException::class)
            override fun setValues(ps: PreparedStatement, i: Int) {
                ps.setObject(1, userEntities[i].uuid)
                ps.setString(2, userEntities[i].username)
                ps.setString(3, userEntities[i].password)
                ps.setBoolean(4, userEntities[i].isEnabled)
                ps.setString(5, userEntities[i].firstname)
                ps.setString(6, userEntities[i].lastname)
                if (userEntities[i].age == null) {
                    ps.setNull(7, Types.INTEGER)
                } else {
                    ps.setInt(7, userEntities[i].age!!)
                }
                ps.setString(8, userEntities[i].gender?.toString())
                ps.setString(9, userEntities[i].interests)
                ps.setString(10, userEntities[i].city)
            }

            override fun getBatchSize(): Int {
                return userEntities.size
            }
        })
    }

    fun createUser(userEntity: UserEntity) {
        jdbcTemplate.update(createUserSql) { ps ->
            ps.setObject(1, userEntity.uuid)
            ps.setString(2, userEntity.username)
            ps.setString(3, userEntity.password)
            ps.setBoolean(4, userEntity.isEnabled)
            ps.setString(5, userEntity.firstname)
            ps.setString(6, userEntity.lastname)
            if (userEntity.age == null) {
                ps.setNull(7, Types.INTEGER)
            } else {
                ps.setInt(7, userEntity.age!!)
            }
            ps.setString(8, userEntity.gender?.toString())
            ps.setString(9, userEntity.interests)
            ps.setString(10, userEntity.city)
        }
    }

    private var userRowMapper: RowMapper<UserEntity> = RowMapper { rs, rowNum ->
        val userEntity = UserEntity(
            rs.getObject("uuid") as UUID,
            rs.getString("username"),
            rs.getString("password_hash"),
            rs.getBoolean("enabled")
        )

        userEntity.firstname = rs.getString("firstname")
        userEntity.lastname = rs.getString("lastname")
        userEntity.age = rs.getObject("age") as Int?
        rs.getString("gender")?.let { userEntity.gender = GenderType.valueOf(it) }
        userEntity.interests = rs.getString("interests")
        userEntity.city = rs.getString("city")

        return@RowMapper userEntity
    }
}
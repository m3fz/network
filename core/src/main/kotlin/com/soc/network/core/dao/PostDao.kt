package com.soc.network.core.dao

import com.soc.network.core.model.entity.PostEntity
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementSetter
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.*

@Service
class PostDao(
    private val jdbcTemplate: JdbcTemplate,
    @Qualifier("roJdbcTemplate") private val roJdbcTemplate: JdbcTemplate
) {
    private val createPostSql = "insert into post(uuid, author_uuid, text, edit_dt) values(?,?,?,?)"
    private val updatePostSql = "update post set text=?, edit_dt=? where uuid=?"
    private val deletePostSql = "delete from post where uuid=?"
    private val getPostSql = "select uuid, author_uuid, text, edit_dt from post where uuid=?"
    private val getAuthorPostsSql = "select uuid, author_uuid, text, edit_dt from post where author_uuid=? limit ?"

    fun createPost(postEntity: PostEntity) {
        jdbcTemplate.update(createPostSql) { ps ->
            ps.setObject(1, postEntity.uuid)
            ps.setObject(2, postEntity.authorUuid)
            ps.setString(3, postEntity.text)
            ps.setTimestamp(4, Timestamp.valueOf(postEntity.editDt))
        }
    }

    fun updatePost(postEntity: PostEntity) {
        jdbcTemplate.update(updatePostSql) { ps ->
            ps.setString(1, postEntity.text)
            ps.setTimestamp(2, Timestamp.valueOf(postEntity.editDt))
            ps.setObject(3, postEntity.uuid)
        }
    }

    fun deletePost(uuid: UUID) {
        jdbcTemplate.update(deletePostSql) { ps ->
            ps.setObject(1, uuid)
        }
    }

    fun getPost(uuid: UUID): PostEntity? {
        return try {
            roJdbcTemplate.query(getPostSql, PreparedStatementSetter { ps -> ps.setObject(1, uuid) },
                postRowMapper).firstOrNull()
        } catch (e: DataAccessException) {
            null
        }
    }

    fun getAuthorPosts(authorUuid: UUID): List<PostEntity> {
        return roJdbcTemplate.query(getAuthorPostsSql, PreparedStatementSetter { ps ->
            ps.setObject(1, authorUuid)
            ps.setInt(2, 1000)
        }, postRowMapper)
    }

    private val postRowMapper: RowMapper<PostEntity> = RowMapper { rs, rowNum ->
        return@RowMapper PostEntity(
            rs.getObject("uuid") as UUID,
            rs.getObject("author_uuid") as UUID,
            rs.getString("text"),
            rs.getTimestamp("edit_dt").toLocalDateTime()
        )
    }
}
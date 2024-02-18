package com.soc.network.service

import com.soc.network.dao.FeedCacheDao
import com.soc.network.dao.FriendDao
import com.soc.network.dao.PostDao
import com.soc.network.model.dto.PostDto
import com.soc.network.model.entity.PostEntity
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.apache.coyote.BadRequestException
import org.springframework.stereotype.Service
import java.util.*

@Service
class PostService(
    val postDao: PostDao,
    private val feedCacheDao: FeedCacheDao,
    private val friendDao: FriendDao,
) {

    fun createPost(postDto: PostDto, userUuid: UUID): UUID {
        val postEntity = PostEntity(authorUuid = userUuid, text = postDto.text)
        postDao.createPost(postEntity)

        runBlocking {
            launch { assembleFriendsFeeds(userUuid) }
        }

        return postEntity.uuid
    }

    fun updatePost(postDto: PostDto, userUuid: UUID): UUID {
        if (postDto.uuid == null) {
            throw BadRequestException("Post uuid cannot be null")
        }

        val postEntity = PostEntity(uuid = postDto.uuid, authorUuid = userUuid, text = postDto.text)
        postDao.updatePost(postEntity)

        runBlocking {
            launch { assembleFriendsFeeds(userUuid) }
        }

        return postEntity.uuid
    }

    fun deletePost(uuid: UUID) {
        postDao.deletePost(uuid)

        runBlocking {
            launch { assembleFriendsFeeds(uuid) }
        }
    }

    fun getPost(uuid: UUID): PostDto {
        val postEntity = postDao.getPost(uuid) ?: throw NoSuchElementException("Post with uuid=$uuid does not exist")

        return toDto(postEntity)
    }

    fun getAuthorPosts(authorUuid: UUID): List<PostDto> {
        return postDao.getAuthorPosts(authorUuid).stream()
            .map { toDto(it) }
            .toList()
    }

    fun getFeed(uuid: UUID, limit: Int, offset: Int): List<PostDto> {
        val feed = feedCacheDao.getFeed(uuid.toString()) ?: assembleUserFeed(uuid)

        return feed.drop(offset).take(limit)
    }

    fun assembleUserFeed(uuid: UUID): List<PostDto> {
        return friendDao.findFriends(uuid).parallelStream()
            .flatMap { this.getAuthorPosts(it).stream() }
            .sorted(Comparator.comparing(PostDto::editDt).reversed())
            .limit(1000)
            .toList()
            .also { feedCacheDao.saveFeed(uuid.toString(), it) }
    }

    private suspend fun assembleFriendsFeeds(uuid: UUID) {
            friendDao.findFriendedUsers(uuid).parallelStream()
                .forEach { assembleUserFeed(it) }
    }

    private fun toDto(postEntity: PostEntity): PostDto {
        return PostDto(postEntity.uuid, postEntity.text, postEntity.editDt)
            .apply { authorUuid = postEntity.authorUuid }
    }
}
package com.soc.network.core.service

import com.soc.network.core.dao.FriendDao
import org.springframework.stereotype.Service
import java.util.*

@Service
class FriendService(
    private val friendDao: FriendDao,
    private val postService: PostService
) {

    fun addFriend(userUuid: UUID, friendUuid: UUID) {
        friendDao.addFriend(userUuid, friendUuid)
        postService.assembleUserFeed(userUuid)
    }

    fun deleteFriend(userUuid: UUID, friendUuid: UUID) {
        friendDao.deleteFriend(userUuid, friendUuid)
        postService.assembleUserFeed(userUuid)
    }

    fun getFriends(userUuid: UUID): List<UUID> {
        return friendDao.findFriends(userUuid)
    }
}

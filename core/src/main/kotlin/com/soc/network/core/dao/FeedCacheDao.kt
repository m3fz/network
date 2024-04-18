package com.soc.network.core.dao

import com.soc.network.core.model.dto.PostDto
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Service
class FeedCacheDao(private val redisTemplate: RedisTemplate<String, Any>) {

    fun saveFeed(userId: String, feed: List<PostDto>) {
        redisTemplate.opsForValue().set(userId, feed)
    }

    fun getFeed(userId: String): List<PostDto>? {
        return redisTemplate.opsForValue().get(userId) as? List<PostDto>
    }
}
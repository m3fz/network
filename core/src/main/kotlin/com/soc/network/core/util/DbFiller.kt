package com.soc.network.core.util

import com.soc.network.core.dao.UserDao
import com.soc.network.core.model.GenderType
import com.soc.network.core.model.entity.UserEntity
import jakarta.annotation.PostConstruct
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.dao.DuplicateKeyException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random

@Component
@ConditionalOnProperty(name = ["generate-data"], havingValue = "true")
class DbFiller(
    private val userDao: UserDao,
    private val passwordEncoder: PasswordEncoder
) {
    private val interests: List<String> = listOf("cats", "dogs", "auto", "motorcycle", "cooking", "gardening", "video-games", "movies", "football", "hockey", "basketball", "books")
    private val fileLines: List<String> = mutableListOf()
    private var insertsCount: AtomicInteger = AtomicInteger()

    @PostConstruct
    fun fill() {
        if (userDao.hasUsers()) return

        println("Reading file")
        this::class.java.getResourceAsStream("/db/people.csv").bufferedReader().useLines { lines ->
            lines.forEach { fileLines.addLast(it) }
        }
        println("Starting fill the DB")
        fileLines.asSequence().chunked(150).forEach { handleLinesChunk(it)}
        println("Finished filling the DB. Total is: ${insertsCount.get()}")
    }

    private fun handleLinesChunk(lines: List<String>) {
        try {
            val userEntities = lines.parallelStream()
                .map { createEntityFromLine(it) }
                .toList()
            println("Created users chunk")
            userDao.createUsers(userEntities)
            val total = insertsCount.addAndGet(150)
            println("Saved users chunk. Total is $total")
        } catch (e: DuplicateKeyException) {
            println("Got duplicated username in chunk, generating it again. " + e.message)
            handleLinesChunk(lines)
        }
    }

    private fun createEntityFromLine(line: String): UserEntity {
        val params = line.split(",")
        val nameParts = params[0].split(" ")
        val username = makeUsername(params[0], params[1].toInt())
        val entity = UserEntity(
            username = username,
//            passwordHash = Base64.getEncoder().encodeToString(username.toByteArray())
//            passwordHash = passwordEncoder.encode(username)
            passwordHash = passwordEncoder.encode("pwd")
        )
        entity.lastname = nameParts[0]
        entity.firstname = nameParts[1]
        entity.age = params[1].toInt()
        entity.gender = chooseGender()
        entity.interests = chooseInterests()
        entity.city = params[2]

        return entity
    }

    private fun makeUsername(name: String, age: Int): String {
        return Random.nextInt(1,9999).toString() + "_" +
                TransliterateUtil.transliterate(name) +
                (LocalDate.now().year - age)
    }

    private fun chooseGender(): GenderType {
        return GenderType.values().random()
    }

    private fun chooseInterests(): String {
        return interests.random()
    }
}
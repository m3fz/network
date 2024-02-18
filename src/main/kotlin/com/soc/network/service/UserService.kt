package com.soc.network.service

import com.soc.network.dao.UserDao
import com.soc.network.model.dto.UserDto
import com.soc.network.model.dto.UserUuidDto
import com.soc.network.model.entity.UserEntity
import com.soc.network.model.error.UserCreateException
import com.soc.network.model.error.UserNotExistsException
import org.apache.coyote.BadRequestException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
    val userDao: UserDao,
    val passwordEncoder: PasswordEncoder
) {

    fun getUserByUuid(uuid: UUID): UserDto {
        val userEntity = userDao.getUserByUuid(uuid)
            ?: throw UserNotExistsException("User with uuid=$uuid does not exist")

        return convertToDto(userEntity)
    }

    fun createUser(userDto: UserDto): UserUuidDto {
        if (userDao.getUserByUsername(userDto.username) != null) {
            throw UserCreateException("User ${userDto.username} already exists!")
        }

        val passwordHash = passwordEncoder.encode(userDto.password)

        val userEntity = UserEntity(username = userDto.username, passwordHash = passwordHash)
        userEntity.firstname = userDto.firstname
        userEntity.lastname = userDto.lastname
        userEntity.age = userDto.age
        userEntity.gender = userDto.gender
        userEntity.interests = userDto.interests
        userEntity.city = userDto.city

        userDao.createUser(userEntity)

        return UserUuidDto(userEntity.uuid)
    }

    fun findUsers(firstname: String, lastname: String): List<UserDto> {
        if (firstname.length < 2 || lastname.length < 2) {
            throw BadRequestException("firstname and lastname params should have more than 2 characters")
        }

        return userDao.findUsersByName(firstname, lastname).parallelStream()
            .map { convertToDto(it) }
            .sorted(compareBy { it.uuid })
            .toList()
    }

    private fun convertToDto(userEntity: UserEntity): UserDto {
        val userDto = UserDto(userEntity.uuid, userEntity.username, userEntity.password)
        userDto.firstname = userEntity.firstname
        userDto.lastname = userEntity.lastname
        userDto.age = userEntity.age
        userDto.gender = userEntity.gender
        userDto.interests = userEntity.interests
        userDto.city = userEntity.city

        return userDto
    }
}
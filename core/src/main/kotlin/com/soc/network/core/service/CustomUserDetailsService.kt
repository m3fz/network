package com.soc.network.core.service

import com.soc.network.core.dao.UserDao
import org.springframework.dao.IncorrectResultSizeDataAccessException
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(private val userDao: UserDao) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        try {
            return userDao.getUserByUsername(username!!)!!
        } catch (e: IncorrectResultSizeDataAccessException) {
            throw InternalAuthenticationServiceException("User with such username does not exist", e)
        }

    }

}
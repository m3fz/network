package com.soc.network.service

import com.soc.network.dao.UserDao
import org.springframework.dao.IncorrectResultSizeDataAccessException
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(val userDao: UserDao) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        try {
            return userDao.getUserByUsername(username!!)!!
        } catch (e: IncorrectResultSizeDataAccessException) {
            throw InternalAuthenticationServiceException("User with such username does not exist", e)
        }

    }

}
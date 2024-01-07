package com.soc.network.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

data class UserEntity(
    val uuid: UUID = UUID.randomUUID(),
    private val username: String,
    private val passwordHash: String,
    private val enabled: Boolean = true,
): UserDetails {
    var firstname: String? = null
    var lastname: String? = null
    var age: Int? = null
    var gender: GenderType? = null
    var interests: String? = null
    var city: String? = null

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        mutableSetOf(SimpleGrantedAuthority("ROLE_USER"))

    override fun getPassword(): String = passwordHash

    override fun getUsername(): String = username

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = enabled

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = enabled
}

package com.eleetcoders.api.services

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class UserServiceTest {
    @Test
    fun `email domain is correct`() {
        val userService = UserService()
        assertEquals(userService.getEmailDomain("test123@domain.com"), "domain.com")
        assertEquals(userService.getEmailDomain("test123@domain"), "domain")
        assertEquals(userService.getEmailDomain("test123@"), null)
        assertEquals(userService.getEmailDomain("test123"), null)
    }
}
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

    @Test
    fun `cpp emails are allowed`() {
        val userService = UserService()
        assertEquals(userService.isValidEmail("test123@cpp.edu"), true)
        assertEquals(userService.isValidEmail("cpp.edu"), false)
        assertEquals(userService.isValidEmail("test123@sdsu.edu"), false)
        assertEquals(userService.isValidEmail("test123"), false)
    }
}
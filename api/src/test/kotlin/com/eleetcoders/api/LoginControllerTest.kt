package com.eleetcoders.api

import com.eleetcoders.api.controllers.LoginController
import com.eleetcoders.api.controllers.UserController
import com.eleetcoders.api.models.Status
import com.eleetcoders.api.models.User
import com.eleetcoders.api.services.LoginServices
import com.eleetcoders.api.services.UserService
import com.google.gson.Gson
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.Mockito
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType


@WebMvcTest(controllers = [LoginController::class])
class LoginControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var loginService: LoginServices

    @Test
    fun signUpPage_ShouldReturnVerifyStatus() {
        // Arrange
        val loginCredentials = mapOf(
            "email" to "test@example.com",
            "password" to "password123",
            "firstname" to "mike",
            "lastname" to "idk",
            "username" to "urmoma"
        )
        val request = mock(HttpServletRequest::class.java)
        val response = mock(HttpServletResponse::class.java)
        val loginServices = mock(LoginServices::class.java)
        val user = User(loginCredentials)
        val expectedStatus = Status.VERIFY

        `when`(loginServices.encrypt("password123")).thenReturn("encryptedPassword")
        `when`(loginServices.createNewUser(user, request, response)).thenReturn(Gson().toJson(Status.SUCCESS))

        val signUpController = LoginController(loginServices)

        // Act
        val result = signUpController.signUpPage(loginCredentials, request, response)

        // Assert
        assertEquals(Gson().toJson(expectedStatus), result)
        verify(response).status = HttpStatus.OK.value()
    }
}
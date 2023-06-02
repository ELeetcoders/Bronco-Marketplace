package com.eleetcoders.api

import com.eleetcoders.api.controllers.BaseController
import com.google.gson.Gson
import com.google.gson.JsonParser
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [BaseController::class], excludeAutoConfiguration = [SecurityAutoConfiguration::class])
class BaseControllerUnitTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var mailSender: JavaMailSender

    @Test
    fun `should return JSON with all products for a given category`() {

        val result = mockMvc.perform(get("/"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()

        val responseString = result.response.contentAsString
        JsonParser.parseString(responseString)
//        val responseMap: MutableMap<String, Any> = HashMap()
//        responseMap["name"] = "John Doe"
//        responseMap["age"] = 30
//        responseMap["email"] = "johndoe@example.com"
//        println(Gson().toJson(responseMap))
//        println(responseMap)
//        println("this is a test")
//        println("Response body: $responseString")
    }
}


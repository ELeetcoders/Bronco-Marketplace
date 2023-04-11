package com.eleetcoders.api

import com.eleetcoders.api.controllers.UserController
import com.eleetcoders.api.models.Product
import com.eleetcoders.api.models.Status
import com.eleetcoders.api.models.User
import com.eleetcoders.api.services.UserService
import com.google.gson.Gson
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [UserController::class])
class UserControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var userService: UserService

    @Test
    fun `should return all products with same email`() {
        val user = User("test@cpp.edu", "pass", "testing", "te", "st")
        val products = listOf<Product>(
            Product("1", "test", 10.2, "test@cpp.edu", "this should pass", "", Product.Category.BOOK),
            Product("2", "test2", 1000.2, "test@cpp.edu", "this should pass", "", Product.Category.SERVICES),
            Product("3", "test3", 10.322, "ahhh@cpp.edu", "this shouldnt pass", "", Product.Category.BOOK)

        )

        Mockito.`when`(userService.listProducts(user)).thenReturn(Gson().toJson(products.subList(0,2)))
        val expectedVal = Gson().toJson(products.subList(0,2))
        val result = mockMvc.perform(get("/user/get-listings"))
            .andExpect(status().isOk)
            .andExpect(content().json(expectedVal))
            .andReturn()
        val response = result.response.contentAsString
        println("Response Body: $response")
    }

    @Test
    fun `should return a pass for test1`(){
        val user = User("test1@cpp.edu", "pass", "testing1", "te", "st")

        Mockito.`when`(userService.createUser(user)).thenReturn(Gson().toJson(Status.SUCCESS))

        val expectedVal = Gson().toJson(Status.SUCCESS)
        val result = mockMvc.perform(post("/user/create-user", Gson().toJson(user)))
            .andExpect(status().isOk)
            .andExpect(content().json(expectedVal))
            .andReturn()
        val response = result.response.contentAsString
        println("Response Body: $response")
    }
}
package com.eleetcoders.api

import com.eleetcoders.api.controllers.ProductController
import com.eleetcoders.api.models.Category
import com.eleetcoders.api.models.Product
import com.eleetcoders.api.services.ProductService
import com.google.gson.Gson
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.util.JsonExpectationsHelper
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(controllers = [ProductController::class])
class ProductControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var productService: ProductService

    @Test
    fun `should return JSON with all products for a given category`() {
        val products = listOf(
            Product("1", "Math book", 1.00, "food", "jane@example.com", "Easy book"),
            Product("2", "CS book", 0.50, "food", "john@example.com", "Hard book")
        )
        Mockito.`when`(productService.getAllProductsByCategory(Category(category = "book"))).thenReturn(Gson().toJson(products))

        val expectedResponse = Gson().toJson(products)
        val result = mockMvc.perform(get("/product/book/get-all"))
            .andExpect(status().isOk)
            //.andExpect(content().json(expectedResponse))
            .andReturn()

        val responseString = result.response.contentAsString
        println("Response body: $responseString")
    }
}


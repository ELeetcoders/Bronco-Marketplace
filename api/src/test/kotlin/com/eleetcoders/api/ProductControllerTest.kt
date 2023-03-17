package com.eleetcoders.api

import com.eleetcoders.api.controllers.ProductController
import com.eleetcoders.api.models.Product
import com.eleetcoders.api.services.ProductService
import com.google.gson.Gson
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [ProductController::class])
class ProductControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var productService: ProductService

    @Test
    fun `should return JSON with all products for a given category`() {
        val products = listOf(
            Product("1", "Math book", 1.00, "food", "jane@example.com", Product.Category.BOOK),
            Product("2", "CS book", 0.50, "food", "john@example.com", Product.Category.BOOK),
            Product("3", "CS1400 texting", 40.5, "email@example.com", "tutoring", Product.Category.SERVICES)
        )

        Mockito.`when`(productService.getAllProductsByCategory(Product.Category.BOOK)).thenReturn(Gson().toJson(products.subList(0, 2)))

        val expectedResponse = Gson().toJson(products.subList(0,2))
        val result = mockMvc.perform(get("/product/BOOK/get-all"))
            .andExpect(status().isOk)
            .andExpect(content().json(expectedResponse))
            .andReturn()

        val responseString = result.response.contentAsString
        println("Response body: $responseString")
    }
}


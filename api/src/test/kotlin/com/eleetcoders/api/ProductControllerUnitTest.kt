package com.eleetcoders.api

import com.eleetcoders.api.controllers.BaseController
import com.eleetcoders.api.controllers.ProductController
import com.eleetcoders.api.models.Category
import com.eleetcoders.api.models.Product
import com.eleetcoders.api.services.ProductService
import com.google.gson.Gson
import com.google.gson.JsonParser
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

@WebMvcTest(controllers = [ProductController::class], excludeAutoConfiguration = [SecurityAutoConfiguration::class])
class ProductControllerUnitTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var productService: ProductService
    //val productService: ProductService = ProductService()

    @Test
    fun `should return all products`() {
        // Mocking the service method
        val products = HashMap<String, MutableList<Product>>()
        products.put("BOOK", listOf<Product>(
            Product(
                id = "5nBVHD2xV2ucClqeqAKb",
                name = "Book",
                price = 10.0,
                email = "mmt@cpp.edu",
                firstname = "Michael",
                lastname = "Truong",
                username = "michael8pho",
                desc = "Brand new",
                category = Product.Category.BOOK
            )
        ).toMutableList())

        products.put("SERVICE", listOf(
            Product(
                id = "5nBVHD2xV2ucClqeqAKd",
                name = "Cleaning Service",
                price = 50.0,
                email = "janedoe@example.com",
                firstname = "Jane",
                lastname = "Doe",
                username = "janedoe",
                desc = "Professional cleaning service",
                category = Product.Category.SERVICES
            )
        ).toMutableList())

        products.put("TECH", listOf(
            Product(
                id = "5nBVHD2xV2ucClqeqAKc",
                name = "Keyboard",
                price = 20.0,
                email = "johndoe@example.com",
                firstname = "John",
                lastname = "Doe",
                username = "johndoe",
                desc = "Wireless keyboard",
                category = Product.Category.TECH
            )
        ).toMutableList())

        `when`(productService.getAllProducts()).thenReturn(Gson().toJson(products))

        // Performing the request
        val result = mockMvc.perform(get("/product/get-all"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string(Gson().toJson(products)))
            .andReturn()

        // Verifying the service method was called
        verify(productService).getAllProducts()

        //make sure response was json
        val responseString = result.response.contentAsString
        val jsonResponse = JsonParser.parseString(responseString).asJsonObject

        assertTrue(jsonResponse.has("BOOK"))
        assertTrue(jsonResponse.has("TECH"))
        assertTrue(jsonResponse.has("SERVICE"))

        // Check if each category has a "price" field
        val bookCategory = jsonResponse.get("BOOK").asJsonArray
        for (product in bookCategory) {
            assertTrue(product.asJsonObject.has("price"))
            assertTrue(product.asJsonObject.has("id"))
        }

        val techCategory = jsonResponse.get("TECH").asJsonArray
        for (product in techCategory) {
            assertTrue(product.asJsonObject.has("price"))
        }

        val serviceCategory = jsonResponse.get("SERVICE").asJsonArray
        for (product in serviceCategory) {
            assertTrue(product.asJsonObject.has("price"))
        }


    }
}
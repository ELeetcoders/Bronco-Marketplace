package com.eleetcoders.api

import com.google.gson.JsonParser
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
//@WebMvcTest(controllers = [ProductController::class], excludeAutoConfiguration = [SecurityAutoConfiguration::class])
@AutoConfigureMockMvc
class ProductControllerIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return JSON with all products for a given category`() {
        val result = mockMvc.perform(get("/product/BOOK/get-all"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()

        //val jsonResponse = result.response
        //val first100Characters = jsonResponse.substring(0, 100)
        //val jsonElement: JsonElement = Gson().fromJson(jsonResponse, JsonElement::class.java)
        //println("Response body: $jsonResponse")
        //println("First 100 characters: $jsonResponse")
        val responseString = result.response.contentAsString
        val jsonResponse = JsonParser.parseString(responseString).asJsonObject

        Assertions.assertTrue(jsonResponse.has("BOOK"))
        Assertions.assertFalse(jsonResponse.has("TECH"))
        Assertions.assertFalse(jsonResponse.has("SERVICE"))

        // Check if each category has a "price" field
        val bookCategory = jsonResponse.get("BOOK").asJsonArray
        for (product in bookCategory) {
            Assertions.assertTrue(product.asJsonObject.has("price"))
            Assertions.assertTrue(product.asJsonObject.has("id"))
        }
    }

    @Test
    fun `test json`() {
        val result = mockMvc.perform(get("/"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()

        val responseString = result.response.contentAsString
        println(JsonParser.parseString(responseString))
        println("Response body: $responseString")
    }
}


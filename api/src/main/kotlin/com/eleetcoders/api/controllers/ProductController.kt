package com.eleetcoders.api.controllers

import com.eleetcoders.api.models.Product
import com.eleetcoders.api.services.ProductService
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.api.client.json.Json
import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/product")
class ProductController @Autowired constructor(
    private val productService: ProductService
){

    @GetMapping("/get-all")
    fun getProduct(): String {
        return productService.getAllProducts()
    }

    @PostMapping("/post")
    fun postProduct(@RequestBody product: Product): Boolean {
        return productService.postProduct(product)
    }

    @GetMapping("/del-product")
    fun deleteProduct(@RequestBody product: Product) : Boolean {
        return productService.deleteProduct(product)
    }

    @PostMapping("/update-product")
    fun updateProduct(@RequestBody myJson: Map<String, Any>) : Boolean {
        val product = myJson["product"]?.let {
            ObjectMapper().convertValue(it as Map<*, *>, Product::class.java)
        } ?: throw IllegalArgumentException("Missing required 'product' field in request body")
        val name = myJson.getOrDefault("name", "") as String
        val desc = myJson.getOrDefault("desc", "") as String
        val price = myJson.getOrDefault("price", 0) as Int
        return productService.updateProduct(product, name, desc, price)
    }

    @GetMapping("/filter-price")
    fun filterByPrice(@RequestBody max: String) : String? {
        val price = max.toDouble()
        return productService.filterByPrice(price)
    }

    data class SearchRequest(val term: String)

    @GetMapping("/search")
    fun searchProduct(@RequestParam request: SearchRequest) : String? {
        return productService.searchByTerm(request.term)
    }
    @GetMapping("/sort-name")
    fun sortName(@RequestParam reversed:Boolean = false) : String {
        return productService.sortByName(reversed)
    }

    @GetMapping("/sort-price")
    fun sortPrice(@RequestParam reversed: Boolean = false) : String {
        return productService.sortByPrice(reversed)
    }

    @GetMapping("/get-product")
    fun getProduct(@RequestBody product: Product): String {
        return productService.getProduct(product)
    }
}
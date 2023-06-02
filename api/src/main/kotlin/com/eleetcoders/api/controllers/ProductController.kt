package com.eleetcoders.api.controllers

import com.eleetcoders.api.models.Product
import com.eleetcoders.api.services.ProductService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.MediaType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/product", produces= arrayOf("application/json"))
class ProductController @Autowired constructor(
    private val productService: ProductService
){

    @GetMapping("/get-all")
    fun getProduct(): String {
        return productService.getAllProducts()
    }

    @GetMapping("/{category}/get-all")
    fun getAllProductsByCategory(@PathVariable category: Product.Category): String {
        return productService.getAllProductsByCategory(category)
    }

    @GetMapping("/filter-price")
    fun filterByPrice(@RequestParam max: String) : String? {
        return productService.filterByPrice(max.toDouble())
    }

    @GetMapping("/search-email")
    fun searchByEmail(@RequestParam email: SearchRequest) : String {
        return productService.searchByEmail(email.term)
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
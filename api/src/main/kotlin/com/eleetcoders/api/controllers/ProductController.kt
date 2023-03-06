package com.eleetcoders.api.controllers

import com.eleetcoders.api.services.ProductService
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
    fun postProduct(): String {
        return productService.postProduct()
    }

    @GetMapping("/find-product")
    fun findProduct(@RequestBody id: String): String {
        return productService.findProduct(id)
    }

    @GetMapping("/del-product")
    fun deleteProduct(@RequestBody id: String) : Boolean {
        return productService.deleteProduct(id)
    }

    @PostMapping("/update-product")
    fun updateProduct(@RequestBody id: String) : Boolean {
        return productService.updateProduct(id)
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
    fun sortName(@RequestParam reversed: Boolean) : String {
        return productService.sortByName(reversed)
    }

    @GetMapping("/sort-price")
    fun sortPrice(@RequestParam reversed: Boolean) : String {
        return productService.sortByPrice(reversed)
    }
}
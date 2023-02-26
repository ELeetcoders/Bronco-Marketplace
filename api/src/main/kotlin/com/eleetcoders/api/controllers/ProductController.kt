package com.eleetcoders.api.controllers

import com.eleetcoders.api.services.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
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
}
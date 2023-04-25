package com.eleetcoders.api.controllers

import com.eleetcoders.api.models.Product
import com.eleetcoders.api.models.Status
import com.eleetcoders.api.models.User
import com.eleetcoders.api.services.UserService
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController constructor(
    private val userService: UserService = UserService()
){

    private inline fun <reified T : Any> getObj(data : Map<String, Any>, dataType : String) : T{
        return data[dataType]?.let {
            ObjectMapper().convertValue(it as Map<*,*>, T::class.java)
        } ?: throw IllegalArgumentException("Missing required $dataType field in request body")
    }

    @GetMapping("/get-listings")
    fun getListings(@RequestBody user: User) : String {
        return userService.listProducts(user)
    }

    @PostMapping("/create-listing")
    fun createListing(@RequestBody data : Map<String, Any>, request : HttpServletRequest) : Boolean {
        val user = getObj<User>(data, "user")
        val name = data.getOrDefault("name", "") as String
        val category = Product.ignoreCase(data.getOrDefault("category", "") as String)
        val price = data.getOrDefault("price", "-1.0") as String
        val desc = data.getOrDefault("desc", "") as String
        val imageUrl = data.getOrDefault("imageUrl", "") as String

        val session = request.getSession(false)
        val email: String = session.getAttribute("email") as String

        return userService.createListing(email, name, desc, price.toDouble(), category, imageUrl)
    }

    @PostMapping("/remove-listing")
    fun removeListing(@RequestBody product: Product) : String {
        return userService.removeListing(product)
    }
}
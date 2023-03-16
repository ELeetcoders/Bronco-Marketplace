package com.eleetcoders.api.controllers

import com.eleetcoders.api.models.Product
import com.eleetcoders.api.models.User
import com.eleetcoders.api.services.UserService
import com.fasterxml.jackson.databind.ObjectMapper
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

    @PostMapping("/create-user")
    fun createUser(@RequestBody user: User) : Boolean {
        return userService.createUser(user)
    }

    @GetMapping("/login-user")
    fun loginUser(@RequestBody userAndPassword : Map<String, Any>) : Boolean {
        val user = getObj<User>(userAndPassword, "user")
        val password = userAndPassword.getOrDefault("password", "") as String
        return userService.loginUser(user, password)
    }

    @GetMapping("/get-listings")
    fun getListings(@RequestBody user: User) : String {
        return userService.listProducts(user)
    }

    @PostMapping("/create-listing")
    fun createListing(@RequestBody data : Map<String, Any>) : Boolean {
        val user = getObj<User>(data, "user")
        val name = data.getOrDefault("name", "") as String
        val category = data.getOrDefault("category", "") as String
        val price = data.getOrDefault("price", -1.0) as Double
        val desc = data.getOrDefault("desc", "") as String

        return userService.createListing(user, name, desc, price, category)
    }

    @PostMapping("/remove-listing")
    fun removeListing(@RequestBody product: Product) : Boolean {
        return userService.removeListing(product)
    }
}
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

    @PostMapping("/create-user")
    fun createUser(@RequestBody user: User) : Boolean {
        return userService.createUser(user)
    }

    @GetMapping("/login-user")
    fun loginUser(@RequestBody userAndPassword : Map<String, Any>) : Boolean {
        val user = userAndPassword["user"]?.let {
            ObjectMapper().convertValue(it as Map<*, *>, User::class.java)
        } ?: throw IllegalArgumentException("Missing required 'user' field in request body")
        val password = userAndPassword.getOrDefault("password", "") as String
        return userService.loginUser(user, password)
    }

    @GetMapping("/get-listings")
    fun getListings(@RequestBody user: User) : String {
        return userService.listProducts(user)
    }
}
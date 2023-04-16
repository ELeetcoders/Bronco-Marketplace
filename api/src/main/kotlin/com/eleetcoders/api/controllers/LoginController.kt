package com.eleetcoders.api.controllers

import com.eleetcoders.api.models.User
import com.eleetcoders.api.services.LoginServices
import com.google.gson.Gson
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/login")
class LoginController constructor(
    // TODO: Create Login Page
    // TODO: Check if name exists within system
    val loginServices: LoginServices
){

    @GetMapping("/sign-in")
    fun loginPage(@RequestBody loginCredentials: Map<String, String>) : String {
        val email = Gson().fromJson(loginCredentials["email"], String::class.java)
        val password = loginServices.encrypt(
            Gson().fromJson(loginCredentials["password"], String::class.java)
        )

        return loginServices.checkCreds(email, password)
    }

    @GetMapping("sign-up")
    fun signUpPage(@RequestBody loginCredentials : Map<String, String>) : String {
        val map = HashMap<String, String>(loginCredentials)

        map["password"] = loginServices.encrypt(map["password"] as String)
        val user = User(map)

        return loginServices.createNewUser(user)
    }
}
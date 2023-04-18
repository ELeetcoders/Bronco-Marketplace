package com.eleetcoders.api.controllers

import com.eleetcoders.api.models.Status
import com.eleetcoders.api.models.User
import com.eleetcoders.api.services.LoginServices
import com.google.gson.Gson
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
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
    fun loginPage(@RequestBody loginCredentials: Map<String, String>,
                  request : HttpServletRequest, response: HttpServletResponse) : String {
        val email = Gson().fromJson(loginCredentials["email"], String::class.java)
        val password = loginServices.encrypt(
            Gson().fromJson(loginCredentials["password"], String::class.java)
        )

        if(loginServices.checkCredentials(email, password) == Gson().toJson(Status.FAIL))
            return Gson().toJson(Status.FAIL)

        val session = request.getSession(true)
        session.setAttribute("email", email)

        val cookie = Cookie("SESSION", session.id)
        response.addCookie(cookie)

        response.status = HttpStatus.OK.value()
        return Gson().toJson(Status.SUCCESS)
    }

    @GetMapping("sign-up")
    fun signUpPage(@RequestBody loginCredentials : Map<String, String>) : String {
        val map = HashMap<String, String>(loginCredentials)

        map["password"] = loginServices.encrypt(map["password"] as String)
        val user = User(map)

        return loginServices.createNewUser(user)
    }
}
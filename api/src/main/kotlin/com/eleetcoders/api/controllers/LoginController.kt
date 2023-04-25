package com.eleetcoders.api.controllers

import assertAuthenticated
import com.eleetcoders.api.models.Status
import com.eleetcoders.api.models.User
import com.eleetcoders.api.services.LoginServices
import com.google.gson.Gson
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/login")
class LoginController constructor(val loginServices: LoginServices){

    @GetMapping("/verify")
    fun verify(request : HttpServletRequest, response: HttpServletResponse) : String {
        if (!assertAuthenticated(request)) return "";
        val session = request.getSession(false)
        val email: String? = session?.getAttribute("email") as? String
        return Gson().toJson(email)
    }

    @PostMapping("/sign-in")
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

        response.status = HttpStatus.OK.value()
        response.setHeader("Set-Cookie", "JSESSIONID=" + session.getId() + "; Path=/; Domain=.broncomarketplace.com; SameSite=Lax;")
        return Gson().toJson(Status.SUCCESS)
    }

    @PostMapping("sign-up")
    fun signUpPage(@RequestBody loginCredentials : Map<String, String>,
                   request: HttpServletRequest, response: HttpServletResponse) : String {
        val map = HashMap<String, String>(loginCredentials)

        map["password"] = loginServices.encrypt(map["password"] as String)
        val user = User(map)

        if(loginServices.createNewUser(user) == Gson().toJson(Status.FAIL))
            return Gson().toJson(Status.FAIL)

        val session = request.getSession(true)
        session.maxInactiveInterval = 86400; //1 day session
        session.setAttribute("email", user.email)

        // Create a new JSESSIONID cookie with a custom maxAge value so it persists when user closes browser
        val cookie = Cookie("JSESSIONID", session.id)
        cookie.maxAge = 86400 // Set cookie maxAge to 1 day
        response.addCookie(cookie)

        response.status = HttpStatus.OK.value()
        response.setHeader("Set-Cookie", "JSESSIONID=" + session.getId() + "; SameSite=Lax;");
        return Gson().toJson(Status.SUCCESS)
    }

    @PostMapping("sign-out")
    fun signout(request: HttpServletRequest, response: HttpServletResponse): String {
        if (!assertAuthenticated(request)) return "";
        
        val session: HttpSession? = request.getSession(false)
        if (session != null) {
            response.setHeader("Set-Cookie", "JSESSIONID=" + session.getId() + "; Path=/; Domain=.broncomarketplace.com; SameSite=Lax; Max-Age=0")
            session.invalidate() // Invalidate the session
        }
        
        return Gson().toJson(Status.SUCCESS)
    }



}
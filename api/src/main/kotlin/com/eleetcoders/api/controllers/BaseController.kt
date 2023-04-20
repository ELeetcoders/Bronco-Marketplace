package com.eleetcoders.api.controllers

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.apache.tomcat.util.codec.binary.Base64
import org.jsoup.Jsoup
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

@RestController
@RequestMapping("/")
class BaseController {

    @GetMapping
    fun base(): String = "This is our API for the bronco marketplace!"

    @GetMapping("/michael")
    fun michael(): String = "API endpoint made by Michael!"

    @GetMapping("/testlogin")
    fun authtest(request : HttpServletRequest, response: HttpServletResponse): String {
        //println("testtttt")
        val session = request.getSession(true)
        session.maxInactiveInterval = 86400; //1 day session
        session.setAttribute("email", "watermelons@cpp.edu")

        // Create a new JSESSIONID cookie with a custom maxAge value so it persists when user closes browser
        val cookie = Cookie("JSESSIONID", session.id)
        cookie.maxAge = 86400 // Set cookie maxAge to 1 day
        response.addCookie(cookie)

        return "API endpoint should be accessible!"
    }

    @GetMapping("/testlogout")
    fun testlogout(request : HttpServletRequest, response: HttpServletResponse): String {
        val session: HttpSession? = request.getSession(false)
        if (session != null) {
            session.invalidate() // Invalidate the session
        }
        return "Logged out!"
    }

    @GetMapping("/cookietest")
    fun cookietest(request : HttpServletRequest, response: HttpServletResponse): String {
        return "API endpoint should be accessible!"
    }

    @GetMapping("/testdeploy")
    fun testdeploy(): String = "This API endpoint should be seen"

    @GetMapping("/async")
    fun async(): String {
        var result = ""
        /* This can be useful when you do something async, example: call async func */
        runBlocking {
            result = doSomethingAsync()
        }
        return result
    }

    suspend fun doSomethingAsync(): String {
        /* This would be the method that contains an async process, example: calling an api waiting for response */
        delay(2000)
        return "Async operation complete after 2 seconds!"
    }

    @GetMapping("/tyler")
    fun tyler(): String {
        val local = LocalDateTime.now();
        return "The current timestamp is: $local"
    }

    @GetMapping("/encryption")
    fun encryption(@RequestParam(value = "input", defaultValue = "hello world") input: String): String {
        val secretKey1 = "ssdkF\$HUy2A#D%kd"
        val secretKey2 = "weJiSEvR5yAC5ftB"

        val ivParameterSpec = IvParameterSpec(secretKey1.toByteArray())
        val secretKeySpec = SecretKeySpec(secretKey2.toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")

        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec)
        val encrypted: ByteArray = cipher.doFinal(input.toByteArray())
        return Base64.encodeBase64String(encrypted)
    }

    @GetMapping("/uziel")
    fun uziel(): String {
        return "This sentence is kinda interesting"
    }

    @GetMapping("/parsing")
    fun parsing(): String? {
        val doc = Jsoup.connect("http://cs480-projects.github.io/teams-spring2023/index.html").get()
        return doc.appendText("This was all parsed with JSoup").html()
    }











}
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
    fun base(): String = "Pong!"

    @PostMapping("/test")
    fun test(req: HttpServletRequest): String {
        val cookies: Array<Cookie> = req.cookies ?: emptyArray()
        cookies.forEach { cookie ->
            println("${cookie.name}=${cookie.value}")
        }
        return "Hello World"
    }

    @GetMapping("/test2")
    fun test2(req: HttpServletRequest): String {
        val cookies: Array<Cookie> = req.cookies ?: emptyArray()
        cookies.forEach { cookie ->
            println("${cookie.name}=${cookie.value}")
        }
        return "test2"
    }
}
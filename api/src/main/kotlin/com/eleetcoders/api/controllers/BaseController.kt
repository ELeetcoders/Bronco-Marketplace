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
}
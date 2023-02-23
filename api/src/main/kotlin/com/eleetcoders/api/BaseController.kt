package com.eleetcoders.api

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.apache.tomcat.util.codec.binary.Base64
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import org.jsoup.Jsoup

@RestController
@RequestMapping("/")
class BaseController {

    @GetMapping
    fun base(): String = "This is our API for the bronco marketplace!"

    @GetMapping("/michael")
    fun michael(): String = "API endpoint made by Michael!"

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
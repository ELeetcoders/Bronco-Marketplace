package com.eleetcoders.api.controllers

import com.google.gson.Gson
import io.github.cdimascio.dotenv.dotenv
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/", produces= arrayOf("application/json"))
class BaseController {

    @GetMapping
    fun base(): String ="Pong!"

//    @GetMapping("/testing")
//    fun testing(): Any {
//        val responseMap: MutableMap<String, Any> = HashMap()
//        responseMap["name"] = "John Doe"
//        responseMap["age"] = 30
//        responseMap["email"] = "johndoe@example.com"
//        return responseMap
//    }
//
//    @GetMapping("/testing2")
//    fun testing2(): Any {
//        val responseMap: MutableMap<String, Any> = HashMap()
//        responseMap["name"] = "John Doe"
//        responseMap["age"] = 30
//        responseMap["email"] = "johndoe@example.com"
//        return Gson().toJson(responseMap)
//    }
//
//    @GetMapping("/testing3")
//    fun testing3(): Any {
//        val responseMap: MutableMap<String, Any> = HashMap()
//        responseMap["name"] = "John Doe"
//        responseMap["age"] = 30
//        responseMap["email"] = "johndoe@example.com"
//        return Gson().toJson(Gson().toJson(responseMap))
//    }
//
//    @GetMapping("/test")
//    fun test(): String {
//        val dotenv = dotenv()
//        return dotenv["GMAIL_PASSWORD"]
//    }

    @Autowired
    private lateinit var mailSender: JavaMailSender
    @GetMapping("/email")
    fun email() {
        val message = SimpleMailMessage()
        val toEmail = "mmt@cpp.edu"
        val body = "Hi this is the verification email"
        val subject = "Verify email"
        message.setFrom("fromemail@gmail.com")
        message.setTo(toEmail)
        message.setText(body)
        message.setSubject(subject)
        mailSender.send(message)
        println("Mail Send...")
    }
}
package com.eleetcoders.api.controllers

import io.github.cdimascio.dotenv.dotenv
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/")
class BaseController {

//    @Value("\${spring.mail.password}")
//    private var GMAIL_PASSWORD = "wadrjddcbzsxmwed"

    @GetMapping
    fun base(): String = "Pong!"

    @GetMapping("/test")
    fun test(): String {
        val dotenv = dotenv()
        return dotenv["GMAIL_PASSWORD"]
    }

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
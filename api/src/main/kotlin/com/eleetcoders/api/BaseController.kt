package com.eleetcoders.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

@RestController
@RequestMapping("/")
class BaseController {

    @GetMapping
    fun base(): String = "This is our API for the bronco marketplace!"

    @GetMapping("/michael")
    fun michael(): String = "API endpoint made by Michael!"

    @GetMapping("/tyler")
    fun tyler(): String {
        val local = LocalDateTime.now();
        return "The current timestamp is: $local"
    }
}
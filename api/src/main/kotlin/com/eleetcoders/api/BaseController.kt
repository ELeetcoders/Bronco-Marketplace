package com.eleetcoders.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class BaseController {

    @GetMapping
    fun base(): String = "This is our API for the bronco marketplace!"

    @GetMapping("/michael")
    fun michael(): String = "API endpoint made by Michael!"

}
package com.eleetcoders.api

import kotlinx.coroutines.delay
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlinx.coroutines.runBlocking

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

}
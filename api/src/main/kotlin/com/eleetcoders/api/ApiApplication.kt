package com.eleetcoders.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ApiApplication {

	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			runApplication<ApiApplication>(*args)
		}
	}
}
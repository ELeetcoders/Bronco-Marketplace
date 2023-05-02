package com.eleetcoders.api

import io.github.cdimascio.dotenv.dotenv
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.env.Environment

@SpringBootApplication
class ApiApplication {

	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			val dotenv = dotenv()
			val gmailPassword = dotenv["GMAIL_PASSWORD"]
			System.setProperty("spring.mail.password", gmailPassword)
			runApplication<ApiApplication>(*args)
		}
	}
}
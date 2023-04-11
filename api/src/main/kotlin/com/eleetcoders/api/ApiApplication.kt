package com.eleetcoders.api

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
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
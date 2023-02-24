package com.eleetcoders.api

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class ApiApplication

fun main(args: Array<String>) {
	val options = FirebaseOptions.builder()
		.setCredentials(GoogleCredentials.getApplicationDefault())
		.setProjectId("bronco-marketplace")
		.build()

	if (FirebaseApp.getApps().isEmpty()) {
		FirebaseApp.initializeApp(options);
	}

	runApplication<ApiApplication>(*args)
}

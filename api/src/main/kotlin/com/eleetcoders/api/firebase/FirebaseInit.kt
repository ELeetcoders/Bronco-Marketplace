package com.eleetcoders.api.firebase

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import javax.annotation.PostConstruct


@Service
class FirebaseInit {

    companion object {
        private var initialized = false
        private val lock = Any()
    }

    @PostConstruct
    fun initialization() {
        if (!initialized) {
            synchronized(lock) {
                if (!initialized) {

                    // for development and testing
                    var serviceAccount: InputStream? = FirebaseInit::class.java.getResourceAsStream("/serviceAccountKey.json")
                    // for docker prod
                    if (serviceAccount == null) {
                        val file = File("serviceAccountKey.json")
                        serviceAccount = FileInputStream(file)
                    }

                    val options: FirebaseOptions = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build()

                    FirebaseApp.initializeApp(options)
                    initialized = true
                }
            }
        }
    }
}
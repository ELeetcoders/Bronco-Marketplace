package com.eleetcoders.api.firebase

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseOptions
import com.google.cloud.firestore.Firestore
import com.google.firebase.FirebaseApp
import com.google.firebase.cloud.FirestoreClient
import org.springframework.stereotype.Service
import java.io.FileInputStream
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
                    val serviceAccount = FirebaseInit::class.java.getResourceAsStream("/serviceAccountKey.json")

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
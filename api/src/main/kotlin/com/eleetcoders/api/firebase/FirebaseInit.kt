package com.eleetcoders.api.firebase

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseOptions
import com.google.cloud.firestore.Firestore
import com.google.firebase.FirebaseApp
import com.google.firebase.cloud.FirestoreClient
import org.springframework.stereotype.Service
import java.io.FileInputStream


@Service
class FirebaseInit {
    fun initialization() {

        val serviceAccount = FileInputStream("path/to/serviceAccountKey.json")

        val options: FirebaseOptions = FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build()

        FirebaseApp.initializeApp(options)

    }
}
package com.eleetcoders.api.services

import com.eleetcoders.api.models.Status
import com.eleetcoders.api.models.User
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.QueryDocumentSnapshot
import com.google.common.io.Resources.getResource
import com.google.firebase.cloud.FirestoreClient
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import org.jetbrains.annotations.NotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileReader
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

@Service
class LoginServices @Autowired constructor(
){
    val algorithm: String = "AES"
    val transformation : String = "AES/ECB/PKCS5Padding"

    fun checkCreds(email : String, password : String) : String {
        val firebase = FirestoreClient.getFirestore()

        val users = firebase.collection("user")

        var target : QueryDocumentSnapshot? = null

        for (user in users.get().get().documents) {
            if (user.id == email) {
                target = user
                break
            }
        }

        if (target == null)
            return Gson().toJson(Status.SUCCESS)

        if (decrypt(target.get("password") as String) == decrypt(password))
            return Gson().toJson(Status.SUCCESS)
        return Gson().toJson(Status.FAIL)
    }

    fun createNewUser(user : User): String {
        val db = FirestoreClient.getFirestore()
        val userRef = db.collection("user").document(user.email)

        if (userRef.get().get().exists() ||
            user.email.length < 9 ||
            user.email.substring(user.email.length-8) != "@cpp.edu")
            return Gson().toJson(Status.FAIL)

        val data = mapOf<String, Any>("username" to user.userName, "password" to user.password,
            "firstname" to user.firstname, "lastname" to user.lastname)
        userRef.create(data)
        return Gson().toJson(Status.SUCCESS)
    }

    fun encrypt(input: String) : String {
        val keyBytes = getResource("keyBytes.txt").readText().toByteArray()
        val cipher = Cipher.getInstance(transformation)
        val secretKey = SecretKeySpec(keyBytes.copyOf(16), algorithm)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        val encryptedText = cipher.doFinal(input.toByteArray(Charsets.UTF_8))
        return Base64.getEncoder().encodeToString(encryptedText)
    }

    fun decrypt(input: String) : String {
        val keyBytes = getResource("keyBytes.txt").readText().toByteArray()
        val cipher = Cipher.getInstance(transformation)
        val secretKey = SecretKeySpec(keyBytes.copyOf(16), algorithm)
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        val encryptedText = Base64.getDecoder().decode(input)

        val decryptedText = cipher.doFinal(encryptedText)
        return String(decryptedText)
    }
}
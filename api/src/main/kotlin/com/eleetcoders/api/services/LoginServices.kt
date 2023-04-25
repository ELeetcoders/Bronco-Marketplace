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

    fun checkCredentials(email : String, password : String) : String {
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
            return Gson().toJson(Status.FAIL)


        if (decrypt(target.get("password") as String) == decrypt(password)) {

            return Gson().toJson(Status.SUCCESS)
        }
        return Gson().toJson(Status.FAIL)
    }

    fun createNewUser(user : User): String {
        val db = FirestoreClient.getFirestore()
        val allUsers = db.collection("user")
        val userRef = allUsers.document(user.email)

        val userService = UserService()

        if (userRef.get().get().exists() || !userService.isValidEmail(user.email))
            return Gson().toJson(Status.FAIL)

        for (userReference in db.collection("user").get().get().documents) {
            if (user.userName.lowercase(Locale.getDefault()) ==
                (userReference.get("username") as String).lowercase(Locale.getDefault()))
            {
                return Gson().toJson(Status.FAIL)
            }
        }

        val data = mapOf<String, Any>("username" to user.userName, "password" to user.password,
            "firstname" to user.firstname, "lastname" to user.lastname)
        userRef.create(data)
        return Gson().toJson(Status.SUCCESS)
    }

    fun encrypt(input: String) : String {
        val secretKey = getBytes()
        val cipher = Cipher.getInstance(transformation)

        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        val encryptedText = cipher.doFinal(input.toByteArray(Charsets.UTF_8))
        return Base64.getEncoder().encodeToString(encryptedText)
    }

    fun decrypt(input: String) : String {
        val secretKey = getBytes()
        val cipher = Cipher.getInstance(transformation)

        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        val encryptedText = Base64.getDecoder().decode(input)

        val decryptedText = cipher.doFinal(encryptedText)
        return String(decryptedText)
    }

    private fun getBytes() : SecretKeySpec {
        val keyBytes = getResource("keyBytes.txt").readText().toByteArray()

        val secretKey : SecretKeySpec = if (keyBytes.size <= 16) {
            SecretKeySpec(keyBytes.copyOf(16), algorithm)
        } else {
            SecretKeySpec(keyBytes.copyOf(32), algorithm)
        }

        return secretKey
    }
}
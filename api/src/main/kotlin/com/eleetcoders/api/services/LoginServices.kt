package com.eleetcoders.api.services

import com.eleetcoders.api.models.Status
import com.eleetcoders.api.models.User
import com.google.cloud.firestore.QueryDocumentSnapshot
import com.google.common.io.Resources.getResource
import com.google.firebase.cloud.FirestoreClient
import com.google.gson.Gson
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

@Service
class LoginServices @Autowired constructor(
){
    companion object {
        const val ALGORITHM: String = "AES"
        const val TRANSFORMATION : String = "AES/ECB/PKCS5Padding"
    }

    fun verify(email: String) : String {
        val db = FirestoreClient.getFirestore()
        val userRef = db.collection("user").document(email)

        // Retrieve the document and extract the firstname and lastname fields
        val snapshot = userRef.get().get()
        val firstname = snapshot.getString("firstname")
        val lastname = snapshot.getString("lastname")
        val username = snapshot.getString("username")
        val profilePic = snapshot.getString("profilePic")

        // Do something with the firstname and lastname fields (e.g. add them to a map)
        val userData = mutableMapOf<String, Any>()
        if (firstname != null) userData["firstname"] = firstname
        if (lastname != null) userData["lastname"] = lastname
        if (username != null) userData["username"] = username
        if (profilePic != null) userData["profilePic"] = profilePic
        userData["email"] = email

        // Convert the map to a JSON string and return it
        return Gson().toJson(userData)
    }

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

        var verified = target.get("verified")
        if (verified == null) {
            verified = true
        }

        if (verified == false) {
            return Gson().toJson(Status.VERIFY)
        }

        if (decrypt(target.get("password") as String) == decrypt(password)) {
            return Gson().toJson(Status.SUCCESS)
        }

        return Gson().toJson(Status.FAIL)
    }

    @Autowired
    private lateinit var mailSender: JavaMailSender
    fun createNewUser(user : User, request: HttpServletRequest, response: HttpServletResponse): String {
        val db = FirestoreClient.getFirestore()
        val userRef = db.collection("user").document(user.email)

        val userService = UserService()

        if (userRef.get().get().exists() || !userService.isValidEmail(user.email))
            return Gson().toJson(Status.FAIL)

        val verificationId = UUID.randomUUID().toString()

        val data = mapOf<String, Any>("username" to user.userName, "password" to user.password,
            "firstname" to user.firstname, "lastname" to user.lastname, "verified" to false,
            "verificationId" to verificationId)
        userRef.create(data)

        val message = SimpleMailMessage()
        val toEmail = user.email
        val body = "Hi," + user.firstname + ", navigate to the verification link to verify email: " + "http://broncomarketplace.com/verify?id=" + verificationId
        val subject = "Verify CPP email address"
        message.setFrom("fromemail@gmail.com")
        message.setTo(toEmail)
        message.setText(body)
        message.setSubject(subject)
        mailSender.send(message)

        val session = request.getSession(true)
        //session.maxInactiveInterval = 86400; //1 day to verify email
        session.setAttribute("verificationId", verificationId)
        session.setAttribute(verificationId, user.email)
        response.setHeader("Set-Cookie", "JSESSIONID=" + session.getId() + "; Path=/; Domain=.broncomarketplace.com; SameSite=Lax; Max-Age=86400");
        return Gson().toJson(Status.VERIFY)
    }

    fun encrypt(input: String) : String {
        val keyBytes = getResource("keyBytes.txt").readText().toByteArray()
        val cipher = Cipher.getInstance(TRANSFORMATION)

        val secretKey : SecretKeySpec = if (keyBytes.size <= 16) {
            SecretKeySpec(keyBytes.copyOf(16), ALGORITHM)
        } else {
            SecretKeySpec(keyBytes.copyOf(32), ALGORITHM)
        }

        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        val encryptedText = cipher.doFinal(input.toByteArray(Charsets.UTF_8))
        return Base64.getEncoder().encodeToString(encryptedText)
    }

    fun decrypt(input: String) : String {
        val keyBytes = getResource("keyBytes.txt").readText().toByteArray()
        val cipher = Cipher.getInstance(TRANSFORMATION)

        val secretKey : SecretKeySpec = if (keyBytes.size <= 16) {
            SecretKeySpec(keyBytes.copyOf(16), ALGORITHM)
        } else {
            SecretKeySpec(keyBytes.copyOf(32), ALGORITHM)
        }

        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        val encryptedText = Base64.getDecoder().decode(input)

        val decryptedText = cipher.doFinal(encryptedText)
        return String(decryptedText)
    }
}
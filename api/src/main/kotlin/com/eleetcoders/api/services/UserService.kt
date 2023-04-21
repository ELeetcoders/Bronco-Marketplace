package com.eleetcoders.api.services

import com.eleetcoders.api.models.Product
import com.eleetcoders.api.models.Status
import com.eleetcoders.api.models.User
import com.google.firebase.cloud.FirestoreClient
import com.google.gson.Gson
import org.springframework.web.bind.annotation.RequestBody


class UserService {
    companion object {
        val ALLOWED_EMAIL_DOMAINS = arrayOf("cpp.edu")
    }

    /**
     * Retrieves the domain of an email address.
     *
     * Example:
     *   Input: test123@mydomain.com
     *   Output: mydomain.com
     *
     * If no domain could be detected, return null.
     *
     * @param email The email address.
     * @return The email domain, or null if not found.
     */
    fun getEmailDomain(email: String): String? {
        /* Fast validity check. */
        if (!email.contains('@')) return null

        val domain = email.split('@').last()

        /* Ensure something comes after the @ symbol. */
        if (domain.isEmpty()) return null

        return domain
    }

    /**
     * Returns true if the email is valid.
     *
     * @param email The email address.
     * @return True if the email address is valid, false otherwise.
     */
    fun isValidEmail(email: String): Boolean {
        val emailDomain = getEmailDomain(email) ?: return false

        /* Ensure domain is allowed. */
        return ALLOWED_EMAIL_DOMAINS.contains(emailDomain)
    }

    fun deleteUser(user: User) : String {
        val db = FirestoreClient.getFirestore()
        val collectionRef = db.listCollections()

        for (collection in collectionRef) {
            if (collection.id == "user")
                continue
            for (document in collection.listDocuments()) {
                if (document.get().get().data?.get("email") as String  == user.email)
                    document.delete()
            }
        }

        db.collection("user").document(user.email).delete()
        return Gson().toJson(Status.SUCCESS)
    }

    fun listProducts(user: User) : String {
        val db = FirestoreClient.getFirestore()
        val collectionRef = db.listCollections()
        val productList = ArrayList<Map<String, Any>>()

        for (collection in collectionRef) {
            if (collection.id == "user")
                continue
            for (document in collection.listDocuments())  {
                val data = document.get().get().data

                if (data?.get("email") == user.email) {
                    productList.add(data)
                }
            }
        }

        return Gson().toJson(productList)
    }

    fun createListing(user: User, name: String, desc: String, price: Double,
                      category: Product.Category, imageURL: String) : Boolean {

        if (price < 0 ||
            category == Product.Category.NONE)
            return false

        val db = FirestoreClient.getFirestore()
        val data = mutableMapOf<String, Any>("name" to name, "desc" to desc,
            "price" to price, "email" to user.email, "imageUrl" to imageURL)
        val docId = db.collection(category.name).add(data).get().id
        return db.collection(category.name).document(docId).get().get().exists()
    }

    fun removeListing(product: Product) : String {
        val db = FirestoreClient.getFirestore()
        val docRef = db.collection(product.category.name).document(product.id)

        if (!docRef.get().get().exists())
            return Gson().toJson(Status.FAIL)
        docRef.delete()
        return Gson().toJson(Status.SUCCESS)
    }
}
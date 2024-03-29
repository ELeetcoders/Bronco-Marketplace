package com.eleetcoders.api.services

import com.eleetcoders.api.models.Product
import com.eleetcoders.api.models.Status
import com.eleetcoders.api.models.User
import com.google.api.core.ApiFuture
import com.google.cloud.firestore.DocumentSnapshot
import com.google.firebase.cloud.FirestoreClient
import com.google.gson.Gson


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

    fun listProducts(email: String) : String {  /* Lists products without categorizing objects */
        val db = FirestoreClient.getFirestore()
        val productList = ArrayList<Map<String, Any>>()

        db.listCollections().forEach { collection ->
            val collectionName = collection.id
            if (collectionName != "user" && collectionName != "chats") {
                collection.whereEqualTo("email", email).get().get().forEach { document ->
                    val productData = HashMap(document.data) // create a new HashMap with the existing data
                    productData["id"] = document.id // add a new key-value pair for the document id
                    productList.add(productData)
                }
            }
        }

        return Gson().toJson(productList)
    }

    fun createListing(email: String, name: String, desc: String, price: Double,
                      category: Product.Category, imageURL: String) : Boolean {

        if (price < 0 ||
            category == Product.Category.NONE)
            return false

        val db = FirestoreClient.getFirestore()
        val userInfoRef = db.collection("user").document(email)
        val future: ApiFuture<DocumentSnapshot> = userInfoRef.get()
        val document = future.get()


        val firstname: String? = document.getString("firstname")
        val lastname: String? = document.getString("lastname")
        val username: String? = document.getString("username")

        val data = mutableMapOf<String, Any?>(
            "name" to name,
            "desc" to desc,
            "price" to price,
            "email" to email,
            "firstname" to firstname,
            "lastname" to lastname,
            "username" to username,
            "imageUrl" to imageURL
        )
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
package com.eleetcoders.api.services

import com.eleetcoders.api.models.Product
import com.eleetcoders.api.models.User
import com.google.firebase.cloud.FirestoreClient
import com.google.gson.Gson


class UserService {

    fun createUser(user: User): Boolean {

        val db = FirestoreClient.getFirestore()
        val userRef = db.collection("user").document(user.email)

        if (userRef.get().get().exists() ||
            user.email.length < 9 ||
            user.email.substring(user.email.length-8) != "@cpp.edu")
            return false

        val data = mapOf<String, Any>("username" to user.userName, "password" to user.password,
            "firstname" to user.firstname, "lastname" to user.lastname)
        userRef.create(data)
        return true

    }

    fun loginUser(user: User, password : String) : Boolean  {
        return user.checkPassword(password)
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

    fun createListing(user: User, product: Product) : Boolean {
        val db = FirestoreClient.getFirestore()
        val docRef = db.collection(product.category).document(product.id)

        if (docRef.get().get().exists())
            return false

        val data = mutableMapOf<String, Any>("name" to product.name, "desc" to product.desc,
            "price" to product.price, "email" to user.email)
        docRef.create(data)
        return true
    }

    fun removeListing(product: Product) : Boolean {
        val db = FirestoreClient.getFirestore()
        val docRef = db.collection(product.category).document(product.id)

        if (!docRef.get().get().exists())
            return false
        docRef.delete()
        return true
    }
}
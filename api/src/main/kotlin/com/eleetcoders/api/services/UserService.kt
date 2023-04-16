package com.eleetcoders.api.services

import com.eleetcoders.api.models.Product
import com.eleetcoders.api.models.Status
import com.eleetcoders.api.models.User
import com.google.firebase.cloud.FirestoreClient
import com.google.gson.Gson


class UserService {


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
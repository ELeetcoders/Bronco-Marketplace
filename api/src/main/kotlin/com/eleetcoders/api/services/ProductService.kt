package com.eleetcoders.api.services
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.api.core.ApiFuture
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.WriteResult
import com.google.firebase.cloud.FirestoreClient
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProductService @Autowired constructor(){

    fun getProduct(): String {
        val db: Firestore = FirestoreClient.getFirestore()
        val query = db.collection("test").document("mytestdoc")
        val querySnapshot = query.get().get()
        val field1 = querySnapshot.getString("field1")
        val field2 = querySnapshot.getString("field2")
        val field3 = querySnapshot.getString("field3")

        return "$field1\n$field2\n$field3"
    }

    fun getAllProducts(): String {
        val db: Firestore = FirestoreClient.getFirestore()
        val querySnapshot = db.collection("productDemo").get().get()
        val products = mutableListOf<Map<String, Any>>()
        for (document in querySnapshot.documents) {
            products.add(document.data)
        }
        val gson = Gson()
        return gson.toJson(products)
    }

    fun postProduct(): String {
        val db: Firestore = FirestoreClient.getFirestore()
        val docRef = db.collection("test").document("mytestdoc5")
        val data: MutableMap<String, Any> = HashMap()
        data["field1"] = "12345"
        data["field2"] = "1234"
        data["field3"] = "123"
        val result: ApiFuture<WriteResult> = docRef.set(data)

        return "Update time : " + result.get().updateTime
    }

    fun findProduct(id: String): String {
        val db: Firestore = FirestoreClient.getFirestore()
        val querySnapshot = db.collection("test").document(id).get().get().data

        return querySnapshot.toString()
    }

    fun deleteProduct(id: String) : Boolean {
        val db: Firestore = FirestoreClient.getFirestore()
        db.collection("test").document(id).delete()
        return true
    }

    fun updateProduct(id: String) : Boolean {
        val db: Firestore = FirestoreClient.getFirestore()
        db.collection("test").document(id).update("hello", "not world")
        return true
    }

    fun filterByPrice(max: Double) : String? {
        val db = FirestoreClient.getFirestore()
        val docRef = db.collection("productDemo").get().get().documents
        val data = ArrayList<Map<String, Any>>()
        for (document in docRef) {
            if (document.data["price"].toString().toDouble() <= max) // Price will always be numeric
                data.add(document.data)
        }
        return data.toString()
    }

    fun searchByTerm(term: String): String? {
        val db = FirestoreClient.getFirestore()
        val docRef = db.collection("productDemo").get().get().documents
        val data = ArrayList<Map<String, Any>>()
        for (document in docRef) {
            if (document.data["name"].toString().contains(term))
                data.add(document.data)
        }

        return Gson().toJson(data)
    }
}
package com.eleetcoders.api.services
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.api.core.ApiFuture
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.WriteResult
import com.google.firebase.cloud.FirestoreClient
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
        val querySnapshot = db.collection("demo").get().get()
        val products = mutableListOf<Map<String, Any>>()
        for (document in querySnapshot.documents) {
            products.add(document.data)
        }
        val objectMapper = ObjectMapper().registerModule(KotlinModule())
        return objectMapper.writeValueAsString(products)
    }

    fun postProduct(): String {
        val db: Firestore = FirestoreClient.getFirestore()
        val docRef = db.collection("test").document("mytestdoc")
        val data: MutableMap<String, Any> = HashMap()
        data["field1"] = "12345"
        data["field2"] = "1234"
        data["field3"] = "123"
        val result: ApiFuture<WriteResult> = docRef.set(data)

        return "Update time : " + result.get().updateTime
    }


}
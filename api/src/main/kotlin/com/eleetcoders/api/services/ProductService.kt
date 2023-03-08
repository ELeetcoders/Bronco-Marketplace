package com.eleetcoders.api.services
import com.eleetcoders.api.models.Product
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.api.core.ApiFuture
import com.google.cloud.firestore.DocumentReference
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.WriteResult
import com.google.firebase.cloud.FirestoreClient
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@Service
class ProductService @Autowired constructor() {

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

    fun postProduct(product: Product): String {
        val db: Firestore = FirestoreClient.getFirestore()
        val docRef = db.collection(product.category).document(product.id)
        val data: MutableMap<String, Any> = hashMapOf(
            "name" to product.name,
            "price" to product.price, "username" to product.username
        )

        return docRef.create(data).toString()
    }
    fun deleteProduct(product: Product): Boolean {
        val docRef = findProduct(product)
        docRef.delete()
        return true
    }

    private fun findProduct(product: Product): DocumentReference{
        val db: Firestore = FirestoreClient.getFirestore()
        return db.collection(product.category).document(product.id)
    }

    fun updateProduct(product: Product): Boolean {
        val productRef = findProduct(product)
        productRef.update("price", 500)
        return true
    }

    fun filterByPrice(max: Double): String? {
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
        val collections = db.listCollections()
        val data = ArrayList<Map<String, Any>>()

        for (collection in collections) {
            for (document in collection.get().get().documents) {
                if (document.data["name"].toString().lowercase(Locale.getDefault()).contains(term))
                    data.add(document.data)
            }
        }

        return Gson().toJson(data)
    }

    fun sortByName(reversed: Boolean): String {
        val db = FirestoreClient.getFirestore()
        val collection = db.collection("productDemo")
        val data = ArrayList<Map<String, Any>>()

        //for (collection in collectList) {
        val docRefs = collection.get().get().documents
        for (document in docRefs) {
            data.add(document.data)
        }
        //}
        if (reversed)
            data.sortWith(NameComparator().reversed())
        else
            data.sortWith(NameComparator())
        return Gson().toJson(data)
    }

    fun sortByPrice(reversed: Boolean): String {
        val db = FirestoreClient.getFirestore()
        val collection = db.collection("productDemo")
        val data = ArrayList<Map<String, Any>>()

        //for (collection in collectList) {
        val docRefs = collection.get().get().documents
        for (document in docRefs) {
            data.add(document.data)
        }
        //}
        if (reversed)
            data.sortWith(PriceComparator().reversed())
        else
            data.sortWith(PriceComparator())
        return Gson().toJson(data)
    }
}
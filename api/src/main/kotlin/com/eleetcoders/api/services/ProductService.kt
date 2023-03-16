package com.eleetcoders.api.services
import com.eleetcoders.api.models.Category
import com.eleetcoders.api.models.Product
import com.google.cloud.firestore.DocumentReference
import com.google.cloud.firestore.Firestore
import com.google.firebase.cloud.FirestoreClient
import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.ArrayList

@Service
class ProductService @Autowired constructor() {

    fun getAllProducts(): String {
        val db: Firestore = FirestoreClient.getFirestore()
        val collections = db.listCollections()
        val products = mutableListOf<Map<String, Any>>()

        for (collection in collections) {
            if (collection.id == "users")
                continue
            for (document in collection.get().get().documents) {
                products.add(document.data)
            }
        }
        return Gson().toJson(products)
    }

    fun getAllProductsByCategory(category: Category): String {
        val db: Firestore = FirestoreClient.getFirestore()
        val collections = db.listCollections()
        val products = mutableListOf<Map<String, Any>>()
        for (collection in collections) {
            if (collection.id != category.category)
                continue
            for (document in collection.get().get().documents) {
                products.add(document.data)
            }
        }
        return Gson().toJson(products)
    }

    fun postProduct(product: Product): Boolean {
        val db: Firestore = FirestoreClient.getFirestore()
        val docRef = db.collection(product.category.name).document(product.id)
        val data: MutableMap<String, Any> = hashMapOf(
            "name" to product.name, "price" to product.price,
            "desc" to product.desc, "email" to product.email
        )

        return !docRef.create(data).isCancelled
    }
    fun deleteProduct(product: Product): Boolean {
        val docRef = findProduct(product)
        docRef.delete()
        return true
    }

    private fun findProduct(product: Product): DocumentReference{
        val db: Firestore = FirestoreClient.getFirestore()
        return db.collection(product.category.name).document(product.id)
    }

    fun updateProduct(product: Product, name: String?, desc: String?, price: Double?): Boolean {
        val productRef = findProduct(product)
        var upName = product.name
        var upDesc = product.desc
        var upPrice = product.price
        if (name != null)
            upName = name
        if (desc != null)
            upDesc = desc
        if (price != null)
            upPrice = price

        val data = mapOf<String, Any>("name" to upName, "desc" to upDesc, "price" to upPrice)
        productRef.update(data)

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

    fun getProduct(product: Product): String {
        return Gson().toJson(product)
    }

}
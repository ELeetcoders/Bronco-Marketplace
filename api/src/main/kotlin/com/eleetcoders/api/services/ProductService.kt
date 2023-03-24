package com.eleetcoders.api.services
import com.eleetcoders.api.models.Product
import com.google.cloud.firestore.DocumentReference
import com.google.cloud.firestore.Firestore
import com.google.firebase.cloud.FirestoreClient
import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.Double
import kotlin.Long

@Service
class ProductService @Autowired constructor() {

    fun getAllProducts(): String {
        val db: Firestore = FirestoreClient.getFirestore()
        val collections = db.listCollections()
        val products = HashMap<String, ArrayList<Map<String, Any>>>()

        for (collection in collections) {
            val collectionName = collection.id
            if (collectionName == "user")
                continue
            val temp = ArrayList<Map<String,Any>>()
            for (document in collection.get().get().documents) {
                temp.add(document.data)
            }
            products[collectionName] = temp
        }
        return Gson().toJson(products)
    }

    fun getAllProductsByCategory(category: Product.Category): String {
        val db: Firestore = FirestoreClient.getFirestore()
        val collections = db.listCollections()
        val products = mutableListOf<Map<String, Any>>()
        for (collection in collections) {
            if (collection.id == category.name)
                for (document in collection.get().get().documents) {
                    products.add(document.data)
            }
        }
        return Gson().toJson(products)
    }

    private fun findProduct(product: Product): DocumentReference{
        val db: Firestore = FirestoreClient.getFirestore()
        return db.collection(product.category.name).document(product.id)
    }

    fun filterByPrice(max: Double): String? {
        val db = FirestoreClient.getFirestore()
        val collections = db.listCollections()
        val data = HashMap<String, ArrayList<Product>>()
        for (collection in collections) {
            if (collection.id == "user")
                continue
            val list = ArrayList<Product>()
            for (document in collection.get().get().documents) {
                val temp = document.data
                val product = dataToProduct(temp, document.id, Product.ignoreCase(collection.id))
                if (product.price <= max)
                    list.add(product)
            }
            data[collection.id] = list
        }
        return Gson().toJson(data)
    }

    fun searchByEmail(email: String) : String {
        val db = FirestoreClient.getFirestore()
        val collections = db.listCollections()
        val data = HashMap<String, ArrayList<Product>>()

        for (collection in collections) {
            val collectionName = collection.id
            if (collectionName == "user") continue

            val productList = ArrayList<Product>()

            for (document in collection.get().get().documents){
                val productID = document.id
                if (document.data["email"] == email)
                    productList.add(dataToProduct(document.data, productID, Product.ignoreCase(collectionName)))
            }
            data[collectionName] = productList
        }

        return Gson().toJson(data)
    }

    fun searchByTerm(term: String): String? {
        val db = FirestoreClient.getFirestore()
        val collections = db.listCollections()

        val products = HashMap<String, ArrayList<Map<String, Any>>>()
        for (collection in collections) {
            val collectionName = collection.id
            if (collectionName == "user")
                continue
            val temp = ArrayList<Map<String,Any>>()
            for (document in collection.get().get().documents) {
                if (document.data["name"].toString().lowercase(Locale.getDefault()).
                    contains(term.lowercase(Locale.getDefault())))
                {
                 temp.add(document.data)
                }
            }
            products[collectionName] = temp
        }
        return Gson().toJson(products)
    }

    fun sortByName(reversed: Boolean): String {
        val db = FirestoreClient.getFirestore()
        val collection = db.collection("productDemo")
        val data = ArrayList<Map<String, Any>>()

        val docRefs = collection.get().get().documents
        for (document in docRefs) {
            data.add(document.data)
        }
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

        val docRefs = collection.get().get().documents
        for (document in docRefs) {
            data.add(document.data)
        }

        if (reversed)
            data.sortWith(PriceComparator().reversed())
        else
            data.sortWith(PriceComparator())
        return Gson().toJson(data)
    }

    fun getProduct(product: Product): String {
        return Gson().toJson(product)
    }

    private fun dataToProduct(data: MutableMap<String, Any>, id: String, category: Product.Category) : Product{
        var price : Double

        try {
            price = data["price"] as Double
        } catch (e : Exception) {
            price = (data["price"] as Long).toDouble()
        }

        return Product(id, data["name"] as String, price, data["email"] as String,
            data["desc"] as String, data["imageUrl"] as String, category)
    }
}
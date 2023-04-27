package com.eleetcoders.api.services
import com.eleetcoders.api.models.Product
import com.google.cloud.firestore.Firestore
import com.google.firebase.cloud.FirestoreClient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@Service
class ProductService @Autowired constructor() {

    fun getAllProducts(categoryName: String? = null): String {
        val db: Firestore = FirestoreClient.getFirestore()
        val collections = db.listCollections()

        val products = HashMap<String, MutableList<Product>>()

        for (collection in collections) {
            val collectionName = collection.id

            /* Do not include users. */
            if (collectionName == "user")
                continue

            /* If we specified a category, only return results from that one. */
            if (categoryName != null && collectionName != categoryName) continue

            val temp = ArrayList<Product>()
            for (document in collection.get().get().documents) {
                temp.add(dataToProduct(document.data, document.id, Product.ignoreCase(collectionName)))
            }
            products[collectionName] = temp
        }
        return Gson().toJson(products)
    }

    fun getAllProductsByCategory(category: Product.Category): String {
        return getAllProducts(category.name)
    }

    fun filterByPrice(max: Double): String {
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
        val values = getAllProducts()
        val typeToken = object : TypeToken<Map<String, MutableList<Product>>>() {}.type
        val data = Gson().fromJson<Map<String, MutableList<Product>>>(values, typeToken)

        for (list in data.values) {
            if (reversed)
                list.sortByDescending { it.name }
            else
                list.sortBy { it.name }
        }

        return Gson().toJson(data)
    }

    fun sortByPrice(reversed: Boolean): String {
        val values = getAllProducts()
        val typeToken = object : TypeToken<Map<String, MutableList<Product>>>() {}.type
        val data = Gson().fromJson<Map<String, MutableList<Product>>>(values, typeToken)

        for (list in data.values) {
            if (reversed)
                list.sortByDescending { it.price }
            else
                list.sortBy { it.price }
        }

        return Gson().toJson(data)
    }

    fun getProduct(product: Product): String {
        return Gson().toJson(product)
    }

    private fun dataToProduct(data: MutableMap<String, Any>, id: String, category: Product.Category) : Product{

        val price = try {
            data["price"] as Double
        } catch (e : Exception) {
            (data["price"] as Long).toDouble()
        }

        return Product(
            id,
            data["name"] as? String ?: "",
            price,
            data["email"] as? String ?: "",
            data["firstname"] as? String ?: "",
            data["lastname"] as? String ?: "",
            data["username"] as? String ?: "",
            data["desc"] as? String ?: "",
            data["imageUrl"] as? String ?: "",
            category
        )
    }
}
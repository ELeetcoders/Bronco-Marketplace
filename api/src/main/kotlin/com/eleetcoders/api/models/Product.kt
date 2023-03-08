package com.eleetcoders.api.models

import com.google.gson.Gson

class Product (id:String = "", name:String = "", price:Int = 0,
                category: String, username: String){
    val id : String
    val name : String
    val price: Int
    val category: String
    val username: String

    init{
        this.id = id
        this.name = name
        this.price = price
        this.category = category
        this.username = username
    }
    override fun toString(): String {
        return Gson().toJson(this)
    }
}
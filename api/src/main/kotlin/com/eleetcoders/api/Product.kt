package com.eleetcoders.api

class Product (id:String = "", name:String = "", price:Double = 0.0){
    val id : String
    val name : String
    val price: Double

    init{
        this.id = id
        this.name = name
        this.price = price
    }
    override fun toString(): String {
        return "id: $id\nname: $name\nprice: $price"
    }
}
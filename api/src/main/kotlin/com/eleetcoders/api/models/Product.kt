package com.eleetcoders.api.models

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.Gson

class Product @JsonCreator constructor(
    @JsonProperty("id") val id: String = "",
    @JsonProperty("name") val name: String = "",
    @JsonProperty("price") val price: Double = -1.0,
    @JsonProperty("email") val email: String = "",
    @JsonProperty("desc") val desc: String = "",
    @JsonProperty("category") private val temp: String,
    val category : Category = ignoreCase(temp)


    ) {
    override fun toString(): String {
        return Gson().toJson(this)
    }

    enum class Category {
        NONE,
        BOOK,
        TECH,
        SERVICES
    }
    companion object {
        fun ignoreCase(input: String): Category {
            return Category.values().firstOrNull { it.name.equals(input, true) } ?: return Category.NONE
        }
    }
}
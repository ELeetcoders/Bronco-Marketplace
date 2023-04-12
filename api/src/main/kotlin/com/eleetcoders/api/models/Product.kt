package com.eleetcoders.api.models

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.google.gson.Gson
import java.util.*

class Product @JsonCreator constructor(
    @JsonProperty("id") val id: String = "",
    @JsonProperty("name") val name: String = "",
    @JsonProperty("price") val price: Double = -1.0,
    @JsonProperty("email") val email: String = "",
    @JsonProperty("desc") val desc: String = "",
    @JsonProperty("imageUrl") val imageUrl: String = "",

    @JsonProperty("category", required = true)
    @JsonDeserialize(using = CategoryDeserializer::class)
    val category : Category
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
    class CategoryDeserializer : JsonDeserializer<Category>() {
        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): Category {
            val value = parser.valueAsString.uppercase(Locale.getDefault())
            return Category.valueOf(value)
        }

    }
}

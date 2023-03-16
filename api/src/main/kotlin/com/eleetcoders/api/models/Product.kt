package com.eleetcoders.api.models

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.Gson

class Product @JsonCreator constructor(
    @JsonProperty("id") val id: String = "",
    @JsonProperty("name") val name: String = "",
    @JsonProperty("price") val price: Int = 0,
    @JsonProperty("category") val category: String = "",
    @JsonProperty("email") val email: String = "",
    @JsonProperty("desc") val desc: String = ""
) {
    override fun toString(): String {
        return Gson().toJson(this)
    }
}
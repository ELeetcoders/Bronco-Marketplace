package com.eleetcoders.api.models

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.Gson

class User @JsonCreator constructor(
    @JsonProperty("email") val email : String = "",
    @JsonProperty("password") val password : String = "",
    @JsonProperty("username") val userName : String = ""
){
    fun checkPassword(ps : String) : Boolean{
        return password == ps
    }


    override fun toString(): String {
        return Gson().toJson(this)
    }
}
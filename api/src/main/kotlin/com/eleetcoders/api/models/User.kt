package com.eleetcoders.api.models

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.Gson

class User @JsonCreator constructor(
    @JsonProperty("email") val email : String = "",
    @JsonProperty("password") val password : String = "",
    @JsonProperty("username") val userName : String = "",
    @JsonProperty("firstname") val firstname : String = "",
    @JsonProperty("lastname") val lastname : String = ""
){
    constructor(
        map : Map<String, String>
    ) : this(map["email"] as String, map["password"] as String, map["username"] as String,
        map["firstname"] as String, map["lastname"] as String)
    fun checkPassword(ps : String) : Boolean{
        return password == ps
    }


    override fun toString(): String {
        return Gson().toJson(this)
    }
}
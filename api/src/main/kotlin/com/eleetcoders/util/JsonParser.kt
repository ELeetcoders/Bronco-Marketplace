package com.eleetcoders.api.controllers

import com.fasterxml.jackson.databind.ObjectMapper

class getJsonVals {
    inline fun <reified  T: Any >parseJSON(data: Map<String, Any>, key: String): T {
        return data[key]?.let {
            ObjectMapper().convertValue(it as Map<*,*>, T::class.java)
        } ?: throw IllegalArgumentException("Missing required $key field in request body")
    }
}
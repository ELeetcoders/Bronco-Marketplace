package com.eleetcoders.api.models

import com.fasterxml.jackson.annotation.JsonProperty

class Category constructor(
    @JsonProperty("category") val category: String = "",
){}
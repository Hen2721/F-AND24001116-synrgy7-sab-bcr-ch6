package com.example.domain


import com.google.gson.annotations.SerializedName

data class Edition(
    @SerializedName("direction")
    val direction: Any,
    @SerializedName("englishName")
    val englishName: String,
    @SerializedName("format")
    val format: String,
    @SerializedName("identifier")
    val identifier: String,
    @SerializedName("language")
    val language: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("type")
    val type: String
)
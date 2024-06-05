package com.example.data.remote.response


import com.google.gson.annotations.SerializedName

data class RegisterResponseItem(
    @SerializedName("email")
    val email: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("username")
    val username: String
)
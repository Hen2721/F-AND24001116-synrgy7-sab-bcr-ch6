package com.example.data.remote.response


import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("data")
    val data: RegisterResponseItem,
    @SerializedName("message")
    val message: String
)
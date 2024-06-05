package com.example.data.remote.response


import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("data")
    val data: ProfileResponseItem,
    @SerializedName("message")
    val message: String
)
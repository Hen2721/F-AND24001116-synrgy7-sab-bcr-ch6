package com.example.data.remote.response

import com.google.gson.annotations.SerializedName

data class EditProfileImageResponse(
    @SerializedName("data")
    val data: EditProfileImageResponseItem,
    @SerializedName("message")
    val message: String
)
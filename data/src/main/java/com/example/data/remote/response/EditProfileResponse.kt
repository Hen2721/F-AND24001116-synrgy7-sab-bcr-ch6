package com.example.data.remote.response


import com.google.gson.annotations.SerializedName

data class EditProfileResponse(
    @SerializedName("data")
    val data: EditProfileResponseItem,
    @SerializedName("message")
    val message: String
)
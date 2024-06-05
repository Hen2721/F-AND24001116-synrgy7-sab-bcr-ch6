package com.example.data.remote.response

import com.google.gson.annotations.SerializedName

data class EditProfileImageResponseItem(
    @SerializedName("image")
    val image: String,
)
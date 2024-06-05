package com.example.data.remote.response


import com.example.domain.Profile
import com.google.gson.annotations.SerializedName

data class ProfileResponseItem(
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("image")
    val image: String?,
    @SerializedName("name")
    val name: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("role")
    val role: String,
    @SerializedName("username")
    val username: String
)

fun ProfileResponseItem.toProfile(): Profile {
    return Profile(
        name = name.orEmpty(),
        username = username.orEmpty(),
        email = email.orEmpty(),
        phone = phone.orEmpty(),
        image = image.orEmpty()
    )
}
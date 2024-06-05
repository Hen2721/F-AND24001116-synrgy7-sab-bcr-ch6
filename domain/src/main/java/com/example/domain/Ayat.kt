package com.example.domain


import com.google.gson.annotations.SerializedName

data class Ayat(
    @SerializedName("audio")
    val audio: String?,
    @SerializedName("audioSecondary")
    val audioSecondary: List<String>?,
    @SerializedName("hizbQuarter")
    val hizbQuarter: Int,
    @SerializedName("juz")
    val juz: Int,
    @SerializedName("manzil")
    val manzil: Int,
    @SerializedName("number")
    val number: Int,
    @SerializedName("numberInSurah")
    val numberInSurah: Int,
    @SerializedName("page")
    val page: Int,
    @SerializedName("ruku")
    val ruku: Int,
    @SerializedName("sajda")
    val sajda: Boolean,
    @SerializedName("text")
    val text: String,
    var translationText: String,
)
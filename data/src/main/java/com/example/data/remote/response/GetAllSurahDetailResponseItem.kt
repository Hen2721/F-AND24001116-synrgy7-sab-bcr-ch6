package com.example.data.remote.response


import com.example.domain.Ayat
import com.example.domain.Edition
import com.example.domain.SurahDetail
import com.google.gson.annotations.SerializedName

data class GetAllSurahDetailResponseItem(
    @SerializedName("ayahs")
    val ayahs: List<Ayat>,
    @SerializedName("edition")
    val edition: Edition,
    @SerializedName("englishName")
    val englishName: String,
    @SerializedName("englishNameTranslation")
    val englishNameTranslation: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("number")
    val number: Int,
    @SerializedName("numberOfAyahs")
    val numberOfAyahs: Int,
    @SerializedName("revelationType")
    val revelationType: String
)

fun GetAllSurahDetailResponseItem.toSurahDetail(): SurahDetail {
    return SurahDetail(
        ayahs = ayahs.orEmpty(),
        edition = edition!!,
        englishName = englishName.orEmpty(),
        englishNameTranslation = englishNameTranslation.orEmpty(),
        name = name.orEmpty(),
        number = number ?: -1,
        numberOfAyahs = numberOfAyahs ?: -1,
        revelationType = revelationType.orEmpty()
    )
}
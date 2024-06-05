package com.example.data.remote.response


import com.example.domain.Surah
import com.google.gson.annotations.SerializedName

data class GetAllSurahResponseItem(
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

fun GetAllSurahResponseItem.toSurah(): Surah {
    return Surah(
        englishName = englishName.orEmpty(),
        englishNameTranslation = englishNameTranslation.orEmpty(),
        name = name.orEmpty(),
        number = number ?: -1,
        numberOfAyahs = numberOfAyahs ?: -1,
        revelationType = revelationType.orEmpty()
    )
}
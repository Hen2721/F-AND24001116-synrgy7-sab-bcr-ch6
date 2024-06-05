package com.example.domain.repository

import com.example.domain.Profile
import com.example.domain.Surah
import com.example.domain.SurahDetail
import okhttp3.MultipartBody

interface MainRepository {
    suspend fun fetchAllSurahData(): List<Surah>
    suspend fun fetchSurahDetail(noSurah: Int): SurahDetail
    suspend fun fetchProfile(token: String): Profile
    suspend fun updateProfile(token: String, user: Profile): String
    suspend fun uploadProfileImage(token: String, image: MultipartBody.Part): String
}
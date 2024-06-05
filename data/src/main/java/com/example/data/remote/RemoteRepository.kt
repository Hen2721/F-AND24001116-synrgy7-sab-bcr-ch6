package com.example.data.remote

import com.example.data.remote.response.toProfile
import com.example.data.remote.response.toSurah
import com.example.data.remote.response.toSurahDetail
import com.example.data.remote.service.LampahService
import com.example.data.remote.service.QuranService
import com.example.domain.Login
import com.example.domain.LoginBody
import com.example.domain.Profile
import com.example.domain.Surah
import com.example.domain.SurahDetail
import com.example.domain.User
import com.example.domain.repository.GuestRepository
import com.example.domain.repository.MainRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class RemoteRepository @Inject constructor(
    private val quranService: QuranService,
    private val lampahService: LampahService
): MainRepository, GuestRepository {
    override suspend fun fetchAllSurahData(): List<Surah> {
        return quranService.getAllSurah().data?.map { result -> result.toSurah() }.orEmpty()
    }

    override suspend fun fetchSurahDetail(noSurah: Int): SurahDetail {
        val surahDetailList = quranService.getSurahByNumber(noSurah).data?.map { result -> result.toSurahDetail() }.orEmpty()
        val surahDetailAr = surahDetailList.find { it.edition.language == "ar" }
        val surahDetailId = surahDetailList.find { it.edition.language == "id" }

        surahDetailId?.ayahs?.map { ayat ->
            surahDetailAr?.ayahs?.filter { it.number == ayat.number }?.forEach { it.translationText = ayat.text }
        }

        return surahDetailAr!!
    }

    override suspend fun fetchProfile(token: String): Profile {
        return lampahService.getProfile("Bearer $token").data.toProfile()
    }

    override suspend fun updateProfile(token: String, user: Profile): String {
        return lampahService.updateProfile("Bearer $token", user).message
    }

    override suspend fun register(user: User): String {
        return lampahService.register(user).message
    }

    override suspend fun login(user: LoginBody): Login {
        return Login(
            message = lampahService.login(user).message,
            token = lampahService.login(user).data.token
        )
    }

    override suspend fun uploadProfileImage(token: String, image: MultipartBody.Part): String {
        return lampahService.uploadProfileImage("Bearer $token", image).message
    }
}
package com.example.data.remote.service

import com.example.data.remote.response.EditProfileImageResponse
import com.example.data.remote.response.EditProfileResponse
import com.example.data.remote.response.LoginResponse
import com.example.data.remote.response.ProfileResponse
import com.example.data.remote.response.RegisterResponse
import com.example.domain.LoginBody
import com.example.domain.Profile
import com.example.domain.User
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part

interface LampahService {
    @POST("auth/register")
    suspend fun register(@Body user: User): RegisterResponse

    @POST("auth/login")
    suspend fun login(@Body user: LoginBody): LoginResponse

    @GET("auth/me")
    suspend fun getProfile(@Header("Authorization") token: String): ProfileResponse

    @PATCH("auth/me")
    suspend fun updateProfile(@Header("Authorization") token: String, @Body user: Profile): EditProfileResponse

    @Multipart
    @PATCH("auth/me/image")
    suspend fun uploadProfileImage(@Header("Authorization") token: String, @Part image: MultipartBody.Part): EditProfileImageResponse
}
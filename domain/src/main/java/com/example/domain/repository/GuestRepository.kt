package com.example.domain.repository

import com.example.domain.Login
import com.example.domain.LoginBody
import com.example.domain.User

interface GuestRepository {
    suspend fun register(user: User): String
    suspend fun login(user: LoginBody): Login
}


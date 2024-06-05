package com.example.domain.repository

import com.example.domain.User


interface RegisterRepository {
    suspend fun validateRegisterInput(user: User): Boolean
}
package ru.kpfu.itis.t_travel.domain.repository

import ru.kpfu.itis.t_travel.domain.model.User

interface ProfileRepository {
    suspend fun getProfile():User
    suspend fun updateProfile(user: User)
}
package ru.kpfu.itis.t_travel.data.repository

import ru.kpfu.itis.t_travel.data.model.UserDto
import ru.kpfu.itis.t_travel.data.remote.ApiService
import ru.kpfu.itis.t_travel.domain.model.User
import ru.kpfu.itis.t_travel.domain.repository.ProfileRepository
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ProfileRepository {
    override suspend fun getProfile(): User {
        return apiService.getUserProfile().toDomain()
    }

    override suspend fun updateProfile(user: User) {
        val userDto = UserDto(
            id = user.id,
            username = user.username,
            firstName = user.firstName,
            lastName = user.lastName,
            email = user.email,
            phone = user.phone
        )
        apiService.updateUserProfile(userDto)
    }
}
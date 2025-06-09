package ru.kpfu.itis.t_travel.domain.useCase.profile

import ru.kpfu.itis.t_travel.domain.model.User
import ru.kpfu.itis.t_travel.domain.repository.ProfileRepository
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(user: User) {
        repository.updateProfile(user)
    }
}
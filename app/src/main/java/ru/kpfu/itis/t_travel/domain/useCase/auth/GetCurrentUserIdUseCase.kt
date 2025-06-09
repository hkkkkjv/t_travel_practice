package ru.kpfu.itis.t_travel.domain.useCase.auth

import ru.kpfu.itis.t_travel.data.repository.AuthRepositoryImpl
import javax.inject.Inject

class GetCurrentUserIdUseCase @Inject constructor(
    private val authRepositoryImpl: AuthRepositoryImpl
){
    suspend operator fun invoke(): Int {
        return authRepositoryImpl.userId()
    }
}

package com.example.map.domain

import javax.inject.Inject

class LoginUserUseCase @Inject constructor(private val repository: MapRepository) {

    suspend operator fun invoke(user: User): RegistrationUserResult {
        return repository.login(user)
    }
}
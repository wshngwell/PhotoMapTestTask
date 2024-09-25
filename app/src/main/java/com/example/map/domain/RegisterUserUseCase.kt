package com.example.map.domain

import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(private val repository: MapRepository) {
    suspend operator fun invoke(user: User): RegistrationUserResult {
        return repository.resister(user)
    }
}
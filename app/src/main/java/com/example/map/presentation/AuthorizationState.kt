package com.example.map.presentation

import com.example.map.domain.RegistrationUserResult

sealed class AuthorizationState {
    class Success(val registrationUserResult: RegistrationUserResult) : AuthorizationState()
    data class Error(val errorName: String) : AuthorizationState()
    data object Loading : AuthorizationState()
}
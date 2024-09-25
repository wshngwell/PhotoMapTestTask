package com.example.map.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.map.data.network.MapRepositoryImpl
import com.example.map.domain.RegisterUserUseCase
import com.example.map.domain.User
import com.example.map.presentation.AuthorizationState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(
    val registrationUseCase: RegisterUserUseCase
) : ViewModel() {


    private val _accessTokenLiveData = MutableLiveData<AuthorizationState>()

    val accessTokenLiveData: MutableLiveData<AuthorizationState>
        get() = _accessTokenLiveData

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        if (throwable.message.toString() == "HTTP 400 ") {
            _accessTokenLiveData.value =
                AuthorizationState.Error("Пароль неверный")
        } else {
            _accessTokenLiveData.value =
                AuthorizationState.Error("Нет подключения к сети")
        }
    }

    fun register(login: String, passwordFirst: String, passwordSecond: String) {
        if (!loginValidation(login)) return
        if (!passwordFirstValidation(passwordFirst)) return
        if (!passwordEqualityValidation(passwordFirst, passwordSecond)) return

        _accessTokenLiveData.value = AuthorizationState.Loading

        val user = User(login, passwordFirst)
        viewModelScope.launch(exceptionHandler) {
            val answer = registrationUseCase(user)
            _accessTokenLiveData.value = AuthorizationState.Success(answer)
        }
    }

    private fun loginValidation(login: String): Boolean {
        if (login.length < 4 || login.length > 32) {
            _accessTokenLiveData.value =
                AuthorizationState.Error("Логин должен быть размером от 4 до 32")
            return false
        }
        return true
    }

    private fun passwordFirstValidation(password: String): Boolean {
        if (password.length < 8 || password.length > 500) {
            _accessTokenLiveData.value =
                AuthorizationState.Error("Пароль должен быть размером от 8 до 500")
            return false
        }
        return true
    }

    private fun passwordEqualityValidation(passwordFirst: String, passwordSecond: String): Boolean {
        if (passwordFirst != passwordSecond) {
            _accessTokenLiveData.value =
                AuthorizationState.Error("Пароли не совпадают")
            return false
        }
        return true
    }
}
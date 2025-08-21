package com.compose.wan.android.service

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.wan.android.net.WanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel  @Inject constructor(
    private val wanRepository: WanRepository
): ViewModel() {
    val username = TextFieldState()
    val password = TextFieldState()
    val repassword = TextFieldState()

    fun register() {
        viewModelScope.launch {
            wanRepository.register(username.text.toString(), password.text.toString(), repassword.text.toString())
        }
    }
}
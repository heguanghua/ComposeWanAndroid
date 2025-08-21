package com.compose.wan.android.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.compose.wan.android.service.RegisterViewModel
import com.compose.wan.android.widget.NormalButton

@Composable
fun RegisterScreen(viewModel: RegisterViewModel = hiltViewModel()) {
    Column {
        Text("RegisterScreen")
        BasicTextField(
            state = viewModel.username,
        )
        BasicTextField(
            state = viewModel.password,
        )
        BasicTextField(
            state = viewModel.repassword,
        )
        NormalButton {
            viewModel.register()
        }
    }
}
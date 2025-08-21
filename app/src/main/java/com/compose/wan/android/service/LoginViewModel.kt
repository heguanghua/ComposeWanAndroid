package com.compose.wan.android.service

import androidx.lifecycle.ViewModel
import com.compose.wan.android.net.WanRepository
import javax.inject.Inject

class LoginViewModel  @Inject constructor(
    private val wanRepository: WanRepository
): ViewModel() {

}
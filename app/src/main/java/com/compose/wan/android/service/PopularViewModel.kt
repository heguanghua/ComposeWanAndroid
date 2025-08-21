package com.compose.wan.android.service

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.wan.android.constant.LoadStatus
import com.compose.wan.android.model.response.SingleHotKey
import com.compose.wan.android.model.response.SingleWeb
import com.compose.wan.android.net.WanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PopularState(
    val loadStatus: LoadStatus = LoadStatus.Loading,
    val hotKeys: List<SingleHotKey>? = null,
    var hotWebs: List<SingleWeb>? = null
)

@HiltViewModel
class PopularViewModel  @Inject constructor(
    private val wanRepository: WanRepository
): ViewModel() {
    private val status = MutableStateFlow(LoadStatus.Loading)

    private val hotKeys = MutableStateFlow(listOf<SingleHotKey>())

    private val hotWebs = MutableStateFlow(listOf<SingleWeb>())

    private val _uiState = MutableStateFlow(PopularState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(status, hotKeys, hotWebs) { status, hotKeys, hotWebs ->
                PopularState(
                    loadStatus = status,
                    hotKeys = hotKeys,
                    hotWebs = hotWebs
                )
            }.catch {

            }.collect {
                _uiState.update {
                    it.copy(
                        loadStatus = it.loadStatus,
                        hotKeys = it.hotKeys,
                        hotWebs = it.hotWebs
                    )
                }
            }
        }
    }

    fun getPopularInfo() {
        viewModelScope.launch {
            try {
                val hotKey = async { wanRepository.getHotKeys() }
                val hotWeb = async { wanRepository.getHotWebs() }
                hotKeys.value = hotKey.await()
                hotWebs.value = hotWeb.await()
                status.value = LoadStatus.Finish
                _uiState.update {
                    it.copy(
                        loadStatus = LoadStatus.Finish,
                        hotKeys = hotKey.await(),
                        hotWebs = hotWeb.await()
                    )
                }
                println("getPopularInfo error: Finish")
            } catch (e: Exception) {
                println("getPopularInfo error: ${e.message}")
            }
        }
    }
}
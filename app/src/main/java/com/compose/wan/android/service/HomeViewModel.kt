package com.compose.wan.android.service

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.wan.android.constant.LoadStatus
import com.compose.wan.android.model.response.HomeArticleListResp
import com.compose.wan.android.net.WanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeState(
    val homeArticles: HomeArticleListResp? = null,
    val isLoading: Boolean = false,
)

@HiltViewModel
class HomeViewModel  @Inject constructor(
    private val wanRepository: WanRepository
) : ViewModel() {

    private val loadStatus = MutableStateFlow(LoadStatus.Loading)
    private val homeArticles = MutableStateFlow(HomeArticleListResp())

    private val _uiState = MutableStateFlow(HomeState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(loadStatus, homeArticles) { loadStatus, homeArticles ->
                HomeState(
                    homeArticles,
                    loadStatus == LoadStatus.Loading,
                )
            }.collect { homeState ->
                _uiState.update {
                    it.copy(
                        homeArticles = homeState.homeArticles,
                        isLoading = homeState.isLoading,
                    )
                }
            }
        }
    }

    fun getHomeArticles(page: Int) {
        viewModelScope.launch {
            val homeArticle = wanRepository.getHomeArticles(page)
            homeArticles.value = homeArticle
        }
    }
}
package com.compose.wan.android.service

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.wan.android.constant.LoadStatus
import com.compose.wan.android.model.response.HomeArticleListResp
import com.compose.wan.android.model.response.SingleHomeBanner
import com.compose.wan.android.net.WanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeState(
    val homeArticles: HomeArticleListResp? = null,
    val homeBanners: List<SingleHomeBanner>? = null,
    val loadStatus: LoadStatus = LoadStatus.Loading,
)

@HiltViewModel
class HomeViewModel  @Inject constructor(
    private val wanRepository: WanRepository
) : ViewModel() {

    private val status = MutableStateFlow(LoadStatus.Loading)
    private val homeArticles = MutableStateFlow(HomeArticleListResp())
    private val banners = MutableStateFlow(listOf<SingleHomeBanner>())

    private val _uiState = MutableStateFlow(HomeState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(status, homeArticles, banners) { status, homeArticles, banners ->
                HomeState(
                    homeArticles,
                    banners,
                    status,

                )
            }.collect { homeState ->
                _uiState.update {
                    it.copy(
                        homeArticles = homeState.homeArticles,
                        homeBanners = homeState.homeBanners,
                        loadStatus = homeState.loadStatus,
                    )
                }
            }
        }
    }

    fun getHomeInfo(page: Int) {
        viewModelScope.launch {
            val homeArticle = async { wanRepository.getHomeArticles(page) }
            val homeBanner = async { wanRepository.getHomeBanners() }
            homeArticles.value = homeArticle.await()
            banners.value = homeBanner.await()
            status.value = LoadStatus.Finish
        }
    }
}
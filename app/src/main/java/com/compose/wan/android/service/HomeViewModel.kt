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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeState(
    val loadStatus: LoadStatus = LoadStatus.Loading,
    val isRefresh: Boolean = false,
    val currentPage: Int = 0,
    val isLoadingMore: Boolean = false,
    val homeArticles: HomeArticleListResp? = null,
    val homeBanners: List<SingleHomeBanner>? = null,
)

@HiltViewModel
class HomeViewModel  @Inject constructor(
    private val wanRepository: WanRepository
) : ViewModel() {

    private val status = MutableStateFlow(LoadStatus.Loading)
    private val isRefresh = MutableStateFlow(false)
    private val currentPage = MutableStateFlow(0)
    private val isLoadingMore = MutableStateFlow(false)
    private val homeArticles = MutableStateFlow(HomeArticleListResp())
    private val banners = MutableStateFlow(listOf<SingleHomeBanner>())

    private val _uiState = MutableStateFlow(HomeState())
    val uiState = _uiState.asStateFlow()

    init {
        val combineFirst = combine(status, isRefresh, homeArticles, banners) { status, refresh, homeArticles, banners ->
            HomeState(
                loadStatus = status,
                isRefresh = refresh,
                homeArticles = homeArticles,
                homeBanners = banners,
            )
        }

        val combineSecond = combine(currentPage, isLoadingMore) { currentPage, isLoadingMore ->
            HomeState(
                currentPage = currentPage,
                isLoadingMore = isLoadingMore
            )
        }
        viewModelScope.launch {
            combine(combineFirst, combineSecond) { combineFirst, combineSecond ->
                HomeState(
                    loadStatus = combineFirst.loadStatus,
                    isRefresh = combineFirst.isRefresh,
                    homeArticles = combineFirst.homeArticles,
                    homeBanners = combineFirst.homeBanners,
                    currentPage = combineSecond.currentPage,
                    isLoadingMore = combineSecond.isLoadingMore,
                )
            }.catch {  }.collect { homeState ->
                _uiState.update {
                    it.copy(
                        loadStatus = homeState.loadStatus,
                        isRefresh = homeState.isRefresh,
                        currentPage = homeState.currentPage,
                        isLoadingMore = homeState.isLoadingMore,
                        homeArticles = homeState.homeArticles,
                        homeBanners = homeState.homeBanners,
                    )
                }
            }
        }
    }

    fun getHomeInfo() {
        viewModelScope.launch {
            val homeArticle = async { wanRepository.getHomeArticles(uiState.value.currentPage) }
            val homeBanner = async { wanRepository.getHomeBanners() }
            homeArticles.value = homeArticle.await()
            banners.value = homeBanner.await()
            status.value = LoadStatus.Finish
        }
    }

    fun loadMore() {
        if (isLoadingMore.value) return
        viewModelScope.launch {
            isLoadingMore.value = true
            currentPage.value++
            val page = currentPage.value
            val result = wanRepository.getHomeArticles(page)
            _uiState.update {
                it.copy(
                    homeArticles = it.homeArticles?.copy(datas = it.homeArticles.datas + result.datas),
                    currentPage = page,
                    isLoadingMore = false
                )
            }
        }
    }
}
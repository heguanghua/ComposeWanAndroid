package com.compose.wan.android.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.compose.wan.android.R
import com.compose.wan.android.constant.LoadStatus
import com.compose.wan.android.service.HomeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.getHomeInfo(0)
    }
    when (uiState.loadStatus) {
        LoadStatus.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    color = Color(0xff1296db)
                )
            }
        }

        LoadStatus.Error -> {
            Text("error")
        }

        LoadStatus.Finish -> {
            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                val pagerState = rememberPagerState(
                    initialPage = 0,
                    pageCount = { uiState.homeBanners?.size ?: 0 })
                val isDragged by pagerState.interactionSource.collectIsDraggedAsState()
                if (isDragged.not()) {
                    with(pagerState) {
                        var currentPageKey by remember { mutableIntStateOf(0) }
                        LaunchedEffect(key1 = currentPageKey) {
                            launch {
                                delay(timeMillis = 3000)
                                val nextPage = (currentPage + 1).mod(pageCount)
                                animateScrollToPage(page = nextPage)
                                if (currentPageKey < Int.MAX_VALUE) {
                                    currentPageKey++
                                } else {
                                    currentPageKey = 0
                                }
                            }
                        }
                    }
                }
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        val item = uiState.homeBanners!![it]
                        NetworkImage(
                            url = item.imagePath
                        )
                    }
                }
                uiState.homeArticles?.datas?.forEach {
                    Column(
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(bottom = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.icon_cat),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(RoundedCornerShape(50))
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "${it.author.let { author -> author?.ifEmpty { it.shareUser?.ifEmpty { it.chapterName } } }}",
                                style = TextStyle(fontSize = 14.sp)
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(it.niceDate)
                        }
                        Text(
                            it.title,
                            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        )
                        Row(
                            modifier = Modifier.padding(top = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                it.chapterName,
                                style = TextStyle(fontSize = 14.sp, color = Color(0xFF1296db))
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Image(
                                painter = painterResource(id = if (it.isCollect) R.drawable.icon_collected else R.drawable.icon_not_collect),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(30.dp)
                                    .clickable {

                                    })
                        }
                    }
                }
            }
        }

        LoadStatus.Empty -> {
            Text("empty")
        }
    }
}

@Composable
fun NetworkImage(url: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val imageRequest = ImageRequest.Builder(context)
        .data(url)
        .crossfade(true)
        .diskCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .networkCachePolicy(CachePolicy.ENABLED)
        .diskCacheKey(url.substringBefore("?"))
        .memoryCacheKey(url.substringBefore("?"))
        .build()
    Image(
        painter = rememberAsyncImagePainter(
            model = imageRequest,
            error = painterResource(R.drawable.icon_loading_error), // 错误占位图
            placeholder = painterResource(R.drawable.icon_loading_image) // 加载中占位图
        ),
        contentDescription = "网络图片", // 无障碍描述
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f) // ✅ 固定16:9比例
            .clip(RoundedCornerShape(8.dp)),
        contentScale = ContentScale.Crop
    )
}
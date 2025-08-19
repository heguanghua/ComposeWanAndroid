package com.compose.wan.android.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.compose.wan.android.R
import com.compose.wan.android.constant.LoadStatus
import com.compose.wan.android.service.HomeViewModel

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
            Column(modifier = Modifier.padding(horizontal = 8.dp).verticalScroll(rememberScrollState()),) {
                HorizontalMultiBrowseCarousel(
                    state = rememberCarouselState { uiState.homeBanners?.count() ?: 0 },
                    modifier = Modifier.fillMaxWidth(),
                    preferredItemWidth = 186.dp,
                ) { i ->
                    val item = uiState.homeBanners!![i]
                    NetworkImage(
                        url = item.imagePath)
                }
                uiState.homeArticles?.datas?.forEach {
                    Column(modifier = Modifier.padding(bottom = 8.dp).clip(RoundedCornerShape(8.dp)).border(1.dp, Color.Gray, RoundedCornerShape(8.dp)).padding(8.dp)) {
                       Row(modifier = Modifier.padding(bottom = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                           Image(painter = painterResource(id = R.drawable.icon_cat), contentDescription = "", modifier = Modifier.size(30.dp).clip(RoundedCornerShape(50)))
                           Spacer(modifier = Modifier.width(4.dp))
                           Text("${it.author.let { author -> author?.ifEmpty { it.shareUser?.ifEmpty { it.chapterName } } }}", style = TextStyle(fontSize = 14.sp))
                           Spacer(modifier = Modifier.weight(1f))
                           Text(it.niceDate)
                       }
                        Text(it.title, style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp))
                        Row(modifier = Modifier.padding(top = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(it.chapterName, style = TextStyle(fontSize = 14.sp, color = Color(0xFF1296db)))
                            Spacer(modifier = Modifier.weight(1f))
                            Image(painter = painterResource(id = if (it.isCollect) R.drawable.icon_collected else R.drawable.icon_not_collect), contentDescription = "", modifier = Modifier.size(30.dp).clickable {

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
    Image(
        painter = rememberAsyncImagePainter(
            model = url,
            error = painterResource(R.drawable.icon_loading_error), // 错误占位图
            placeholder = painterResource(R.drawable.icon_loading_image) // 加载中占位图
        ),
        contentDescription = "网络图片", // 无障碍描述
        modifier = modifier.fillMaxWidth(),
        contentScale = ContentScale.Crop // 图片缩放模式
    )
}
package com.compose.wan.android.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.compose.wan.android.R
import com.compose.wan.android.constant.LoadStatus
import com.compose.wan.android.service.PopularViewModel

@Composable
fun PopularScreen(viewModel: PopularViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.getPopularInfo()
    }
    println("uiState.loadStatus: ${uiState.loadStatus}")
    when(uiState.loadStatus) {
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
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Box(Modifier.fillMaxWidth().height(1.dp).background(Color.Gray))
                // 搜索标题行
                Row(
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Hot Keys")
                    Spacer(Modifier.weight(1f))
                    Image(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "",
                        modifier = Modifier.size(30.dp)
                    )
                }
                Box(Modifier.fillMaxWidth().height(1.dp).background(Color.Gray))
                // 使用常规 FlowRow 替代 LazyVerticalGrid
                FlowRow(
                    modifier = Modifier.padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    maxItemsInEachRow = 4
                ) {
                    uiState.hotKeys?.forEach { item ->
                        Box(
                            modifier = Modifier
                                .width((LocalConfiguration.current.screenWidthDp.dp - 32.dp) / 4) // 计算宽度
                                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                                .aspectRatio(2f)
                                .padding(2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = item.name,
                                fontSize = 12.sp,
                                lineHeight = 14.sp,
                                textAlign = TextAlign.Center,
                                maxLines = 2,
                            )
                        }
                    }
                }

                Box(Modifier.fillMaxWidth().height(1.dp).background(Color.Gray))
                // 搜索标题行
                Row(
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Hot Webs")
                }
                Box(Modifier.fillMaxWidth().height(1.dp).background(Color.Gray))

                // 第二个网格
                FlowRow(
                    modifier = Modifier.padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    maxItemsInEachRow = 4
                ) {
                    uiState.hotWebs?.forEach { item ->
                        Box(
                            modifier = Modifier
                                .width((LocalConfiguration.current.screenWidthDp.dp - 32.dp) / 4) // 计算宽度
                                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                                .aspectRatio(2f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = item.name,
                                fontSize = 12.sp,
                                lineHeight = 14.sp,
                                textAlign = TextAlign.Center,
                                maxLines = 2,
                            )
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
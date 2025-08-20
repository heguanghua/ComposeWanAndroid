package com.compose.wan.android.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.compose.wan.android.constant.Routes
import com.compose.wan.android.R

@Composable
fun MainAppFrame() {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val bottomSelectedState = remember {
        mutableIntStateOf(0)
    }
    Scaffold(
        bottomBar = {
            val currentDestination = navController.currentBackStackEntryAsState().value?.destination
            // 仅在主界面显示底部栏
            if (currentRoute in listOf(Routes.MAIN_HOME, Routes.MAIN_NEWS, Routes.MAIN_STRUCTURE, Routes.MAIN_PROFILE)) {
                NavigationBar(
                    containerColor = Color.White,
                    modifier = Modifier,
                    windowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)
                ) {
                    listOf(Routes.MAIN_HOME, Routes.MAIN_NEWS, Routes.MAIN_STRUCTURE, Routes.MAIN_PROFILE).forEachIndexed { index, item ->
                        val curRoute = item
                        val isSelect =
                            currentDestination?.hierarchy?.any { it.route == curRoute } == true
                        NavigationBarItem(
                            modifier = Modifier,
                            selected = isSelect,
                            icon = {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Image(
                                        modifier = Modifier.size(30.dp),
                                        painter = painterResource(id = whichIcon(item, isSelect)),
                                        contentDescription = ""
                                    )
                                    Text(
                                        text = curRoute,
                                        fontSize = 12.sp,
                                        color = if (bottomSelectedState.intValue == index) Color(0xFF1296db) else Color.Gray,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Color.White,
                            ),
                            onClick = {
                                bottomSelectedState.intValue = index
                                navController.navigate(curRoute) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination when
                                    // reselecting the same item
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (true) Routes.MAIN_GRAPH else Routes.AUTH_GRAPH,
            Modifier.padding(innerPadding)
        ) {
            navigation(
                startDestination = Routes.LOGIN,
                route = Routes.AUTH_GRAPH
            ) {
                composable(Routes.LOGIN) { LoginScreen(
//                    onLoginSuccess = {
//                    navController.navigate(Routes.MAIN_GRAPH) {
//                        popUpTo(Routes.AUTH_GRAPH) { inclusive = true } // 清除回退栈
//                    }
//                }
                )}
                composable(Routes.REGISTER) { RegisterScreen() }
            }

            navigation(
                startDestination = Routes.MAIN_HOME,
                route = Routes.MAIN_GRAPH
            ) {
                composable(Routes.MAIN_HOME) { HomeScreen() }
                composable(Routes.MAIN_NEWS) { NewsScreen() }
                composable(Routes.MAIN_STRUCTURE) { StructScreen() }
                composable(Routes.MAIN_PROFILE) { ProfileScreen(navController) }
            }
        }
    }
}

private fun whichIcon(screen: String, isSelect: Boolean): Int {
    return when (screen) {
        Routes.MAIN_HOME -> {
            if (isSelect) R.drawable.ic_bottom_home_selected else R.drawable.ic_bottom_home_normal
        }

        Routes.MAIN_NEWS -> {
            if (isSelect) R.drawable.ic_bottom_news_selected else R.drawable.ic_bottom_news_normal
        }

        Routes.MAIN_STRUCTURE -> {
            if (isSelect) R.drawable.ic_bottom_structure_selected else R.drawable.ic_bottom_structure_normal
        }

        else -> {
            if (isSelect) R.drawable.ic_bottom_profile_selected else R.drawable.ic_bottom_profile_normal
        }
    }
}
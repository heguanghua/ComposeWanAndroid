package com.compose.wan.android.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.compose.wan.android.R
import com.compose.wan.android.SpHelper
import com.compose.wan.android.constant.Routes
import com.compose.wan.android.constant.SpKey
import com.compose.wan.android.widget.NormalButton

@Composable
fun ProfileScreen(navController: NavController,) {
    Column {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xff1296db))
                .padding(vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                contentDescription = "",
                painter = painterResource(R.drawable.ic_bottom_profile_selected),
                modifier = Modifier.padding(all = 10.dp).clip(RoundedCornerShape(50)).background(Color.White)
            )
            Spacer(Modifier.height(8.dp))
            Text(SpHelper.getString(SpKey.USER_NAME) ?: "not login", style = TextStyle(color = Color.White))
        }
        Spacer(Modifier.height(20.dp))

        NormalButton(
            content = "logout",
            click = {
            SpHelper.clearUserInfo()
            navController.navigate(Routes.LOGIN)
        })
    }
}
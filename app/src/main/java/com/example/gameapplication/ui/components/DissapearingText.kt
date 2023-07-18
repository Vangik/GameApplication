package com.example.gameapplication.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun DisappearingText() {
    var visible by remember { mutableStateOf(true) }

    LaunchedEffect(visible) {
        delay(6000)
        visible = false
    }

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 500)
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Text(
            text = "Wait till the icon disappear",
            color = Color.White,
            fontSize = 24.sp,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight(600),
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .alpha(alpha)
        )
    }
}

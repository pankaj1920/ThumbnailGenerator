package com.app.videoeditorlibrary.ui.customview


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay


@Composable
fun BlueButton(
    text: String,
    modifier: Modifier = Modifier,
    enable: Boolean = true,
    delayMillis: Long = 1000L,
    onClick: () -> Unit,
) {
    var isClickable by remember { mutableStateOf(true) }
    LaunchedEffect(isClickable) {
        if (!isClickable) {
            delay(delayMillis)
            isClickable = true
        }
    }
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        enabled = enable,
        onClick = {
            if (isClickable) {
                isClickable = false
                onClick.invoke()
            }
        }
    ) {

        Text(text = text, style = TextStyle(color = Color.White))
    }
}


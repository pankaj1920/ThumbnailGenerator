package com.app.videoeditorlibrary.ui.screen.trimmer

import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.app.gallerypicker.compose.video.ComposeMultipleVideoPicker
import com.app.gallerypicker.compose.video.pickMultipleVideo
import com.app.videoutils.utils.VideoPlayer
import com.app.videoeditorlibrary.ui.customview.BlueButton
import com.app.videoeditorlibrary.ui.customview.SpaceHeight
import com.app.videoeditorlibrary.utils.Print
import com.app.videotrimmer.videoTrimmerUtils
import com.app.videoutils.utils.getVideoDuration

@RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
@Composable
fun VideoTrimmerScreen(navController: NavHostController) {
    var videDuration by remember { mutableStateOf(0f) }
    val videoUriList = remember { mutableStateListOf<Uri>() }
    var sliderPosition by remember { mutableStateOf(0f..0f) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    videoTrimmerUtils()
    ComposeMultipleVideoPicker(
        onMultipleVideoSelected = { uri ->
            videoUriList.addAll(uri!!)
            getVideoDuration(context, uri[0]) { duration ->
                Print.log("Video Duration: ${duration / 1000} seconds") // Duration is in milliseconds
                val durationInSeconds = (duration / 1000).toFloat()
                videDuration = durationInSeconds
                sliderPosition = 0f..durationInSeconds
            }
        }, onVideoError = { error, message ->
            Print.log("onMultipleVideoSelected $error")
        })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column {
            if (videoUriList.isNotEmpty()) {
                SpaceHeight(10.dp)
                Text(
                    text = "Original Video",
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
                SpaceHeight(10.dp)
                VideoPlayer(videoUri = videoUriList[0])

                SpaceHeight(20.dp)

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    RangeSlider(
                        value = sliderPosition,
                        onValueChange = { range -> sliderPosition = range },
                        valueRange = 0f..videDuration,
                        onValueChangeFinished = {
                            // launch some business logic update with the state you hold
                            // viewModel.updateSelectedSliderValue(sliderPosition)
                        },
                    )
                    SpaceHeight(5.dp)
                    Text("Start Point : ${sliderPosition.start} End Point : ${sliderPosition.endInclusive}")
                    SpaceHeight(20.dp)
                    BlueButton(text = "Video Trimmer", onClick = {
                        pickMultipleVideo()
                    })
                    SpaceHeight(20.dp)

                }

            }
        }
        Column {
            SpaceHeight(20.dp)
            BlueButton(text = "Pick Video", onClick = {
                pickMultipleVideo()
            })
            SpaceHeight(20.dp)
        }

    }


}


@Composable
private fun VideoThumbNail(generatedThumbNail: Bitmap?, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .clickable { onClick() },
        elevation = CardDefaults.elevatedCardElevation(10.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        generatedThumbNail?.let {
            Image(
                modifier = Modifier.size(100.dp),
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
        }

    }
}
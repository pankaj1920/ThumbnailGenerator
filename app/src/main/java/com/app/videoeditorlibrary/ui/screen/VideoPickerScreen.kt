package com.app.videoeditorlibrary.ui.screen

import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.app.gallerypicker.compose.video.ComposeMultipleVideoPicker
import com.app.gallerypicker.compose.video.ComposeSingleVideoPicker
import com.app.gallerypicker.compose.video.pickMultipleVideo
import com.app.gallerypicker.compose.video.pickSingleVideo
import com.app.thumbnailgenerator.utils.getVideoThumbnail
import com.app.thumbnailgenerator.utils.multiVideoThumbnailFlow
import com.app.videoeditorlibrary.ui.customview.BlueButton
import com.app.videoeditorlibrary.ui.customview.SpaceWidth
import com.app.videoeditorlibrary.utils.Print
import kotlinx.coroutines.launch

@RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
@Composable
fun VideoPickerScreen(navController: NavHostController) {
    var generatedThumbNail by remember { mutableStateOf<Bitmap?>(null) }
    var multipleVideoThumbNailList = remember { mutableStateListOf<Bitmap>() }


    val context = LocalContext.current
    val videoUri by remember { mutableStateOf<Uri?>(null) }
    var singleVideo by remember { mutableStateOf(false) }
    var multipleVideo by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    ComposeSingleVideoPicker(onSingleVideoSelected = { uri ->
        coroutineScope.launch {
            generatedThumbNail = getVideoThumbnail(context, uri!!)
            singleVideo = true
            multipleVideo = false
        }
    }, onSingleVideoError = { error, message ->
        Print.log("onSingleVideoSelected $error")
    })

    ComposeMultipleVideoPicker(
        onMultipleVideoSelected = { uri ->
            multiVideoThumbnailFlow(context, uri!!, coroutineScope) { thumbnail ->
                multipleVideoThumbNailList.add(thumbnail)
                singleVideo = false
                multipleVideo = true
            }
            /*     coroutineScope.launch {
                     multipleVideoThumbNailList = getMultiVideoThumbnail(context, uri!!)
                     singleVideo = false
                     multipleVideo = true
                 }*/
        }, onVideoError = { error, message ->
            Print.log("onMultipleVideoSelected $error")
        })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {

            if (singleVideo) {
                VideoThumbNail(generatedThumbNail)
            } else {
                LazyVerticalGrid(
                    modifier = Modifier.fillMaxSize(), columns = GridCells.Fixed(2)
                ) {
                    itemsIndexed(multipleVideoThumbNailList) { index, item ->
                        VideoThumbNail(item)
                    }
                }
            }
        }


        Row {
            Row(modifier = Modifier.weight(1f)) {
                BlueButton(text = "Single Video", onClick = {
                    pickSingleVideo()
                })

            }
            SpaceWidth(width = 20.dp)
            Row(modifier = Modifier.weight(1f)) {
                BlueButton(text = "Multiple Video", onClick = {
                    pickMultipleVideo()
                })
            }


        }

    }
}

@Composable
private fun VideoThumbNail(generatedThumbNail: Bitmap?) {
    Card(
        modifier = Modifier.padding(16.dp),
        elevation = CardDefaults.elevatedCardElevation(10.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        generatedThumbNail?.let {
            Image(
                modifier = Modifier.height(200.dp),
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
        }

    }
}
package com.app.videoeditorlibrary.ui.screen.thumbnail

import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.app.gallerypicker.compose.video.pickMultipleVideo
import com.app.thumbnailgenerator.utils.multiVideoThumbnailFlow
import com.app.thumbnailgenerator.utils.videoThumbnailListFlow
import com.app.videoeditorlibrary.ui.customview.BlueButton
import com.app.videoeditorlibrary.ui.customview.SpaceHeight
import com.app.videoeditorlibrary.utils.Print
import kotlinx.coroutines.launch

@RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
@Composable
fun ThumbnailGeneratorScreen(navController: NavHostController) {
    val generatedThumbNail by remember { mutableStateOf<Bitmap?>(null) }
    val videoThumbNailList = remember { mutableStateListOf<Bitmap>() }
    val thumbNailList = remember { mutableStateListOf<Bitmap>() }
    val videoUriList = remember { mutableStateListOf<Uri>() }


    val context = LocalContext.current
    var singleVideo by remember { mutableStateOf(false) }
    var multipleVideo by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()


    ComposeMultipleVideoPicker(
        onMultipleVideoSelected = { uri ->
            videoUriList.addAll(uri!!)
            multiVideoThumbnailFlow(context, uri!!, coroutineScope) { thumbnail ->
                videoThumbNailList.add(thumbnail)
                singleVideo = false
                multipleVideo = true
            }
        }, onVideoError = { error, message ->
            Print.log("onMultipleVideoSelected $error")
        })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {

            LazyVerticalGrid(
                modifier = Modifier.weight(1f), columns = GridCells.Fixed(2)
            ) {
                itemsIndexed(thumbNailList) { index, item ->
                    VideoThumbNail(item)
                }
            }
            SpaceHeight(40.dp)
            LazyRow(modifier = Modifier.fillMaxWidth()) {
                itemsIndexed(videoThumbNailList) { index, item ->
                    VideoThumbNail(item, onClick = {
                        thumbNailList.clear()
                        coroutineScope.launch {
                            videoThumbnailListFlow(
                                videoUri = videoUriList[index],
                                context = context
                            ) { thumbnail ->

                                thumbNailList.add(thumbnail)
                            }
                        }

                    })
                }
            }
        }


        BlueButton(text = "Thumbnail Multiple Video", onClick = {
            pickMultipleVideo()
        })

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
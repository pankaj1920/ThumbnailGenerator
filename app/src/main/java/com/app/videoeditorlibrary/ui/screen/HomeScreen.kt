package com.app.videoeditorlibrary.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.app.videoeditorlibrary.constant.VideoEditorLocalData
import com.app.videoeditorlibrary.navigation.routes.VideoLibRoutes
import com.app.videoeditorlibrary.utils.Print

@Composable
fun HomeScreen(navController: NavHostController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            itemsIndexed(
                VideoEditorLocalData.getVideoEditorItemList(),
                key = { _, item -> item.id }) { _, item ->

                Card(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            navigateTo(navController, item.id)
                        },
                    elevation = CardDefaults.elevatedCardElevation(10.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {

                    Text(text = "${item.title}", modifier = Modifier.padding(10.dp))
                }
            }
        }
    }

}

fun navigateTo(navController: NavHostController, id: Int) {
    Print.log("navigateTo => $id")
    when (id) {
        1 -> navController.navigate(VideoLibRoutes.VideoPickerScreen)
        2 -> navController.navigate(VideoLibRoutes.VideoPickerScreen)
        3 -> navController.navigate(VideoLibRoutes.VideoPickerScreen)
        4 -> navController.navigate(VideoLibRoutes.VideoThumbnailScreen)
        5 -> navController.navigate(VideoLibRoutes.VideoTrimmerScreen)
    }
}

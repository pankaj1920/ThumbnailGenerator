package com.app.gallerypicker.compose.video

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable


private var pickSingleVideo: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>? = null

@Composable
fun ComposeSingleVideoPicker(
    onSingleVideoSelected: (Uri?) -> Unit,
    onSingleVideoError: (String,String) -> Unit,
) {
    pickSingleVideo =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                onSingleVideoSelected(uri)
            }else{
                onSingleVideoError("error","No Video Selected")
            }
        }

}

fun pickSingleVideo() {
    pickSingleVideo?.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly))
}
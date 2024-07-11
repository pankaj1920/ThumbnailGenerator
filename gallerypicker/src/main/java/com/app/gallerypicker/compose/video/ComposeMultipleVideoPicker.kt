package com.app.gallerypicker.compose.video

import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickMultipleVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.Composable

private var pickMultipleVideo: ManagedActivityResultLauncher<PickVisualMediaRequest, List<Uri>>? =
    null

@RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
@Composable
fun ComposeMultipleVideoPicker(
    maxVideoCount: Int = MediaStore.getPickImagesMaxLimit(),
    onMultipleVideoSelected: (List<Uri>?) -> Unit,
    onVideoError: (String, String) -> Unit,
) {
    pickMultipleVideo =
        rememberLauncherForActivityResult(PickMultipleVisualMedia(maxVideoCount)) { uris ->
            if (uris.isNotEmpty()) {
                onMultipleVideoSelected(uris)
            } else {
                onVideoError("error", "No Video Selected")
            }
        }
}

fun pickMultipleVideo() {
    pickMultipleVideo?.launch(PickVisualMediaRequest(PickVisualMedia.VideoOnly))
}

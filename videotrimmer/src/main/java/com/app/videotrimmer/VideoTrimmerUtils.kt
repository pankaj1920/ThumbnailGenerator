package com.app.videotrimmer

import android.net.Uri
import android.service.autofill.UserData

fun trimVideo(
    startPosition: Float,
    endPosition: Float,
    uri: Uri,
    onVideoTrimProgress: (progress: Long) -> Unit,
    onVideoTrimSuccess: (outputVideoUri: Uri) -> Unit
) {

}


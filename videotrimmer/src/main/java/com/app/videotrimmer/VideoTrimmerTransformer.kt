package com.app.videotrimmer

import android.net.Uri
import android.os.Bundle
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaItem.ClippingConfiguration

private fun createMediaItem(trimStartMs: Long, trimEndMs: Long, uri: Uri,): MediaItem {
    val mediaItemBuilder = MediaItem.Builder().setUri(uri)
            mediaItemBuilder.setClippingConfiguration(
                ClippingConfiguration.Builder()
                    .setStartPositionMs(trimEndMs)
                    .setEndPositionMs(trimEndMs)
                    .build()
            )

    return mediaItemBuilder.build()
}
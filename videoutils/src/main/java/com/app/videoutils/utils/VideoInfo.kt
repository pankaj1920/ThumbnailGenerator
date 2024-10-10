package com.app.videoutils.utils

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer

fun getVideoDuration(context: Context, uri: Uri, onDurationRetrieved: (Long) -> Unit) {
    val exoPlayer = ExoPlayer.Builder(context).build()
    exoPlayer.apply {
        setMediaItem(MediaItem.fromUri(uri))
        prepare()
        addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    val duration = exoPlayer.duration
                    onDurationRetrieved(duration)
                    exoPlayer.release() // Release the player after retrieving the duration
                }
            }
        })
    }
}
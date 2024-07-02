package android.generate.thumbnail

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext


fun generateThumbnail(
    uri: Uri,
    context: Context,
    qualityDimension: Int,
    thumbnailCount: Int
): ArrayList<Bitmap> {
    val metaDataSource = MediaMetadataRetriever()
    metaDataSource.setDataSource(context, uri)

    val videoLength = (metaDataSource.extractMetadata(
        MediaMetadataRetriever.METADATA_KEY_DURATION
    )!!.toInt() * 1000).toLong()

    val thumbnailArray = arrayListOf<Bitmap>()

    val interval = videoLength / thumbnailCount

    for (i in 0 until thumbnailCount) {
        val frameTime = i * interval
        var bitmap =
            metaDataSource.getFrameAtTime(frameTime, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)!!
        try {
            val targetWidth: Int
            val targetHeight: Int
            if (bitmap.height > bitmap.width) {
                targetHeight = qualityDimension
                val percentage = qualityDimension.toFloat() / bitmap.height
                targetWidth = (bitmap.width * percentage).toInt()
            } else {
                targetWidth = qualityDimension
                val percentage = qualityDimension.toFloat() / bitmap.width
                targetHeight = (bitmap.height * percentage).toInt()
            }
            bitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, false)
            thumbnailArray.add(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }
    metaDataSource.release()
    return thumbnailArray
}
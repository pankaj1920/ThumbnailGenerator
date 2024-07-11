package com.app.thumbnailgenerator.utils

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

suspend fun getVideoThumbnail(
    context: Context,
    videoUri: Uri,
    thumbnailTimeSec: Long = 5, // Time in seconds
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
): Bitmap? {
    return withContext(dispatcher) {
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(context, videoUri)
            val thumbnailTimeUs = thumbnailTimeSec * 1_000_000 // Convert seconds to microseconds
            retriever.getFrameAtTime(
                thumbnailTimeUs,
                MediaMetadataRetriever.OPTION_CLOSEST_SYNC
            ) // Retrieve the frame at the specified time
        } catch (ex: Exception) {
            null
        } finally {
            retriever.release()
        }
    }
}

suspend fun getMultiVideoThumbnail(
    context: Context,
    videoListUri: List<Uri>,
    thumbnailTimeSec: Long = 5, // Time in seconds
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
): ArrayList<Bitmap> {
    val thumbnailArray = arrayListOf<Bitmap>()
    withContext(dispatcher) {
        try {
            for (videoUri in videoListUri) {
                getVideoThumbnail(context, videoUri, thumbnailTimeSec)?.let {
                    thumbnailArray.add(it)
                }
            }
        } catch (ex: Exception) {
            Log.d("getMultiVideoThumbnail Error ", ex.message.toString())
        }
    }
    return thumbnailArray
}


fun getMultiVideoThumbnailFlow(
    context: Context,
    videoListUri: List<Uri>,
    thumbnailTimeSec: Long = 5, // Time in seconds
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
): Flow<Bitmap> {
    return flow {
        videoListUri.forEach { videoUri ->
            getVideoThumbnail(context, videoUri, thumbnailTimeSec, dispatcher)?.let { bitmap ->
                emit(bitmap)
            }
        }
    }.flowOn(dispatcher)
}

fun multiVideoThumbnailFlow(
    context: Context,
    videoListUri: List<Uri>,
    scope: CoroutineScope,
    onThumbnailGenerated: (Bitmap) -> Unit,
) {
    scope.launch {
        getMultiVideoThumbnailFlow(context, videoListUri)
            .catch { e ->
                Log.d("getMultiVideoThumbnailFlow Error ", e.message.toString())
            }
            .collect { thumbnail ->
                onThumbnailGenerated(thumbnail)
            }
    }
}

suspend fun generateThumbnailList(
    uri: Uri,
    context: Context,
    qualityDimension: Int,
    thumbnailCount: Int,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
): ArrayList<Bitmap> {
    val thumbnailArray = arrayListOf<Bitmap>()
    withContext(dispatcher) {
        val metaDataSource = MediaMetadataRetriever()
        try {
            metaDataSource.setDataSource(context, uri)
            val videoLength = (metaDataSource.extractMetadata(
                MediaMetadataRetriever.METADATA_KEY_DURATION
            )!!.toInt() * 1000).toLong()


            val interval = videoLength / thumbnailCount

            for (i in 0 until thumbnailCount) {
                val frameTime = i * interval
                var bitmap =
                    metaDataSource.getFrameAtTime(
                        frameTime,
                        MediaMetadataRetriever.OPTION_CLOSEST_SYNC
                    )!!
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

        } catch (ex: Exception) {
            null
        } finally {
            metaDataSource.release()
        }
    }
    return thumbnailArray
}


fun CoroutineScope.videoThumbnailListFlow(
    context: Context,
    videoUri: Uri,

    qualityDimension: Int = 500,
    thumbnailCount: Int = 20,
    onThumbnailGenerated: (Bitmap) -> Unit,
) {
    launch {
        generateThumbnailListFlow(videoUri, context, qualityDimension, thumbnailCount)
            .catch { e ->
                Log.d("getMultiVideoThumbnailFlow Error ", e.message.toString())
            }
            .collect { thumbnail ->
                onThumbnailGenerated(thumbnail)
            }
    }
}

fun generateThumbnailListFlow(
    uri: Uri,
    context: Context,
    qualityDimension: Int,
    thumbnailCount: Int,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
): Flow<Bitmap> {
    return flow {
        val metaDataSource = MediaMetadataRetriever()
        try {
            metaDataSource.setDataSource(context, uri)
            val videoLength = (metaDataSource.extractMetadata(
                MediaMetadataRetriever.METADATA_KEY_DURATION
            )!!.toInt() * 1000).toLong()

            val interval = videoLength / thumbnailCount

            for (i in 0 until thumbnailCount) {
                val frameTime = i * interval
                val bitmap =
                    metaDataSource.getFrameAtTime(
                        frameTime,
                        MediaMetadataRetriever.OPTION_CLOSEST_SYNC
                    ) ?: continue

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
                    val scaledBitmap =
                        Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, false)
                    emit(scaledBitmap)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            metaDataSource.release()
        }
    }.flowOn(dispatcher)
}
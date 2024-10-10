package com.app.videoeditorlibrary.navigation.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class VideoLibRoutes {

    @Serializable
    data object  VideoPickerScreen : VideoLibRoutes()

    @Serializable
    data object VideoHomeScreen : VideoLibRoutes()

    @Serializable
    data object VideoThumbnailScreen : VideoLibRoutes()

    @Serializable
    data object VideoTrimmerScreen : VideoLibRoutes()

}
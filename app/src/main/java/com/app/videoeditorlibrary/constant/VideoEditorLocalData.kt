package com.app.videoeditorlibrary.constant

import com.app.videoeditorlibrary.model.VideoLibModel

object VideoEditorLocalData {

    fun getVideoEditorItemList(): ArrayList<VideoLibModel> {
        val videoEditorItemList = ArrayList<VideoLibModel>()
        videoEditorItemList.add(VideoLibModel(1, "Premissions"))
        videoEditorItemList.add(VideoLibModel(2, "Image Picker"))
        videoEditorItemList.add(VideoLibModel(3, "Video Picker"))
        videoEditorItemList.add(VideoLibModel(4, "Thumbnail Generator"))
        videoEditorItemList.add(VideoLibModel(5, "Video Trimmer"))

        return videoEditorItemList
    }
}
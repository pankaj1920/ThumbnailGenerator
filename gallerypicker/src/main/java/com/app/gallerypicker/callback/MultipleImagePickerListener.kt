package com.app.gallerypicker.callback

import android.net.Uri

fun interface MultipleImagePickerListener {
    fun onMultipleImagePick(uriList: MutableList<Uri>?)
}
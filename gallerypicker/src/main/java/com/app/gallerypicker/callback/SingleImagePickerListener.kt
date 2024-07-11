package com.app.gallerypicker.callback

import android.net.Uri

fun interface SingleImagePickerListener {
    fun onSingleImagePick(uri: Uri?)
}
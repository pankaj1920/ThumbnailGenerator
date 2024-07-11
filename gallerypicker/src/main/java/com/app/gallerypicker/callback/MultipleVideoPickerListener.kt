package com.app.gallerypicker.callback

import android.net.Uri

fun interface MultipleVideoPickerListener {
    fun onMultipleVideoPick(uriList: MutableList<Uri>?)
}
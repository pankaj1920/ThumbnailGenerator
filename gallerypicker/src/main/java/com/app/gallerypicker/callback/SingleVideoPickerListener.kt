package com.app.gallerypicker.callback

import android.net.Uri

 interface SingleVideoPickerListener {
    fun onSingleVideoPick(uri: Uri?)
    fun onSingleVideoPickError(type:String,message:String)
}
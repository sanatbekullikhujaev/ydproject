package com.usc0der.ydprojectnew.utils

import android.content.Context
import android.os.Environment
import java.io.File

fun Context.getOutputDirectory(): File {
    val appContext = applicationContext
    val mediaDir = getExternalFilesDir(Environment.DIRECTORY_MOVIES)
    return if (mediaDir != null && mediaDir.exists())
        mediaDir else appContext.filesDir
}
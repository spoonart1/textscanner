package com.spoonart.feature_textscanner.data

import android.net.Uri

data class ResultData(
    val uri: Uri,
    val texts : List<String>,
    val display: PrettyDisplay
)

data class PrettyDisplay(
    val distance:String,
    val duration: String,
    val originalPlace:String
)

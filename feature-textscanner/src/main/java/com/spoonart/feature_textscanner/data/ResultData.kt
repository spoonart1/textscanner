package com.spoonart.feature_textscanner.data

import android.net.Uri
import com.spoonart.feature_textscanner.utils.DistanceUtils

data class ResultData(
    val uri: Uri,
    val texts : List<String>,
    val distance: DistanceUtils.DistanceData
)

package com.spoonart.feature_textscanner.utils


import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import javax.inject.Inject

interface TextAnalyzer {
    fun analyzeImage(
        context: Context,
        fromFile: Uri? = null,
        fromBitmap: Bitmap? = null,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit,
    )
}

class TextAnalyzerImpl @Inject constructor() : TextAnalyzer {

    private val recognizer by lazy {
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    }

    override fun analyzeImage(
        context: Context,
        fromFile: Uri?,
        fromBitmap: Bitmap?,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit,
    ) {
        try {
            val image = getImage(context, fromFile, fromBitmap)
            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    if (visionText.textBlocks.isEmpty()) {
                        onFailure("Please use another image or recapture under the best light")
                    } else {
                        onSuccess(visionText.textBlocks.first().text)
                    }
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                    onFailure(e.localizedMessage ?: "unknown error")
                }
        } catch (e: Exception) {
            e.printStackTrace()
            onFailure(e.localizedMessage ?: "unknown error")
        }
    }

    private fun getImage(
        context: Context,
        fromFile: Uri? = null,
        fromBitmap: Bitmap? = null,
    ): InputImage {
        return if (fromFile != null) {
            InputImage.fromFilePath(context, fromFile)
        } else if (fromBitmap != null) {
            InputImage.fromBitmap(fromBitmap, 0)
        } else {
            throw NullPointerException("at least provide either fromFile or fromBitmap, both cannot be null at the same time!")
        }
    }
}










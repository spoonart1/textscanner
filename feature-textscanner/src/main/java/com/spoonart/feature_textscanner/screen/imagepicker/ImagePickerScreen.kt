package com.spoonart.feature_textscanner.screen.imagepicker

import android.content.Context
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.core.content.FileProvider
import com.spoonart.feature_textscanner.R
import java.io.File

@Composable
fun launchCameraPicker(onPicked: (Boolean) -> Unit): ManagedActivityResultLauncher<Uri, Boolean> {
    return rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        onPicked(it)
    }
}

fun cameraUri(context: Context) = ComposeFileProvider.getImageUri(context)

class ComposeFileProvider : FileProvider(
    R.xml.filepaths
) {
    companion object {
        fun getImageUri(context: Context): Uri {
            val directory = File(context.cacheDir, "images")
            directory.mkdirs()
            val file = File.createTempFile(
                "selected_image_",
                ".jpg",
                directory,
            )
            val authority = context.packageName + ".fileprovider"
            return getUriForFile(
                context,
                authority,
                file,
            )
        }
    }
}

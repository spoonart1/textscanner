package com.spoonart.feature_textscanner.ui

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.spoonart.common_utility.location.LocationComposable
import com.spoonart.common_utility.permission.AskPermission
import com.spoonart.feature_textscanner.screen.imagepicker.ComposeFileProvider
import com.spoonart.feature_textscanner.screen.imagepicker.launchCameraPicker
import com.spoonart.uicomponent.theme.TextScannerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TextScannerActivity : ComponentActivity() {
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TextScannerTheme {
                // A surface container using the 'background' color from the theme
                AskPermission()
                LocationComposable{
                    println("loc: $it")
                }
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background) {
                    Box(contentAlignment = Alignment.Center) {
                        var uri by remember { mutableStateOf<Uri?>(null) }
                        val context = LocalContext.current
                        val launcher = launchCameraPicker { isSuccess ->
                            if (isSuccess && uri != null) {

                            }
                        }

                        TakePicture {
                            uri = ComposeFileProvider.getImageUri(context)
                            launcher.launch(uri)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TakePicture(
    onClick : ()->Unit
){
    Button(onClick = onClick) {
        Text(text = "Open Camera")
    }
}

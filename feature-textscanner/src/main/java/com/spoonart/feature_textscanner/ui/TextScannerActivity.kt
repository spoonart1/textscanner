package com.spoonart.feature_textscanner.ui

import android.annotation.SuppressLint
import android.location.Location
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.spoonart.common_utility.location.LocationComposable
import com.spoonart.common_utility.permission.AskPermission
import com.spoonart.feature_textscanner.screen.imagepicker.ComposeFileProvider
import com.spoonart.feature_textscanner.screen.imagepicker.launchCameraPicker
import com.spoonart.feature_textscanner.screen.resultview.ResultView
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
                val viewModel: TextScannerViewModel by viewModels()
                var currentLocation = remember<Location?> { null }
                AskPermission {
                    LocationComposable {
                        currentLocation = it
                    }
                }
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background) {
                    Box(contentAlignment = Alignment.Center) {
                        var uri by remember { mutableStateOf<Uri?>(null) }
                        val loadingState by viewModel.stateProgress.observeAsState()
                        val context = LocalContext.current
                        val launcher = launchCameraPicker { isSuccess ->
                            if (isSuccess && uri != null && currentLocation != null) {
                                viewModel.scanImage(this@TextScannerActivity,
                                    uri!!,
                                    currentLocation!!)
                            }
                        }

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (loadingState == LoadingState.Nothing || loadingState == LoadingState.Finish) {
                                TakePicture {
                                    uri = ComposeFileProvider.getImageUri(context)
                                    launcher.launch(uri)
                                }
                            }

                            if (loadingState == LoadingState.Loading) {
                                LoadingIndicator()
                            }
                        }

                        val resultData by viewModel.result.observeAsState()
                        val error by viewModel.error.observeAsState()
                        resultData?.let {
                            ResultView(onBack = {
                                viewModel.reset()
                            }, data = it, error = error)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingIndicator() {
    Box(
        modifier = Modifier.padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
    Text(
        modifier = Modifier.padding(8.dp),
        text = "Casting magic for you ..."
    )
}

@Composable
private fun TakePicture(
    onClick: () -> Unit,
) {
    Button(onClick = onClick) {
        Text(text = "Open Camera")
    }
}

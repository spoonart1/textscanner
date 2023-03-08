package com.spoonart.common_utility.permission

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat

@Composable
fun AskPermission(
    onGranted: (@Composable () -> Unit)? = null,
) {
    val permissions = arrayOf(
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    var isAllowed by remember { mutableStateOf(false) }
    AskPermission(permissions = permissions) { isGranted ->
        isAllowed = isGranted
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { granted ->
            isAllowed = !granted.containsValue(false)
        }
    )

    LaunchedEffect(key1 = isAllowed, block = {
        launcher.launch(permissions)
    })

    if (isAllowed) {
        onGranted?.invoke()
    }
}

@Composable
private fun AskPermission(
    permissions: Array<String>,
    onCheckResult: @Composable (Boolean) -> Unit,
) {
    val context = LocalContext.current
    onCheckResult(hasPermissions(context, permissions))
}

private fun hasPermissions(context: Context, permissions: Array<String>): Boolean {
    for (permission in permissions) {
        if (ActivityCompat.checkSelfPermission(context,
                permission) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
    }
    return true
}

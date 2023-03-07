package com.spoonart.feature_textscanner.screen.resultview

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import com.spoonart.feature_textscanner.ui.TextScannerViewModel

@Composable
fun ResultView(
    viewModel: TextScannerViewModel
){

    val resultData by viewModel.result.observeAsState()
    val error by viewModel.error.observeAsState()

    BackHandler{
       viewModel.reset()
    }

    resultData?.let {
        Surface {
            Column {
                it.texts.forEach { text ->
                    Text(text = text.pretty())
                }
            }
        }
    }
    if (!error.isNullOrEmpty()){
        Toast(error!! )
    }
}

@Composable
private fun Toast(message:String){
    val context = LocalContext.current
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

private fun String.pretty() : String{
    return if (this.contains(".")){
        this.replace(".",".\n")
    } else this
}



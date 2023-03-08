package com.spoonart.feature_textscanner.screen.resultview

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.spoonart.feature_textscanner.ui.TextScannerViewModel

@OptIn(ExperimentalUnitApi::class)
@Composable
fun ResultView(
    viewModel: TextScannerViewModel
){

    val resultData by viewModel.result.observeAsState()
    val error by viewModel.error.observeAsState()

    BackHandler{
       viewModel.reset()
    }

    resultData?.let { data ->
        Surface {
            Column {
                data.texts.forEach { text ->
                    Text(text = text.pretty(), fontStyle = FontStyle.Italic)
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text(text = "Distance \t\t\t\t: ${data.display.distance}")

                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Estimated time \t: ${data.display.duration}")

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(0.7f),
                    text = data.display.originalPlace ,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = TextUnit(11f, TextUnitType.Sp)
                )
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



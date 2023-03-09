package com.spoonart.feature_textscanner.screen.resultview

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.spoonart.feature_textscanner.data.ResultData

@OptIn(ExperimentalUnitApi::class)
@Composable
fun ResultView(
    onBack: () -> Unit,
    data: ResultData,
    error: String?,
) {

    BackHandler(onBack = onBack)
    Surface {
        Column(modifier = Modifier.fillMaxWidth(0.8f)) {
            Textable(title = "Scanned Image", value = mergeText(data.texts))

            Spacer(modifier = Modifier.height(24.dp))
            Textable(title = "Distance", value = data.display.distance)

            Spacer(modifier = Modifier.height(8.dp))
            Textable(title = "Estimated time", value = data.display.duration)

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                modifier = Modifier.fillMaxWidth(0.7f),
                text = data.display.originalPlace,
                fontWeight = FontWeight.SemiBold,
                fontSize = TextUnit(11f, TextUnitType.Sp)
            )
        }
    }
    if (!error.isNullOrEmpty()) {
        Toast(error)
    }
}

@Composable
private fun Textable(title: String, value: String) {
    TextField(value = value, onValueChange = {}, label = {
        Text(text = title)
    })
}

@Composable
private fun Toast(message: String) {
    val context = LocalContext.current
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

private fun mergeText(texts:List<String>) : String{
    val sb = StringBuilder()
    for (t in texts){
        sb.append(t.pretty(",").pretty("."))
        sb.append(" ")
    }
    return sb.toString()
}

private fun String.pretty(pattern:String): String {
    return if (this.contains(pattern)) {
        this.replace(pattern, "$pattern ")
    } else this
}



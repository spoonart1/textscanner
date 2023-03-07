package com.spoonart.feature_textscanner.ui

import android.content.Context
import android.location.Location
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.maps.model.LatLng
import com.spoonart.feature_textscanner.data.ResultData
import com.spoonart.feature_textscanner.utils.DistanceUtils
import com.spoonart.feature_textscanner.utils.TextAnalyzer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TextScannerViewModel @Inject constructor(
    private val textAnalyzer: TextAnalyzer,
) : ViewModel() {

    private val _stateProgress = MutableLiveData<LoadingState>(LoadingState.Nothing)
    val stateProgress: LiveData<LoadingState> = _stateProgress

    private val _result = MutableLiveData<ResultData>()
    val result: LiveData<ResultData> = _result

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun scanImage(context: Context, uri: Uri, location: Location) {
        viewModelScope.launch(Dispatchers.IO) {
            _stateProgress.postValue(LoadingState.Loading)
            textAnalyzer.analyzeImage(
                context = context,
                fromFile = uri,
                onSuccess = {
                    calculateDistance(uri, it, location)
                },
                onFailure = {
                    _error.postValue(it)
                }
            )
        }
    }

    fun reset(){
        _result.value = null
    }

    private fun calculateDistance(
        uri: Uri,
        texts: List<String>,
        from: Location,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val origin = LatLng(from.latitude, from.longitude)
            DistanceUtils.request(
                origin = origin,
                onResult = {
                    _stateProgress.postValue(LoadingState.Finish)
                    _result.postValue(
                        ResultData(
                            uri = uri,
                            texts = texts,
                            it
                        )
                    )
                }
            )
        }
    }

}

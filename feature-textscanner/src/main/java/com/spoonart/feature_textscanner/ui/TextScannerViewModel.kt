package com.spoonart.feature_textscanner.ui

import android.content.Context
import android.location.Location
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.maps.model.LatLng
import com.spoonart.feature_textscanner.data.PrettyDisplay
import com.spoonart.feature_textscanner.data.ResultData
import com.spoonart.feature_textscanner.utils.DistanceUtils
import com.spoonart.feature_textscanner.utils.LocationUtils
import com.spoonart.feature_textscanner.utils.TextAnalyzer
import com.spoonart.remote_database.RemoteDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TextScannerViewModel @Inject constructor(
    private val textAnalyzer: TextAnalyzer,
    private val remoteDatabase: RemoteDatabase
) : ViewModel() {

    private val gson = Gson()

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
                    calculateDistance(context, uri, it, location)
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
        context: Context,
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
                    val output = PrettyDisplay(
                        distance = DistanceUtils.prettyDistance(it.distanceInMeters),
                        duration = DistanceUtils.prettyDuration(it.durationInSeconds),
                        originalPlace = LocationUtils.getLocationName(context, from)
                    )
                    _result.postValue(
                        ResultData(
                            uri = uri,
                            texts = texts,
                            display = output
                        )
                    )
                    sendData(output)
                }
            )
        }
    }

    private fun sendData(display: PrettyDisplay){
        viewModelScope.launch(Dispatchers.IO) {
            remoteDatabase.save(gson.toJson(display))
        }
    }

}

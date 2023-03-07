package com.spoonart.feature_textscanner.ui

sealed class LoadingState{
    object Loading : LoadingState()
    object Nothing : LoadingState()
    object Finish : LoadingState()
}

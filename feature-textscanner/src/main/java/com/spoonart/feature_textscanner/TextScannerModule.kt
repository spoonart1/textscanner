package com.spoonart.feature_textscanner

import com.spoonart.feature_textscanner.utils.TextAnalyzer
import com.spoonart.feature_textscanner.utils.TextAnalyzerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal class TextScannerModule {

    @Provides
    fun textAnalyzer() : TextAnalyzer = TextAnalyzerImpl()

}

package com.spoonart.remote_database.di

import android.content.Context
import com.spoonart.remote_database.DatabaseEditor
import com.spoonart.remote_database.RemoteDatabase
import com.spoonart.remote_database.RemoteDatabaseImpl
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@dagger.Module
@InstallIn(SingletonComponent::class)
internal class Module {

    @Provides
    @Singleton
    fun providesEditor(
        @ApplicationContext context: Context
    ) = DatabaseEditor(context)

    @Provides
    @Singleton
    fun provideRemoteDatabase(
        editor: DatabaseEditor
    ) : RemoteDatabase = RemoteDatabaseImpl(editor)
}

package com.spoonart.remote_database

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject


internal class DatabaseEditor @Inject constructor(
    context: Context
) {
    init {
        FirebaseApp.initializeApp(context)
    }

    private fun database() = FirebaseDatabase.getInstance()

    fun dbRef() = database().getReference("/experiment/data")
}

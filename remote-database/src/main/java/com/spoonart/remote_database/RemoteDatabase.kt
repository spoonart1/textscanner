package com.spoonart.remote_database

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

interface RemoteDatabase {
    suspend fun save(data: String)
    suspend fun addValueEventListener(listener: RemoteValueEventListener)
}

interface RemoteValueEventListener {
    fun onDataChanged(snapShotJson: String)
    fun onCancelled(error: Exception)
}

internal class RemoteDatabaseImpl @Inject constructor(
    private val editor: DatabaseEditor,
) : RemoteDatabase {

    override suspend fun save(data: String) {
        editor.dbRef().setValue(data)
    }

    override suspend fun addValueEventListener(listener: RemoteValueEventListener) {
        editor.dbRef().addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue(String::class.java)
                listener.onDataChanged(value ?: "#invalid#")
            }

            override fun onCancelled(error: DatabaseError) {
                listener.onCancelled(error.toException())
            }

        })
    }
}

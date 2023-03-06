package com.spoonart.common_utility.location

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.location.Location
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.spoonart.textscanner.ui_component.R

@Composable
fun LocationComposable(onUpdate:(Location)->Unit){

    val context = LocalContext.current
    val locationLiveData = remember { MutableLiveData<Location>() }
    val serviceConnection = remember{
        object : ServiceConnection{
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val binder = service as PowerfulLocationService.MyBinder
                binder.getService().locationLiveData.observeForever {
                    locationLiveData.postValue(it)
                }
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                locationLiveData.postValue(null)
            }

        }
    }

    DisposableEffect(serviceConnection){
     val intent = Intent(context, PowerfulLocationService::class.java)
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)

        ContextCompat.startForegroundService(context, intent)
        onDispose {
            context.unbindService(serviceConnection)
            context.stopService(intent)
        }
    }

    val location by locationLiveData.observeAsState()
    location?.let { onUpdate(it) }
}

class PowerfulLocationService : LifecycleService(){
    private val _locationLiveData = MutableLiveData<Location>()
    val locationLiveData:LiveData<Location> = _locationLiveData

    private val locationRequest = LocationRequest.create()
        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        .setInterval(5000)

    private lateinit var fusedLocationClient:FusedLocationProviderClient
    private lateinit var callback: LocationCallback

    override fun onCreate() {
        super.onCreate()
        startForeground()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        return MyBinder()
    }

    private fun startForeground(){
        val channelId = "mychannelid"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "My Channel"
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Location Service")
            .setContentText("Running...")
            .setSmallIcon(R.drawable.ic_notif)
            .build()
        startForeground(1234, notification)
    }

    @SuppressLint("MissingPermission")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        callback = MyLocationCallback()
        fusedLocationClient.requestLocationUpdates(locationRequest, callback, Looper.myLooper()!!)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(callback)
    }

    private inner class MyLocationCallback : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            _locationLiveData.value = locationResult.lastLocation
        }
    }

    inner class MyBinder : Binder(){
        fun getService():PowerfulLocationService{
            return this@PowerfulLocationService
        }
    }
}

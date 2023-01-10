package com.example.finalprojectlocation.locationreminder.geofence

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == ACTION_GEOFENCE_EVENT) {
            Log.i("GeofenceReceiver", "Geofence event received")
            GeofenceTransitionsJobIntentService.enqueueWork(context, intent)
        }

    }
}
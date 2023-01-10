package com.example.finalprojectlocation.locationreminder.local

import android.content.Context
import androidx.room.Room

object LocalD {
    fun createRemindersDao(context: Context): RemindersDao {
        return Room.databaseBuilder(
            context.applicationContext,
            RemnderDataBase::class.java, "locationReminders.db"
        ).build().reminderDao()
    }
}
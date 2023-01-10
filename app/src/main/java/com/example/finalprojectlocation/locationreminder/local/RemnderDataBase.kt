package com.example.finalprojectlocation.locationreminder.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.finalprojectlocation.locationreminder.data.dto.ReminderDTO

@Database(entities = [ReminderDTO::class], version = 1, exportSchema = false)
abstract class RemnderDataBase: RoomDatabase() {

    abstract fun reminderDao(): RemindersDao
}
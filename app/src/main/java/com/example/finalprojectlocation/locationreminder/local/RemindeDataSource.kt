package com.example.finalprojectlocation.locationreminder.local

import com.example.finalprojectlocation.locationreminder.data.dto.ReminderDTO

interface RemindeDataSource {
    suspend fun getReminders(): Result<List<ReminderDTO>>
    suspend fun saveReminder(reminder: ReminderDTO)
    suspend fun getReminder(id: String): Result<ReminderDTO>
    suspend fun deleteAllReminders()
}
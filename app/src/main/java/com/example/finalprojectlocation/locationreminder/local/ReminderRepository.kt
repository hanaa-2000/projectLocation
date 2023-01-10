package com.example.finalprojectlocation.locationreminder.local

import com.example.finalprojectlocation.locationreminder.data.dto.ReminderDTO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReminderRepository (private val remindersDao: RemindersDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    ) : RemindeDataSource {

        /**
         * Get the reminders list from the local db
         * @return Result the holds a Success with all the reminders or an Error object with the error message
         */
        override suspend fun getReminders(): Result<List<ReminderDTO>> = withContext(ioDispatcher) {
            wrapEspressoIdlingResource{
                return@withContext try {
                    Result.Success(remindersDao.getReminders())
                } catch (ex: Exception) {
                    Result.Error(ex.localizedMessage)
                }
            }

        }

        /**
         * Insert a reminder in the db.
         * @param reminder the reminder to be inserted
         */
        override suspend fun saveReminder(reminder: ReminderDTO) =
            withContext(ioDispatcher) {
                wrapEspressoIdlingResource {
                    remindersDao.saveReminder(reminder)
                }

            }


        override suspend fun getReminder(id: String): Result<ReminderDTO> = withContext(ioDispatcher) {
            wrapEspressoIdlingResource {
                try {
                    val reminder = remindersDao.getReminderById(id)
                    if (reminder != null) {
                        return@withContext Result.Success(reminder)
                    } else {
                        return@withContext Result.Error("Reminder not found!")
                    }
                } catch (e: Exception) {
                    return@withContext Result.Error(e.localizedMessage)
                }
            }

        }


        override suspend fun deleteAllReminders() {
            wrapEspressoIdlingResource {
                withContext(ioDispatcher) {
                    remindersDao.deleteAllReminders()
                }
            }

        }
    }

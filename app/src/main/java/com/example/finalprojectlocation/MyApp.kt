package com.example.finalprojectlocation

import android.app.Application
import com.example.finalprojectlocation.locationreminder.local.LocalD
import com.example.finalprojectlocation.locationreminder.local.RemindeDataSource
import com.example.finalprojectlocation.locationreminder.local.ReminderRepository
import com.example.finalprojectlocation.locationreminder.reminderlist.RemindersListViewModel
import com.example.finalprojectlocation.locationreminder.savereinder.SaveReminderViewModel

//import org.kotlin.android.ext.koin.androidContext
//import org.koin.androidx.viewmodel.dsl.viewModel
//import org.koin.core.context.startKoin
//import org.koin.dsl.module
import java.lang.reflect.Array.get

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()


        val myModule = module {
            viewModel {
                RemindersListViewModel(
                    get(),
                    get() as RemindeDataSource
                )
            }
            single {
                //This view model is declared singleton to be used across multiple fragments
                SaveReminderViewModel(
                    get(),
                    get() as RemindeDataSource
                )
            }
            single { ReminderRepository(get()) as RemindeDataSource }
            single { LocalD.createRemindersDao(this@MyApp) }
        }

        startKoin {
            androidContext(this@MyApp)
            modules(listOf(myModule))
        }
    }
}
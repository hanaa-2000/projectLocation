package  com.example.finalprojectlocation.locationreminders.reminderslist

import android.app.Application
import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.finalprojectlocation.R
import com.example.finalprojectlocation.locationreminder.ReminderDesciption
import com.example.finalprojectlocation.locationreminder.data.dto.ReminderDTO
import com.example.finalprojectlocation.locationreminder.local.LocalD
import com.example.finalprojectlocation.locationreminder.local.RemindeDataSource
import com.example.finalprojectlocation.locationreminder.local.ReminderRepository
import com.example.finalprojectlocation.locationreminder.reminderlist.RemindersListViewModel
import com.example.finalprojectlocation.locationreminder.reminderlist.reminderListFragment
import com.example.finalprojectlocation.locationreminder.savereinder.SaveReminderViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.get
import org.mockito.Mockito.mock
import org.koin.test.AutoCloseKoinTest
import org.mockito.Mockito.verify
import java.lang.reflect.Array.get


@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
//UI Testing
@MediumTest
class ReminderListFragmentTest: AutoCloseKoinTest() {

    @get:Rule
    val instantExecutor = InstantTaskExecutorRule()


    private lateinit var repository: RemindeDataSource
    private lateinit var appContext: Application


    @Before
    fun init() {
        stopKoin()//stop the original app koin
        appContext = getApplicationContext()
        val myModule = module {
            viewModel {
                RemindersListViewModel(
                    appContext,
                    get() as RemindeDataSource
                )
            }
            single {
                SaveReminderViewModel(
                    appContext,
                    get() as RemindeDataSource
                )
            }
            single { ReminderRepository(get()) as RemindeDataSource }
            single { LocalD.createRemindersDao(appContext) }
        }
        //declare a new koin module
        startKoin {
            modules(listOf(myModule))
        }
        //Get our real repository
        repository = get()

        //clear the data to start fresh
        runBlocking {
            repository.deleteAllReminders()
        }
    }


    @Test
    fun clickFAB_navigateToSaveReminderFragment () {

        val scenario = launchFragmentInContainer<reminderListFragment>(Bundle(), R.style.Theme_FinalProjectLocation)
        val navController = mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        onView(withId(R.id.addReminderFAB)).perform(click())

        verify(navController).navigate(
            ReminderDesciption.toSaveReminder()
        )
    }

    @Test
    fun remindersList_DisplayedInUI(): Unit = runBlocking {

        val reminder = ReminderDTO("My Shop", "Get to the Shop", "Abuja", 6.54545, 7.54545)

        repository.saveReminder(reminder)

        launchFragmentInContainer<reminderListFragment>(Bundle(), R.style.Theme_FinalProjectLocation)

        onView(withId(R.id.reminderssRecyclerView))
            .perform(
                RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                    hasDescendant(withText(reminder.title))
                )
            )
    }


    @Test
    fun onUI_noDataDisplayed(): Unit = runBlocking {
        val reminder = ReminderDTO("My Shop", "Get to the Shop", "Abuja", 6.54545, 7.54545)

        repository.saveReminder(reminder)

        repository.deleteAllReminders()

        launchFragmentInContainer<reminderListFragment>(Bundle(), R.style.Theme_FinalProjectLocation)

        onView(withId(R.id.noDataTextView)).check(matches(isDisplayed()))
    }

}
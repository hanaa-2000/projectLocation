package  com.example.finalprojectlocation

import android.app.Activity
import android.app.Application
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.finalprojectlocation.locationreminder.Reminder
import com.example.finalprojectlocation.locationreminder.local.LocalD
import com.example.finalprojectlocation.locationreminder.local.RemindeDataSource
import com.example.finalprojectlocation.locationreminder.local.ReminderRepository
import com.example.finalprojectlocation.locationreminder.reminderlist.RemindersListViewModel
import com.example.finalprojectlocation.locationreminder.savereinder.SaveReminderViewModel
import com.example.finalprojectlocation.util.DataBindingIdlingResource
import com.example.finalprojectlocation.util.monitorActivity
import com.example.finalprojectlocation.utils.EspressoIdlingResource
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import java.lang.reflect.Array.get

@RunWith(AndroidJUnit4::class)
@LargeTest

class RemindersActivityTest :
    AutoCloseKoinTest() {

    private lateinit var repository: RemindeDataSource
    private lateinit var appContext: Application


    private val dataBinding = DataBindingIdlingResource()


    @Before
    fun init() {
        stopKoin()
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

        startKoin {
            modules(listOf(myModule))
        }

        repository = get()


        runBlocking {
            repository.deleteAllReminders()
        }
    }


    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBinding)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBinding)
    }


    @Test
    fun saveReminderScreen_showSnackBarTitleError() {

        val activityScenario = ActivityScenario.launch(Reminder::class.java)
        dataBinding.monitorActivity(activityScenario)

        onView(withId(R.id.addReminderFAB)).perform(click())
        onView(withId(R.id.saveReminder)).perform(click())

        val snackBarMessage = appContext.getString(R.string.err_enter_title)
        onView(withText(snackBarMessage)).check(matches(isDisplayed()))

        activityScenario.close()
    }

    @Test
    fun saveReminderScreen_showSnackBarLocationError() {

        val activityScenario = ActivityScenario.launch(Reminder::class.java)
        dataBinding.monitorActivity(activityScenario)

        onView(withId(R.id.addReminderFAB)).perform(click())
        onView(withId(R.id.reminderTitle)).perform(typeText("Title"))
        closeSoftKeyboard()
        onView(withId(R.id.saveReminder)).perform(click())

        val snackBarMessage = appContext.getString(R.string.err_select_location)
        onView(withText(snackBarMessage)).check(matches(isDisplayed()))

        activityScenario.close()
    }

    @Test
    fun saveReminderScreen_showToastMessage() {

        val activityScenario = ActivityScenario.launch(Reminder::class.java)
        dataBinding.monitorActivity(activityScenario)

        onView(withId(R.id.addReminderFAB)).perform(click())
        onView(withId(R.id.reminderTitle)).perform(typeText("Title"))
        closeSoftKeyboard()
        onView(withId(R.id.reminderDescription)).perform(typeText("Description"))
        closeSoftKeyboard()

        onView(withId(R.id.selectLocation)).perform(click())
        onView(withId(R.id.mapFragment)).perform(longClick())
        onView(withId(R.id.save_button)).perform(click())

        onView(withId(R.id.saveReminder)).perform(click())

        onView(withText(R.string.reminder_saved)).inRoot(
            withDecorView(
                not(
                    `is`(
                        getActivity(
                            activityScenario
                        ).window.decorView
                    )
                )
            )
        )
            .check(matches(isDisplayed()))

        activityScenario.close()
    }

    private fun getActivity(activityScenario: ActivityScenario<Reminder>): Activity {
        lateinit var activity: Activity
        activityScenario.onActivity {
            activity = it
        }
        return activity
    }


}

package com.example.finalprojectlocation.savereminder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.finalprojectlocation.MainCoroutineRule
import com.example.finalprojectlocation.data.FakeDataSource
import com.example.finalprojectlocation.locationreminder.reminderlist.ReminderDataItem
import com.example.finalprojectlocation.locationreminder.savereinder.SaveReminderViewModel
import com.google.common.truth.Truth.assertThat
import com.example.finalprojectlocation.R

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.*
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest {


    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()



    private lateinit var remindersRepository: FakeDataSource

    //Subject under test
    private lateinit var viewModel: SaveReminderViewModel

    @Before
    fun setupViewModel() {
        remindersRepository = FakeDataSource()
        viewModel = SaveReminderViewModel(ApplicationProvider.getApplicationContext(), remindersRepository)
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun validateEnteredData_EmptyTitleAndUpdateSnackBar() {
        val reminder = ReminderDataItem("", "Description", "My School", 7.32323, 6.54343)

        assertThat(viewModel.validateEnteredData(reminder)).isFalse()
        assertThat(viewModel.showSnackBarInt.getOrAwaitValue()).isEqualTo(R.string.err_enter_title)
    }

    @Test
    fun validateEnteredData_EmptyLocationAndUpdateSnackBar() {
        val reminder = ReminderDataItem("Title", "Description", "", 7.32323, 6.54343)

        assertThat(viewModel.validateEnteredData(reminder)).isFalse()
        assertThat(viewModel.showSnackBarInt.getOrAwaitValue()).isEqualTo(R.string.err_select_location)
    }


    @Test
    fun saveReminder_showLoading(){
        val reminder = ReminderDataItem("Title", "Description", "Airport", 7.32323, 6.54343)
        mainCoroutineRule.pauseDispatcher()
        viewModel.saveReminder(reminder)
        assertThat(viewModel.showLoading.getOrAwaitValue()).isTrue()
        mainCoroutineRule.resumeDispatcher()
        assertThat(viewModel.showLoading.getOrAwaitValue()).isFalse()
    }




}
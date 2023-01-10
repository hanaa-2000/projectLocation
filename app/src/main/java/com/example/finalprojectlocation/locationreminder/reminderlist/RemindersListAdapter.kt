package com.example.finalprojectlocation.locationreminder.reminderlist

import com.example.finalprojectlocation.R
import com.example.finalprojectlocation.base.BaseRecycler

class RemindersListAdapter (callBack: (selectedReminder: ReminderDataItem) -> Unit) :
    BaseRecycler<ReminderDataItem>(callBack) {
    override fun getLayoutRes(viewType: Int) = R.layout.itreminder
}
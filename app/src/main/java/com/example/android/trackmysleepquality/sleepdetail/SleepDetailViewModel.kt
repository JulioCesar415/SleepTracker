package com.example.android.trackmysleepquality.sleepdetail

import androidx.lifecycle.ViewModel
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import javax.sql.DataSource

class SleepDetailViewModel(
    private val sleepNightKey: Long = 0L,
    dataSource: SleepDatabaseDao) : ViewModel(){

    }
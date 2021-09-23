package com.example.android.trackmysleepquality.sleepdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import javax.sql.DataSource

class SleepDetailViewModel(
    private val sleepNightKey: Long = 0L,
    dataSource: SleepDatabaseDao) : ViewModel(){
        /**
         * Hold reference to SleepDatabase via its SleepDatabaseDao
         */
        val database = dataSource

        private val night = MediatorLiveData<SleepNight>()

        fun getNight() = night

        init {
            night.addSource(database.getNightWithId(sleepNightKey), night::setValue)
        }

    /**
     * Variable that tells fragment whether it should navigate to [SleepTrackerFragment]
     *
     * this is `private` because we dont want to expose the ability to set [MutableLivedata] to
     * the [Fragment]
     * */

    private val _navigateToSleepTracker = MutableLiveData<Boolean?>()

    /**
     * when true immediately navigate back to the [SleepTrackerFragment]
     */

    val navigateToSleepTracker: LiveData<Boolean?>
    get() = _navigateToSleepTracker

    /**
     * call immediately after navigating to [SleepTrackerFragment]
     */

    fun doneNavigating(){
        _navigateToSleepTracker.value = null
    }

    fun onClose(){
        _navigateToSleepTracker.value = true
    }
}
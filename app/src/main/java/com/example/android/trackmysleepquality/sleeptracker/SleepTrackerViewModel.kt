/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.sleeptracker

import android.app.Application
import android.provider.SyncStateContract.Helpers.get
import android.provider.SyncStateContract.Helpers.insert
import androidx.lifecycle.*
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.formatNights
import kotlinx.coroutines.launch

/**
 * ViewModel for SleepTrackerFragment.
 */
class SleepTrackerViewModel(
        val database: SleepDatabaseDao,
        application: Application) : AndroidViewModel(application) {
//                define variable tonight to hold current night
        private var tonight = MutableLiveData<SleepNight?>()
//        define nights, then getAllNights() from database
        private val nights = database.getAllNights()
//        define nightsString
        val nightsString = Transformations.map(nights){ nights ->
                formatNights(nights, application.resources)
        }

//        start button visibility
        val startButtonVisible = Transformations.map(tonight){
                null == it
        }

//        stop button visibility
        val stopButtonVisible = Transformations.map(tonight){
                null != it
        }

//        clear button visibility
        val clearButtonVisible = Transformations.map(nights){
                it?.isNotEmpty()
        }
//        create init block to initialize tonight
        init {
            initializeTonight()
        }
        //        define navigation event live data. set private to not expose val to fragment
        private val _navigateToSleepQuality = MutableLiveData<SleepNight>()
        //        define public reference that has only getter which fragment will observe
        val navigateToSleepQuality: LiveData<SleepNight>
                get() = _navigateToSleepQuality

//        define snackbar when data is cleared
        private var _showSnackbarEvent = MutableLiveData<Boolean>()
//        define public reference that has only getter that fragment will observe
        val showSnackBarEvent: LiveData<Boolean>
        get() = _showSnackbarEvent

//        implement doneShowingSnackbar
        fun doneShowingSnackbar(){
                _showSnackbarEvent.value = false
        }

        //        reset navigation variable after navigating
        fun doneNavigating(){
                _navigateToSleepQuality.value = null
        }

//        implement initializeTonight(). use viewModelScope.launch to start coroutine
        private fun initializeTonight(){
                viewModelScope.launch {
                        tonight.value = getTonightFromDatabase()
                }
        }

//        implement getTonightFromDataBase
        private suspend fun getTonightFromDatabase(): SleepNight? {
                var night = database.getTonight()

                if (night?.endTimeMilli != night?.startTimeMilli){
                        night = null
                }
                return night
        }

//        implement onStartTracking
        fun onStartTracking(){
                viewModelScope.launch {
//                        captures current time as the start time
                        val newNight = SleepNight()
//                        call insert() to insert it into database
                        insert(newNight)
//                        set tonight to new night
                        tonight.value = getTonightFromDatabase()
                }
        }

//        Define insert that takes SleepNight as argument
        private suspend fun insert(night: SleepNight){
//                insert night into the database
                database.insert(night)
        }

//        define onStopTracking(). Launch a coroutine in the viewModelScope
        fun onStopTracking(){

                viewModelScope.launch {
                        val oldNight = tonight.value ?: return@launch
                        oldNight.endTimeMilli = System.currentTimeMillis()
                        update(oldNight)
//                        trigger navigation
                        _navigateToSleepQuality.value = oldNight

                }
        }

//        implement update()
        private suspend fun update(night: SleepNight){
                database.update(night)
        }

//        implement onClear
        fun onClear(){
                viewModelScope.launch {
                        clear()
                        tonight.value = null
                        _showSnackbarEvent.value = true
                }
        }

        suspend fun clear(){
                database.clear()
        }
}


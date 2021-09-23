package com.example.android.trackmysleepquality.sleepdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.databinding.FragmentSleepDetailBinding
import com.example.android.trackmysleepquality.sleepquality.SleepQualityFragmentArgs

class SleepDetailFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        get reference to binding object and inflate fragment view
        val binding: FragmentSleepDetailBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_sleep_detail, container, false)

        val application = requireNotNull(this.activity).application

        val arguments = SleepQualityFragmentArgs.fromBundle(arguments!!)

//        create instance of ViewModel Factory
        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao
        val viewModelFactory = SleepDetailViewModelFactory(arguments.sleepNightKey, dataSource)

//        get reference to ViewModel associated with this fragment
        val sleepDetailViewModel =
            ViewModelProvider(
                this, viewModelFactory).get(SleepDetailViewModel::class.java)

//        to use ViewModel with data binding, you have to explicitly give binding object reference to it
        binding.sleepDetailViewModel = sleepDetailViewModel

        binding.lifecycleOwner = this

//        add observer to state variable for navigating when Quality icon is tapped
        sleepDetailViewModel.navigateToSleepTracker.observe(viewLifecycleOwner, Observer {
//            observe if true
            if (it == true){
                this.findNavController().navigate(
                    SleepDetailFragmentDirections.actionSleepDetailFragmentToSleepTrackerFragment()
                )
                sleepDetailViewModel.doneNavigating()
            }
        })
        return binding.root
    }
}
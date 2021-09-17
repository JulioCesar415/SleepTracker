package com.example.android.trackmysleepquality.sleeptracker

import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.database.SleepNight

class SleepNightAdapter: RecyclerView.Adapter<TextItemViewHolder>() {
//    define data source that holds list of SleepNight
    var data = listOf<SleepNight>()

    //    recycler view needs to know how big to make the scrollbar. this is how to tell recycler view how many views there are
    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) {
        val item = data[position]
        holder.textView.text = item.sleepQuality.toString()
    }
}
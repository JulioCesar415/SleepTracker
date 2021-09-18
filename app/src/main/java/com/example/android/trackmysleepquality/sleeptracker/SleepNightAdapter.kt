package com.example.android.trackmysleepquality.sleeptracker

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.TextItemViewHolder
import com.example.android.trackmysleepquality.database.SleepNight


class SleepNightAdapter: RecyclerView.Adapter<TextItemViewHolder>(){

    var data = listOf<SleepNight>()

//    add custom setter to data that calls notifyDataSetChanged
    set(value) {
//        save new value with field = value
        field = value
//        notifyDataSetChanged()
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) {

        val item = data[position]

        holder.textView.text = item.sleepQuality.toString()

//        show low sleep quality in red. set sleep quality to red if sleepQuality is 1 or less
        if(item.sleepQuality <= 1){
            holder.textView.setTextColor(Color.RED)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val  view = layoutInflater
//                attachToRoot is false because RecyclerView will attach this for us
            .inflate(R.layout.text_item_view, parent, false) as TextView
        return TextItemViewHolder(view)
    }
}
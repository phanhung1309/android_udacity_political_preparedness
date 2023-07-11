package com.example.android.politicalpreparedness.election.adapter

import android.widget.Button
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.network.models.Election
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("listData")
fun RecyclerView.setElectionData(data: List<Election>?) {
    val adapter = adapter as ElectionListAdapter
    adapter.submitList(data)
}

@BindingAdapter("day")
fun TextView.setElectionDay(date: Date?) {
    val calendar = Calendar.getInstance()
    date?.let {
        calendar.time = it
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        text = dateFormat.format(calendar.time)
    }
}

@BindingAdapter("followButtonText")
fun Button.followButtonText(isSavedElection: Boolean?) {
    isSavedElection?.let {
        text = if (it) {
            resources.getString(R.string.unfollow_button)
        } else {
            resources.getString(R.string.follow_button)
        }
    }
}
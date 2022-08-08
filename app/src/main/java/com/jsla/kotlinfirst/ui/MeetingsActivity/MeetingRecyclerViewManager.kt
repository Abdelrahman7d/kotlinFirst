package com.jsla.kotlinfirst.ui.MeetingsActivity

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.jsla.kotlinfirst.data.RecyclerViewManager

class MeetingRecyclerViewManager(recyclerView: RecyclerView,context: Context)
    : RecyclerViewManager(recyclerView,context) {

    override fun setAdapter() {

        val meetingsRA = MeetingsRA()
        recyclerView.adapter = meetingsRA
    }

    override fun setTouchHelper() {

    }
}
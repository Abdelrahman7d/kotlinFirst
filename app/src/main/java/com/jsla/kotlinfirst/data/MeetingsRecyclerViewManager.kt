package com.jsla.kotlinfirst.data

import com.jsla.kotlinfirst.ui.MeetingsActivity.MeetingsRA

class MeetingsRecyclerViewManager(recyclerViewManager: RecyclerViewManager) :
    RecyclerViewManager(recyclerViewManager) {

    override fun setAdapter() {
        val meetingAdapter = MeetingsRA()
        recyclerView.adapter = meetingAdapter
    }

    override fun setTouchHelper() {

    }
}
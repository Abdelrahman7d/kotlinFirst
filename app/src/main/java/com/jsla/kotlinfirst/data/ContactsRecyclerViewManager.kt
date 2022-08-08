package com.jsla.kotlinfirst.data

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.jsla.kotlinfirst.ui.SelectContactsActivity.ContactsInfoRA

class ContactsRecyclerViewManager (recyclerView : RecyclerView, context: Context)
    : RecyclerViewManager(recyclerView,context) {

    override fun setAdapter() {

        val contactsInfoRA : ContactsInfoRA = ContactsInfoRA()
        recyclerView.adapter = contactsInfoRA
    }
    override fun setTouchHelper() {

    }


}
package com.jsla.kotlinfirst.ui.SelectAttendeesActivity

import android.os.Bundle
import com.jsla.kotlinfirst.ui.createMeetActivity.CreateMeetActivity
import com.jsla.kotlinfirst.ui.SelectContactsActivity.ContactsInfoRA
import com.jsla.kotlinfirst.ui.SelectContactsActivity.SelectContactsActivity

class SelectAttendeesActivity: SelectContactsActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.addSelectedPeopleBtn.setOnClickListener {
            addSelectedPeople()
            addFinishingDialog.show()
        }
    }

    init {
        attendeesModelView = CreateMeetActivity.attendeesModelView
    }


    override fun addSelectedPeople() {

        val adapter : ContactsInfoRA = binding.contactsRV.adapter as ContactsInfoRA
        attendeesModelView.addSelectedPeople(adapter.getSelectedContacts())
    }

    companion object{
        lateinit var attendeesModelView : AttendeesModelView
    }
}
package com.jsla.kotlinfirst.ui.SelectAttendeesActivity

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import com.jsla.kotlinfirst.pogo.Attendee
import com.jsla.kotlinfirst.pogo.ContactInfo
import com.jsla.kotlinfirst.pogo.PersonInfo
import com.jsla.kotlinfirst.ui.SelectContactsActivity.ContactInfoViewModel
import java.io.Serializable

class AttendeesModelView() : ContactInfoViewModel(){

    val attendeesInfoMutableLiveData : MutableLiveData<ArrayList<Attendee>> = MutableLiveData()

    override fun addSelectedPeople(contacts: ArrayList<ContactInfo>) {

        super.setContacts()
        attendeesInfoMutableLiveData.value = getAttendees(contacts)
    }

    fun getAttendees(contacts: ArrayList<ContactInfo>): ArrayList<Attendee>? {

        val people = ArrayList<Attendee>()
        for (contact : ContactInfo in contacts.iterator()){
            people.add(getAttendee(contact))
        }

        return people
    }

    fun getAttendee(contact: ContactInfo): Attendee {

        return Attendee(
            PersonInfo(
            contact.name,
            contact.phoneNumber)
        )
    }

}
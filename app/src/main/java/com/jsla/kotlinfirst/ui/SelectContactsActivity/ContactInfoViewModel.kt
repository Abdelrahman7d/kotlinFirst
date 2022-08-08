package com.jsla.kotlinfirst.ui.SelectContactsActivity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jsla.kotlinfirst.data.DBHelper
import com.jsla.kotlinfirst.pogo.ContactInfo
import com.jsla.kotlinfirst.pogo.PersonInfo

open class ContactInfoViewModel : ViewModel() {

    lateinit var context : Context

    open val mutableLiveData : MutableLiveData<ArrayList<ContactInfo>> = MutableLiveData()

    fun setContacts(){

        mutableLiveData.value = getContacts()
    }

    @SuppressLint("Range")
    fun getContacts(): ArrayList<ContactInfo>? {

        val contactsInfo : ArrayList<ContactInfo> = ArrayList()
        val phones = context.contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC")
        while (phones!!.moveToNext()) {
            val name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

            val contactInfo = ContactInfo(name,phoneNumber)
            contactsInfo.add(contactInfo)

        }

        return contactsInfo
    }

    open fun addSelectedPeople(contacts : ArrayList<ContactInfo>) {

        val dp = DBHelper(context,null)
        dp.addListOfPeople(contacts)
    }

    open fun setMyContext(context : Context){
        this.context = context
    }


}
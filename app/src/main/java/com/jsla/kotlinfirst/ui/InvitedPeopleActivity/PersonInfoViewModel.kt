package com.jsla.kotlinfirst.ui.InvitedPeopleActivity

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jsla.kotlinfirst.data.DBHelper
import com.jsla.kotlinfirst.pogo.PersonInfo

class PersonInfoViewModel : ViewModel() {

    val mutableLiveData : MutableLiveData<ArrayList<PersonInfo>> = MutableLiveData()

    fun getInvitedPeople(context : Context){

        mutableLiveData.value = getInvitedPeopleFromDB(context)
    }

    @SuppressLint("Range")
    private fun getInvitedPeopleFromDB(context : Context) : ArrayList<PersonInfo>{

        val peopleInfo : ArrayList<PersonInfo> = ArrayList()

        // creating a DBHelper class
        // and passing context to it
        val db = DBHelper(context, null)

        // below is the variable for cursor
        // we have called method to get
        // all names from our database
        // and add to name text view
        val cursor = db.getPeople()

        // moving the cursor to first position and
        // appending value in the text view
        if (!cursor!!.moveToFirst()){
            cursor.close()
            return peopleInfo
        }

        getPerson(cursor, peopleInfo)
        // moving our cursor to next
        // position and appending values
        while(cursor.moveToNext()){

            getPerson(cursor,peopleInfo)
        }

        // at last we close our cursor
        cursor.close()

        return peopleInfo
    }

    @SuppressLint("Range")
    private fun getPerson(cursor : Cursor, peopleInfo : ArrayList<PersonInfo>) {

        val id : Long = cursor.getInt(cursor.getColumnIndex(DBHelper.PERSON_ID_COL)).toLong()
        val name = cursor.getString(cursor.getColumnIndex(DBHelper.PERSON_NAME_COl))
        val phoneNumber = cursor.getString(cursor.getColumnIndex(DBHelper.PERSON_PHONENUM_COL))
        val invitationStatus = cursor.getInt(cursor.getColumnIndex(DBHelper.PERSON_INVITATION_STATUS_COL))

        peopleInfo.add(PersonInfo(name,phoneNumber,invitationStatus,id))
    }

}
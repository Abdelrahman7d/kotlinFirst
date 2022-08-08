package com.jsla.kotlinfirst.ui.createMeetActivity

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.util.Log
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jsla.kotlinfirst.data.DBHelper
import com.jsla.kotlinfirst.pogo.Attendee
import com.jsla.kotlinfirst.pogo.MeetingInfo

class CreateMeetingViewModel : ViewModel() {

    lateinit var context: Context
    var attendees : ArrayList<Attendee> = ArrayList()
    var title = ObservableField<String>("")
    var loction = ObservableField<String>("")
    var date = ObservableField<String>("Meeting date")
    var time = ObservableField<String>("Meeting time")
    var duration = ObservableField<String>("")

    fun createMeeting(){

        if(!isCorrectInput()){

            Toast.makeText(context,"Please input all required fields!", Toast.LENGTH_SHORT).show()
            return
        }

        val db : DBHelper = DBHelper(context,null)

        val timeEnd = (time.get()!!.toFloat() + duration.get()!!.toFloat()).toString()
        db.MeetingsDBHelper().insertMeeting(
            MeetingInfo(
                -1,
                title.get().toString(),
                loction.get().toString(),
                date.get().toString(),
                time.get().toString(),
                timeEnd,
                attendees)
        )
        db.close()

        clearFields()

        Toast.makeText(context,"Meeting was added successfully",Toast.LENGTH_SHORT).show()
    }

    fun setMyContext(context : Context){
        this.context = context
    }

    fun addAttendee(attendee : Attendee){
        attendees.add(attendee)
    }

    fun clearAttendees(){
        attendees.clear()
    }

    fun clearFields() {

        title.set("")
        loction.set("")
        date.set("Meeting date")
        time.set("Meeting time")
        duration.set("")
        clearAttendees()
    }

    private fun isCorrectInput(): Boolean {

        return !( title.get()!!.isEmpty()
                || loction.get()!!.isEmpty()
                || date.get()!! == "Meeting date"
                || time.get()!! == "Meeting time"
                || duration.get()!!.isEmpty())
    }
}
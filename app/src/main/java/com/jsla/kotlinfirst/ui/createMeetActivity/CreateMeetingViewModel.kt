package com.jsla.kotlinfirst.ui.createMeetActivity

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.util.Log
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


    val mutableLiveData : MutableLiveData<ArrayList<MeetingInfo>> = MutableLiveData()

    fun setValue() {

        mutableLiveData.value = getMeetings()
    }

    private fun getMeetings(): ArrayList<MeetingInfo>? {

        val cursor = DBHelper(context,null).MeetingsDBHelper().getMeetings()

        val meetings = ArrayList<MeetingInfo>()

        if (!cursor!!.moveToFirst()){
            cursor.close()
            return meetings
        }

        meetings.add(getMeeting(cursor))

        while (cursor!!.moveToNext()){
            meetings.add(getMeeting(cursor))
        }
        cursor.close()
        return meetings
    }

    @SuppressLint("Range")
    private fun getMeeting(cursor: Cursor): MeetingInfo {

        val id = cursor.getString(cursor.getColumnIndex(DBHelper.MEETING_ID_COL)).toLong()
        val title = cursor.getString(cursor.getColumnIndex(DBHelper.MEETING_TITLE_COl))
        val location = cursor.getString(cursor.getColumnIndex(DBHelper.MEETING_LOCATION_COl))
        val date = cursor.getString(cursor.getColumnIndex(DBHelper.MEETING_DATE_COl))
        val timeStart = cursor.getString(cursor.getColumnIndex(DBHelper.MEETING_TIME_START_COl)).toInt()
        val duration = 2
        val timeEnd = (timeStart + duration).toString()

        val meetingInfo  = MeetingInfo(
            id,
            title,
            location,
            date,
            timeStart.toString(),
            timeEnd,
            ArrayList()
        )

        return meetingInfo
    }

    fun createMeeting(){

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
}
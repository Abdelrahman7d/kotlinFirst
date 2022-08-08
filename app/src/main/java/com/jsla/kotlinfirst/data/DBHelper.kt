package com.jsla.kotlinfirst.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.jsla.kotlinfirst.pogo.Attendee
import com.jsla.kotlinfirst.pogo.ContactInfo
import com.jsla.kotlinfirst.pogo.MeetingInfo

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    // below is the method for creating a database by a sqlite query
    override fun onCreate(db: SQLiteDatabase) {

        createTables(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        // this method is to check if table already exists
        db.execSQL("DROP TABLE IF EXISTS " + PERSON_TABLE_NAME)
        onCreate(db)
    }

    private fun createTables(db: SQLiteDatabase){

        var query = ("CREATE TABLE " + PERSON_TABLE_NAME + " (" +
                PERSON_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PERSON_NAME_COl + " TEXT," +
                PERSON_PHONENUM_COL + " TEXT" + "," +
                PERSON_INVITATION_STATUS_COL + " INTEGER" + ")")

        db.execSQL(query)

        query = ("CREATE TABLE " + MEETING_TABLE_NAME + " (" +
                MEETING_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MEETING_TITLE_COl + " TEXT," +
                MEETING_LOCATION_COl + " TEXT," +
                MEETING_DATE_COl + " TEXT," +
                MEETING_TIME_START_COl + " INTEGER," +
                MEETING_TIME_END_COl + " INTEGER" + ")")

        db.execSQL(query)

        query = ("CREATE TABLE $MEETINGS_ATTENDEES_TABLE_NAME(" +
                "$MEETING_ATTENDEES_M_ID_COL INTEGER," +
                "$MEETING_ATTENDEES_P_ID_COL INTEGER," +
                "$MEETING_ATTENDEES_DURATION_COL DECIMAL(10,2)," +
                "FOREIGN KEY ($MEETING_ATTENDEES_M_ID_COL) REFERENCES $MEETING_TABLE_NAME($MEETING_ID_COL)," +
                "FOREIGN KEY ($MEETING_ATTENDEES_P_ID_COL) REFERENCES $PERSON_TABLE_NAME($PERSON_ID_COL)" +
                ")")

        db.execSQL(query)
    }

    // This method is for adding data in our database
    fun addPerson(name : String, phoneNumber : String) : Long{

        // below we are creating
        // a content values variable
        val values = ContentValues()

        // we are inserting our values
        // in the form of key-value pair
        values.put(PERSON_NAME_COl, name)
        values.put(PERSON_PHONENUM_COL, phoneNumber)
        values.put(PERSON_INVITATION_STATUS_COL, 0)

        // here we are creating a
        // writable variable of
        // our database as we want to
        // insert value in our database
        val db = this.writableDatabase

        // all values are inserted into database
        val result : Long = db.insert(PERSON_TABLE_NAME, null, values)

        // at last we are
        // closing our database
        db.close()

        return result
    }

    // below method is to get
    // all data from our database
    fun getPeople(): Cursor? {

        // here we are creating a readable
        // variable of our database
        // as we want to read value from it
        val db = this.readableDatabase

        // below code returns a cursor to
        // read data from the database
        return db.rawQuery("SELECT * FROM " + PERSON_TABLE_NAME, null)

    }

    fun updateInvitationStatus(personId : Long,newStatus : Int){

        val values = ContentValues()
        values.put(PERSON_INVITATION_STATUS_COL,newStatus)

        val db = this.writableDatabase
        val whereArgs = arrayOf<String>()
        whereArgs.plus("")

        // all values are inserted into database
        db.update(PERSON_TABLE_NAME, values,"$PERSON_ID_COL = $personId" , whereArgs)

    }

    fun deletePerson(personId : Long){

        val db = this.writableDatabase
        val whereArgs = arrayOf<String>()
        whereArgs.plus("")

        db.delete(PERSON_TABLE_NAME,"$PERSON_ID_COL = $personId",whereArgs)
    }

    fun addListOfPeople(contacts : ArrayList<ContactInfo>){

        val iterator = contacts.iterator()

        for (contact : ContactInfo in  iterator) {
            val name = contact.name
            val phoneNum = contact.phoneNumber

            addPerson(name, phoneNum)
        }
    }

    inner class MeetingsDBHelper(){

        fun insertMeeting(meetingInfo: MeetingInfo){

            val values = ContentValues()

            values.put(MEETING_TITLE_COl,meetingInfo.meetingTitle)
            values.put(MEETING_LOCATION_COl,meetingInfo.meetingLocation)
            values.put(MEETING_DATE_COl,meetingInfo.meetingDate)
            values.put(MEETING_TIME_START_COl,meetingInfo.meetingStartTime)
            values.put(MEETING_TIME_END_COl,meetingInfo.meetingEndTime)

            val db = this@DBHelper.writableDatabase
            val meetingId = db.insert(MEETING_TABLE_NAME,null,values)
            db.close()

            AttendeesDBHelper().insertAttendees(meetingId,meetingInfo.meetingAttendees)
        }

        fun getMeetings() : Cursor? {

            val db = this@DBHelper.readableDatabase
            val cursor = db.rawQuery("SELECT * FROM " + MEETING_TABLE_NAME, null)

            return cursor
        }

    }

    inner class AttendeesDBHelper(){

        fun insertAttendees(meetingId : Long, attendees : ArrayList<Attendee>){

            var query = "INSERT INTO $MEETINGS_ATTENDEES_TABLE_NAME VALUES \n"
            val iterator = attendees.iterator()

            for (attendee : Attendee in iterator){

                val attendeeId = addPerson(attendee.name,attendee.phoneNumber)
                val duration = attendee.duration

                query += getRow(meetingId,attendeeId,duration)

                if (iterator.hasNext())
                    query += ",\n"
            }

            val db = this@DBHelper.writableDatabase
            db.execSQL(query)
            db.close()
        }

        fun getRow(meetingId : Long, attendeeId : Long, duration : Float) : String{

            return "($meetingId, $attendeeId, $duration)"
        }
    }

    companion object{

        private val DATABASE_NAME = "Party Manager DB"

        private val DATABASE_VERSION = 1

        //Person cols names

        val PERSON_TABLE_NAME = "person_info"

        val PERSON_ID_COL = "id"

        val PERSON_NAME_COl = "name"

        val PERSON_PHONENUM_COL = "phone_number"

        val PERSON_INVITATION_STATUS_COL = "invitation_status"

        //Meeting cols names

        val MEETING_TABLE_NAME = "meeting"

        val MEETING_ID_COL = "id"

        val MEETING_TITLE_COl = "title"

        val MEETING_LOCATION_COl = "location"

        val MEETING_DATE_COl = "date"

        val MEETING_TIME_START_COl = "time_start"

        val MEETING_TIME_END_COl = "time_end"

         //Meeting attendees cols names

        val MEETINGS_ATTENDEES_TABLE_NAME = "meeting_attendees"

        val MEETING_ATTENDEES_M_ID_COL= "m_id"

        val MEETING_ATTENDEES_P_ID_COL= "p_id"

        val MEETING_ATTENDEES_DURATION_COL= "duration"

    }

}

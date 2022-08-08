package com.jsla.kotlinfirst.pogo

data class MeetingInfo (
    val meetingId: Long = -1,
    val meetingTitle: String,
    val meetingLocation: String,
    val meetingDate: String,
    val meetingStartTime: String,
    val meetingEndTime: String,
    val meetingAttendees: ArrayList<Attendee>
) {

}
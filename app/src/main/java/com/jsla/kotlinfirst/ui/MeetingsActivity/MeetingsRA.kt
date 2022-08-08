package com.jsla.kotlinfirst.ui.MeetingsActivity

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jsla.kotlinfirst.R
import com.jsla.kotlinfirst.pogo.MeetingInfo
import com.jsla.kotlinfirst.pogo.PersonInfo

class MeetingsRA : RecyclerView.Adapter<MeetingsRA.MeetingVH>() {

    private var meetings : ArrayList<MeetingInfo> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeetingVH {
        val itemView = LayoutInflater.from(parent.context).inflate( R.layout.meeting_holder,parent,false)

        return MeetingVH(itemView)
    }

    override fun onBindViewHolder(holder: MeetingVH, position: Int) {

        val meeting = meetings[position]

        holder.meetingTitleTV.text = meeting.meetingTitle
        holder.meetingLocationTV.text = meeting.meetingLocation
        holder.meetingDateTV.text = meeting.meetingDate
        holder.meetingTimeTV.text = "${meeting.meetingStartTime} - ${meeting.meetingEndTime}"
        holder.meetingAttendeesTV.text = "${ meeting.meetingAttendees.size } person"
    }

    override fun getItemCount(): Int {
        return meetings.size
    }

    inner class MeetingVH (val itemView: View) : RecyclerView.ViewHolder(itemView) {

        val meetingInfoLL : LinearLayout = itemView.findViewById(R.id.meetingInfoLL)
        val meetingTitleTV : TextView = itemView.findViewById(R.id.meetingTitleTV)
        val meetingLocationTV : TextView = itemView.findViewById(R.id.meetingLocationTV)
        val meetingDateTV : TextView = itemView.findViewById(R.id.meetingDateBtn)
        val meetingTimeTV : TextView = itemView.findViewById(R.id.meetingTimeBtn)
        val meetingAttendeesTV : TextView = itemView.findViewById(R.id.meetingAttendeesTV)

        fun setListeners(){

            meetingInfoLL.setOnClickListener {

            }
        }


    }

    fun setList(meetings:ArrayList<MeetingInfo>){
        this.meetings = meetings
        notifyDataSetChanged()
    }

}
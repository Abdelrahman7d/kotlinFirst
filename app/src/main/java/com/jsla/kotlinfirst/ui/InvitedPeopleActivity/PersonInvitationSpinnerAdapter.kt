package com.jsla.kotlinfirst.ui.InvitedPeopleActivity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.jsla.kotlinfirst.R
import com.jsla.kotlinfirst.pogo.InvitationAcceptanceStatus

class PersonInvitationSpinnerAdapter : BaseAdapter {

    private var invitationAcceptanceStatus: List<InvitationAcceptanceStatus>
    get
    private var context : Context
    get


    constructor(invitationAcceptanceStatus: List<InvitationAcceptanceStatus>, context: Context){
        this.invitationAcceptanceStatus = invitationAcceptanceStatus
        this.context = context
    }

    override fun getCount(): Int {
        return invitationAcceptanceStatus.size
    }

    override fun getItem(position: Int): InvitationAcceptanceStatus {
        return invitationAcceptanceStatus[position]
    }

    override fun getItemId(position: Int): Long {
        return -1;
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var view : View = LayoutInflater.from(context).inflate(R.layout.person_invitation_acceptance_status_holder,parent,false)

        val optionTextView : TextView = view.findViewById(R.id.statusTextView)
        val optionView : View = view.findViewById(R.id.statusColorView)

        val status : String = invitationAcceptanceStatus[position].status
        optionTextView.text = status

        val background : Int =  (invitationAcceptanceStatus[position].background)
        optionView.background = context.getDrawable(background)

        return view
    }
}
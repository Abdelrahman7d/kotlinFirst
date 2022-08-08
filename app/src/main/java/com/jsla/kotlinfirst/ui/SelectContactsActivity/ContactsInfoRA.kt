package com.jsla.kotlinfirst.ui.SelectContactsActivity

import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.jsla.kotlinfirst.R
import com.jsla.kotlinfirst.pogo.ContactInfo

class ContactsInfoRA : RecyclerView.Adapter<ContactsInfoRA.ContactsInfoVH>() {
    private var ContactsInfoList = ArrayList<ContactInfo>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsInfoVH {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.contact_holder, parent,false)

        return ContactsInfoVH(itemView)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ContactsInfoVH, position: Int) {

        val ContactsInfoObj = ContactsInfoList[position]

        holder.nameTV?.text = ContactsInfoObj.name
        holder.phoneNumberTV?.text = ContactsInfoObj.phoneNumber
        holder.setSelectionStatus(ContactsInfoObj.isSelected)

    }

    override fun getItemCount(): Int {
        return ContactsInfoList.size
    }

    inner class ContactsInfoVH : RecyclerView.ViewHolder {

        var containerLL: LinearLayout? = null
        var infoLL: LinearLayout? = null
        var nameTV: TextView? = null
        var phoneNumberTV: TextView? = null

        @RequiresApi(Build.VERSION_CODES.O)
        constructor(itemView: View) : super(itemView) {

            initializeComponents(itemView)
            setListeners()
        }

        private fun initializeComponents(itemView: View) {

            containerLL = itemView.findViewById(R.id.containerLL);
            infoLL = itemView.findViewById(R.id.infoLL);
            nameTV = itemView.findViewById(R.id.nameTV);
            phoneNumberTV = itemView.findViewById(R.id.phoneNumberTV);

        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun setListeners() {

            infoLL?.setOnClickListener {

                val isSelected = !ContactsInfoList[adapterPosition].isSelected
                ContactsInfoList[adapterPosition].isSelected = isSelected

                setSelectionStatus(isSelected)
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun setSelectionStatus(isSelected: Boolean){

            val background : Int
            val textColor : Color
            if (isSelected){

                background = R.drawable.primary_blue_gradient
                textColor = Color.valueOf(Color.WHITE)
            } else {

                background = R.drawable.primary_white_gradient
                textColor = Color.valueOf(nameTV!!.context.getResources().getColor(R.color.blue))
            }

            containerLL!!.background = containerLL!!.context.getDrawable(background)
            nameTV!!.setTextColor(textColor.toArgb())
            phoneNumberTV!!.setTextColor(textColor.toArgb())

        }
    }

    fun setList(ContactsInfoList : ArrayList<ContactInfo>){
        this.ContactsInfoList = ContactsInfoList
        notifyDataSetChanged()
    }

    fun getSelectedContacts() : ArrayList<ContactInfo>{

        val selectedContactInfo : ArrayList<ContactInfo> = ArrayList()

        val iterator = ContactsInfoList.iterator()

        for (contactInfo : ContactInfo in  iterator) {
            if (contactInfo.isSelected)
                selectedContactInfo.add(contactInfo)
        }

        return selectedContactInfo
    }

    fun getList() : ArrayList<ContactInfo>{

        return ContactsInfoList
    }

}
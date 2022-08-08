package com.jsla.kotlinfirst.ui.InvitedPeopleActivity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.jsla.kotlinfirst.R
import com.jsla.kotlinfirst.data.DBHelper
import com.jsla.kotlinfirst.helperInterfaces.OnClick
import com.jsla.kotlinfirst.pogo.InvitationAcceptanceStatus
import com.jsla.kotlinfirst.pogo.PersonInfo

class PersonInfoRA(private val context: Context, private val onClick: OnClick) : RecyclerView.Adapter<PersonInfoRA.PersonInfoVH>() {

    private var peopleInfo: ArrayList<PersonInfo> = ArrayList()
    private var onClickInterface : OnClick = onClick

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonInfoVH {

        val itemView : View = LayoutInflater.from(context).inflate(R.layout.person_holder,parent,false)

        return PersonInfoVH(itemView,context)
    }

    override fun onBindViewHolder(holder: PersonInfoVH, position: Int) {

        val personInfo : PersonInfo = peopleInfo[position]
        val id : Long = personInfo.id
        val status : Int = personInfo.invitationStatus
        val name : String = personInfo.name
        val phoneNumber : String = personInfo.phoneNumber
        val background : Int = when(status){
            1 -> R.drawable.invited_person_gradient
            2 -> R.drawable.not_coming_person_gradient
            else -> R.drawable.not_invited_person_gradient
        }

        holder.containerLL?.background = context.getDrawable(background)
        holder.statusSpinner?.setSelection(status)
        holder.nameTV?.text = name
        holder.phoneNumberTV?.text = phoneNumber
        holder.personId = id

    }

    override fun getItemCount(): Int {
        return peopleInfo.size
    }

    inner class PersonInfoVH : RecyclerView.ViewHolder{

        var personId : Long = 0
        var context : Context
        var containerLL : LinearLayout? = null
        var infoLL : LinearLayout? = null
        var nameTV : TextView? = null
        var phoneNumberTV : TextView? = null
        var statusSpinner : Spinner? = null
        var callBtn : Button? = null

        constructor(itemView: View, context: Context) : super(itemView) {

            this.context = context
            initializeComponents(itemView)
            initializeSpinnerAdapter()
            setListeners()
        }

        private fun initializeComponents(itemView: View) {

            containerLL = itemView.findViewById(R.id.containerLL);
            infoLL = itemView.findViewById(R.id.infoLL);
            nameTV = itemView.findViewById(R.id.nameTV);
            phoneNumberTV = itemView.findViewById(R.id.phoneNumberTV);
            statusSpinner = itemView.findViewById(R.id.statusSpinner);
            callBtn = itemView.findViewById(R.id.callBtn);
        }

        private fun initializeSpinnerAdapter() {

            val statusList : ArrayList<InvitationAcceptanceStatus> = ArrayList<InvitationAcceptanceStatus>()

            statusList.add(InvitationAcceptanceStatus("Not invited",R.drawable.not_invited_person_gradient))
            statusList.add(InvitationAcceptanceStatus("Invited",R.drawable.invited_person_gradient))
            statusList.add(InvitationAcceptanceStatus("Not coming",R.drawable.not_coming_person_gradient))

            val spinnerAdapter : PersonInvitationSpinnerAdapter? =
                context?.let { PersonInvitationSpinnerAdapter(statusList, it) }

            statusSpinner?.adapter = spinnerAdapter

            statusSpinner?.onItemSelectedListener =  object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                    val background = statusList[position].background
                    peopleInfo[adapterPosition].invitationStatus = position
                    containerLL?.background = context.getDrawable(background)
                    val dbHelper = DBHelper(context,null)
                    dbHelper?.updateInvitationStatus(personId,position)
                }

            }
        }

        private fun setListeners(){

            infoLL?.setOnClickListener {

                if(callBtn?.isVisible == true)
                    callBtn?.visibility = View.GONE
                else
                    callBtn?.visibility = View.VISIBLE
            }

            callBtn?.setOnClickListener {
                onClickInterface.onClick(phoneNumberTV?.text.toString())
            }
        }

    }

    fun setList(peopleInfo:ArrayList<PersonInfo>){
        this.peopleInfo = peopleInfo
        notifyDataSetChanged()

    }

    fun addPerson(person : PersonInfo){

        peopleInfo.add(person)
        notifyItemInserted(peopleInfo.size - 1)
    }

    fun deletePerson(position: Int) : Long{

        val id : Long = peopleInfo[position].id
        peopleInfo.removeAt(position)
        notifyItemRemoved(position)
        return id
    }

}
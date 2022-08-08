package com.jsla.kotlinfirst.ui.createMeetActivity

import android.Manifest
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.jsla.kotlinfirst.R
import com.jsla.kotlinfirst.data.ContactsRecyclerViewManager
import com.jsla.kotlinfirst.databinding.ActivityCreateMeetBinding
import com.jsla.kotlinfirst.helperInterfaces.ActivityInitializer
import com.jsla.kotlinfirst.pogo.Attendee
import com.jsla.kotlinfirst.pogo.ContactInfo
import com.jsla.kotlinfirst.pogo.MeetingInfo
import com.jsla.kotlinfirst.pogo.PersonInfo
import com.jsla.kotlinfirst.ui.SelectAttendeesActivity.AttendeesModelView
import com.jsla.kotlinfirst.ui.SelectContactsActivity.ContactInfoViewModel
import com.jsla.kotlinfirst.ui.SelectContactsActivity.ContactsInfoRA

class CreateMeetActivity : AppCompatActivity(), ActivityInitializer<ActivityCreateMeetBinding>{

    lateinit var binding : ActivityCreateMeetBinding
    lateinit var createMeetingViewModel : CreateMeetingViewModel
    var addPeopleDialog : Dialog? = null
    var attendees : ArrayList<Attendee> = ArrayList()
    var isTimeSet = false
    var isDateSet = false

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = bind(this, R.layout.activity_create_meet)

        if(!checkPermission()){
            return
        }
        setListeners()
        setComponents()
    }

    override fun setComponents() {

        setAttendeesViewModel()
        setMeetingsViewModel()
        createAddPeopleDialog()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun setListeners() {

        binding.meetingDateBtn.setOnClickListener {

            getDate()
        }

        binding.meetingTimeBtn.setOnClickListener {

            getMeetingTime()
        }

        binding.addPeopleBtn.setOnClickListener {

            if (addPeopleDialog != null)
                addPeopleDialog!!.show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getDate(){

        val dateDialog : DatePickerDialog = DatePickerDialog(this)

        dateDialog.setOnDateSetListener { view, year, month, dayOfMonth ->

            val date = "$year - $month - $dayOfMonth"
            binding.meetingDateBtn.text = date
            isDateSet = true
        }

        dateDialog.show()
    }

    private fun getMeetingTime(){

        val onTimeSet = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            val time = hourOfDay.toString()
            binding.meetingTimeBtn.text = time

            isTimeSet = true
        }

        val timeDialog : TimePickerDialog = TimePickerDialog(
            this,
            onTimeSet,
            12,
            0,
            true
        )

        timeDialog.show()
    }

    private fun createAddPeopleDialog(){

        addPeopleDialog = Dialog(this)

        addPeopleDialog!!.setContentView(R.layout.dialog_add_selected_contacts)
        addPeopleDialog!!.setCancelable(false)

        val searchSV : SearchView = addPeopleDialog!!.findViewById(R.id.searchSV)
        val contactsRV : RecyclerView = addPeopleDialog!!.findViewById(R.id.contactsRV)
        ContactsRecyclerViewManager(contactsRV,this)

        val addPeopleBtn : Button = addPeopleDialog!!.findViewById(R.id.addSelectedPeopleBtn)
        addPeopleBtn.setOnClickListener {
            val contactsInfoRA = contactsRV.adapter as ContactsInfoRA

            addSelectedPeople(contactsInfoRA.getSelectedContacts())
        }

        val contactInfoViewModel : ContactInfoViewModel = ViewModelProvider(this)[ContactInfoViewModel::class.java]
        contactInfoViewModel.setMyContext(this)
        contactInfoViewModel.setContacts()

        contactInfoViewModel.mutableLiveData.observe(this, Observer<ArrayList<ContactInfo>> { it ->

            val contactsInfoRA = contactsRV.adapter as ContactsInfoRA
            contactsInfoRA.setList(it)
        })


        setSearchAlgo(searchSV, contactsRV)

    }

    private fun setSearchAlgo(searchSV : SearchView,contactsRV : RecyclerView) {

        val contactsInfoRA = contactsRV.adapter as ContactsInfoRA

        searchSV.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            val contactsInfoList = contactsInfoRA.getList()

            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {


                if(newText.toString().isNotEmpty()) {
                    contactsInfoRA.setList(searchForName(newText))
                } else {
                    contactsInfoRA.setList(contactsInfoList)
                }

                return false
            }

            fun searchForName(query: String?) : ArrayList<ContactInfo>{

                val iterator = contactsInfoList.iterator()
                val searchAppearanceList = ArrayList<ContactInfo>()


                for (contactInfo : ContactInfo in iterator){

                    if(contactInfo.name.contains(query.toString())) {
                        searchAppearanceList.add(contactInfo)
                    }
                }

                return searchAppearanceList
            }
        })
    }

    fun addSelectedPeople(contacts : ArrayList<ContactInfo>) {

        createMeetingViewModel.clearAttendees()
        for (contact : ContactInfo in contacts.iterator()){
            createMeetingViewModel.addAttendee(Attendee(PersonInfo(contact.name,contact.phoneNumber)))
        }

        addPeopleDialog!!.hide()
    }

    private fun setAttendeesViewModel(){

        attendeesModelView = ViewModelProvider(this)[AttendeesModelView::class.java]
        attendeesModelView.setMyContext(this)

        attendeesModelView.attendeesInfoMutableLiveData.observe(this, Observer<ArrayList<Attendee>>(){ it ->

            attendees = it
        })
    }

    private fun setMeetingsViewModel(){

        createMeetingViewModel = ViewModelProvider(this)[CreateMeetingViewModel::class.java]
        createMeetingViewModel.setMyContext(this)
        binding.meetingViewModel = createMeetingViewModel
    }

    private fun checkPermission(): Boolean {

        var isAllowed = false

        val activityResultCallback : ActivityResultCallback<Boolean> = ActivityResultCallback() {

            if(it) {

                isAllowed = true
            }
        }

        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission(),
                activityResultCallback)

        when {
            ContextCompat.checkSelfPermission(
                application,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.

                isAllowed = true
            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(
                    Manifest.permission.READ_CONTACTS)

            }
        }

        return isAllowed
    }

    companion object{
        lateinit var attendeesModelView : AttendeesModelView
    }
}
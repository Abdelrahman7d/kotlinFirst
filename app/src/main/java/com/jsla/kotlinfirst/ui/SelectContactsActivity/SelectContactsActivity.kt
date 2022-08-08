package com.jsla.kotlinfirst.ui.SelectContactsActivity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jsla.kotlinfirst.R
import com.jsla.kotlinfirst.data.ContactsRecyclerViewManager
import com.jsla.kotlinfirst.databinding.ActivitySelectContactsBinding
import com.jsla.kotlinfirst.helperInterfaces.ActivityInitializer
import com.jsla.kotlinfirst.pogo.ContactInfo


open class SelectContactsActivity : AppCompatActivity(), ActivityInitializer<ActivitySelectContactsBinding> {

    open lateinit var binding : ActivitySelectContactsBinding

    lateinit var contactInfoViewModel : ContactInfoViewModel
    open lateinit var addFinishingDialog : Dialog
    var contactsInfoList : ArrayList<ContactInfo> = ArrayList()

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding  = bind(this,R.layout.activity_select_contacts)

        if (!checkPermission())
            return

        setComponents()
        setListeners()
    }


    override fun setComponents() {

        val contactsRecyclerViewManager = ContactsRecyclerViewManager(binding.contactsRV,application)
        setMutableData()

        addFinishingDialog = createAddFinishingDialog()
    }

    override fun setListeners() {

        binding.addSelectedPeopleBtn.setOnClickListener {
            addSelectedPeople()
        }

        setSearchAlgo()
    }

    private fun setSearchAlgo() {

        binding.searchSV.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            val contactsInfoRA = getContactsAdapter()

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

    open fun addSelectedPeople() {

        val adapter : ContactsInfoRA = binding.contactsRV.adapter as ContactsInfoRA

        contactInfoViewModel.addSelectedPeople(adapter.getSelectedContacts())
        addFinishingDialog.show()
    }

    private fun checkPermission(): Boolean {

        var isAllowed = false

        val activityResultCallback : ActivityResultCallback<Boolean> = ActivityResultCallback() {

            if(it) {

                isAllowed = true
                setComponents()
                setListeners()
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

    fun setMutableData() {

        contactInfoViewModel = ViewModelProvider(this)[ContactInfoViewModel::class.java]
        contactInfoViewModel.setMyContext(this)

        contactInfoViewModel.setContacts()

        contactInfoViewModel.mutableLiveData.observe(this, Observer<ArrayList<ContactInfo>> { it ->

            val adapter = getContactsAdapter()
            adapter.setList(it)
            contactsInfoList = it
        })
    }

    private fun getContactsAdapter() : ContactsInfoRA{

        return binding.contactsRV.adapter as ContactsInfoRA
    }

    private fun createAddFinishingDialog() : Dialog {

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.people_added_dialog)

        val finishBtn : Button = dialog.findViewById(R.id.finishBtn)

        finishBtn.setOnClickListener(){
            dialog.hide()
            finish()
        }
        dialog.setCancelable(false)

        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_empty_background)
        return dialog
    }

}
package com.jsla.kotlinfirst.ui.InvitedPeopleActivity

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jsla.kotlinfirst.R
import com.jsla.kotlinfirst.data.DBHelper
import com.jsla.kotlinfirst.data.RecyclerViewManager
import com.jsla.kotlinfirst.databinding.ActivityInvitedPeopleBinding
import com.jsla.kotlinfirst.helperInterfaces.ActivityInitializer
import com.jsla.kotlinfirst.helperInterfaces.OnClick
import com.jsla.kotlinfirst.pogo.PersonInfo
import com.jsla.kotlinfirst.ui.SelectContactsActivity.SelectContactsActivity

class InvitedPeopleActivity : AppCompatActivity(), ActivityInitializer<ActivityInvitedPeopleBinding> {

    lateinit var personInfoViewModel:PersonInfoViewModel

    lateinit var recyclerViewManager : RecyclerViewManager
    lateinit var addPersonDialog : Dialog
    lateinit var binding : ActivityInvitedPeopleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = bind(this,R.layout.activity_invited_people)
        setComponents()
        setListeners()
    }

    override fun onStart() {
        super.onStart()
        setMutableData()
    }

    override fun setComponents() {

        recyclerViewManager = RecyclerViewManager(binding.invitedPeopleRV, applicationContext,setOnClickInterface())
        addPersonDialog = createAddPersonDialog()
    }

    override fun setListeners() {

        binding.addPersonCV.setOnClickListener { it
            showAddPersonDialog()
        }

    }

    private fun createAddPersonDialog() : Dialog{

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.add_person_dialog)

        val nameET : EditText = dialog.findViewById(R.id.nameET)
        val phoneNumberET : EditText = dialog.findViewById(R.id.phoneNumberET)
        val addPersonBtn : Button = dialog.findViewById(R.id.finishBtn)
        val selectContactsTV : TextView = dialog.findViewById(R.id.selectContactsTV)

        addPersonBtn.setOnClickListener(){

            val isAdded = addPerson(
                nameET.text.toString(),
                phoneNumberET.text.toString(),
            )

            if(isAdded){
                Toast.makeText(applicationContext, "Added to successfully", Toast.LENGTH_LONG).show()
                nameET.text.clear()
                phoneNumberET.text.clear()
                binding.invitedPeopleRV.adapter?.notifyItemInserted(binding.invitedPeopleRV.adapter!!.itemCount)
            }
        }


        selectContactsTV.setOnClickListener{
            startActivity(Intent(this,SelectContactsActivity::class.java))
            dialog.hide()
        }

        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_empty_background)
        return dialog
    }

    private fun showAddPersonDialog(){
        addPersonDialog.show()
    }
    private fun addPerson(name: String, phoneNumber: String) : Boolean{

        if(name.isEmpty() || phoneNumber.isEmpty()){
            Toast.makeText(applicationContext,"Please input all required fields",Toast.LENGTH_SHORT).show()
            return false
        } else {
            return insertPerson(name, phoneNumber)
        }
    }

    private fun insertPerson(name: String, phoneNumber: String) : Boolean{

        val personInfoRA : PersonInfoRA = binding.invitedPeopleRV.adapter as PersonInfoRA

        val db = DBHelper(this, null)
        val id = db.addPerson(name, phoneNumber)

        if (id != -1L)
        personInfoRA.addPerson(PersonInfo(name,phoneNumber,id = id))

        return id != -1L
    }

    private fun setMutableData() {

        personInfoViewModel = ViewModelProvider(this)[PersonInfoViewModel::class.java]
        personInfoViewModel.getInvitedPeople(this)


        personInfoViewModel.mutableLiveData.observe(this,Observer<ArrayList<PersonInfo>>(){ it ->
            val adapter : PersonInfoRA = binding.invitedPeopleRV.adapter as PersonInfoRA
            adapter.setList(it)
        })


    }

    private fun setOnClickInterface() : OnClick{

        val onClickInterFace : OnClick = object : OnClick {
            override fun onClick(phoneNum : String) {

                val intent : Intent = Intent()
                intent.action = Intent.ACTION_DIAL
                intent.data = Uri.parse("tel: $phoneNum")
                startActivity(intent)
            }
        }

        return onClickInterFace
    }

}
package com.jsla.kotlinfirst.ui.MeetingsActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jsla.kotlinfirst.R
import com.jsla.kotlinfirst.databinding.ActivityMeetingsBinding
import com.jsla.kotlinfirst.helperInterfaces.ActivityInitializer
import com.jsla.kotlinfirst.pogo.MeetingInfo
import com.jsla.kotlinfirst.ui.createMeetActivity.CreateMeetActivity

class MeetingsActivity : AppCompatActivity(), ActivityInitializer<ActivityMeetingsBinding> {

    lateinit var binding : ActivityMeetingsBinding
    lateinit var meetingsViewModel : MeetingsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = bind(this,R.layout.activity_meetings)

        setListeners()
        setComponents()
    }

    override fun onStart() {
        super.onStart()
        meetingsViewModel.setValue()

    }
    override fun setComponents() {

        setMeetingsViewModel()

        val recyclerViewManager = MeetingRecyclerViewManager(binding.meetingsRV,this)
    }

    override fun setListeners() {

        binding.createMeetBtn.setOnClickListener {
            startActivity(Intent(applicationContext, CreateMeetActivity::class.java))
        }
    }

    private fun setMeetingsViewModel() {

        meetingsViewModel = ViewModelProvider(this)[MeetingsViewModel::class.java]
        meetingsViewModel.setMyContext(this)

        meetingsViewModel.mutableLiveData.observe(this, Observer<ArrayList<MeetingInfo>> { it

            val adapter = binding.meetingsRV.adapter as MeetingsRA

            adapter.setList(it)
        })

    }


}
package com.jsla.kotlinfirst.ui.MainActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import com.jsla.kotlinfirst.ui.InvitedPeopleActivity.InvitedPeopleActivity
import com.jsla.kotlinfirst.R
import com.jsla.kotlinfirst.databinding.ActivityMainBinding
import com.jsla.kotlinfirst.helperInterfaces.ActivityInitializer
import com.jsla.kotlinfirst.ui.MeetingsActivity.MeetingsActivity


class MainActivity : AppCompatActivity(),ActivityInitializer<ActivityMainBinding>{

    lateinit var binding : ActivityMainBinding

    //hi repo
    //hello
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =  bind(this,R.layout.activity_main)
        setComponents()
        setListeners()
     }

    override fun setComponents() {
    }

    override fun setListeners() {

        binding.invitedPeopleCV?.setOnClickListener {
            startActivity(Intent(applicationContext, InvitedPeopleActivity::class.java))
        }

        binding.meetingsCV.setOnClickListener{
            startActivity(Intent(applicationContext, MeetingsActivity::class.java))
        }
    }
}
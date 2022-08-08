package com.jsla.kotlinfirst.helperInterfaces

import android.app.Activity
import android.util.Log
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

interface ActivityInitializer<T : ViewDataBinding> {

    fun setComponents()
    fun setListeners()

    fun bind(@NonNull activity : Activity, layoutId : Int) : T{

        val binding : T = DataBindingUtil.setContentView(activity, layoutId)

        return binding
    }

    fun trace(){
        Log.d("Tracer","Arrived here")
    }
}
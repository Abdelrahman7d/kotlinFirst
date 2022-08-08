package com.jsla.kotlinfirst.ui.InvitedPeopleActivity

import androidx.lifecycle.Observer
import com.jsla.kotlinfirst.pogo.PersonInfo

interface MyObserver <T> : Observer<T> {

    override fun onChanged(t: T?)
}
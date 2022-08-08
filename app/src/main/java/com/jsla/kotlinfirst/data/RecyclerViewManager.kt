package com.jsla.kotlinfirst.data

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jsla.kotlinfirst.data.DBHelper
import com.jsla.kotlinfirst.helperInterfaces.OnClick
import com.jsla.kotlinfirst.pogo.PersonInfo
import com.jsla.kotlinfirst.ui.InvitedPeopleActivity.MyItemTouchCallBack
import com.jsla.kotlinfirst.ui.InvitedPeopleActivity.PersonInfoRA

open class RecyclerViewManager (
    open val recyclerView : RecyclerView,
    val context: Context,
    val onClick: OnClick? = null){

    constructor(recyclerViewManager: RecyclerViewManager) : this(
        recyclerViewManager.recyclerView,
        recyclerViewManager.context,
        recyclerViewManager.onClick
    )

    init {
        setAdapter()
        setLayoutManager()
        setTouchHelper()
    }

    protected fun setLayoutManager() {
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    open fun setAdapter() {

        val personInfoRA : PersonInfoRA = PersonInfoRA(context,onClick!!)
        recyclerView.adapter = personInfoRA
    }

    open fun setTouchHelper(){

        val myItemTouchCallBack : MyItemTouchCallBack = MyItemTouchCallBack(recyclerView,0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
        val itemTouchHelper :ItemTouchHelper = ItemTouchHelper(myItemTouchCallBack)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

}
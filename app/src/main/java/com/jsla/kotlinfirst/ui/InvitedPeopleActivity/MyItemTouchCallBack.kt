package com.jsla.kotlinfirst.ui.InvitedPeopleActivity

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.jsla.kotlinfirst.data.DBHelper

class MyItemTouchCallBack (recyclerView: RecyclerView,dragDirs : Int, swipeDirs : Int): ItemTouchHelper.SimpleCallback(dragDirs,swipeDirs) {
    val recyclerView = recyclerView

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {

        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

        deletePerson(viewHolder.adapterPosition)
    }

    private fun deletePerson(position: Int) {

        val db = DBHelper(recyclerView.context, null)
        val personInfoRA : PersonInfoRA = (recyclerView.adapter) as PersonInfoRA

        val id : Long = personInfoRA.deletePerson(position)
        db.deletePerson(id)
    }
}
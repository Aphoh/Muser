package com.aphoh.muser.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import bindView

/**
 * Created by Will on 10/2/15.
 */
class MainSubredditsAdapter : RecyclerView.Adapter<> {


    private inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text : TextView by bindView(0)

    }
}
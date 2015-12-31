package com.aphoh.muser.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import bindView
import com.aphoh.muser.R
import com.aphoh.muser.data.db.model.SongItem
import com.aphoh.muser.util.LogUtil
import com.squareup.picasso.Picasso
import java.util.*

/**
 * Created by Will on 7/11/2015.
 */
public class MainAdapter(val context: Context) : RecyclerView.Adapter<MainAdapter.SongHolder>() {
    var data: ArrayList<SongItem> = ArrayList()
    var log: LogUtil = LogUtil(MainAdapter::class.java.simpleName)
    public var itemClickListener: (View, Int) -> Unit = { v, int -> }
    public var itemLongClickListener: (View, Int) -> Unit = { v, int -> }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SongHolder {
        var v = LayoutInflater.from(parent?.context).inflate(R.layout.row_main, parent, false)
        var holder = SongHolder(v)
        v.setOnClickListener(holder)
        v.setOnLongClickListener(holder)
        return holder
    }

    public fun updateItems(songItems: List<SongItem>) {
        if (!data.isEmpty()) clearSongs()
        data.addAll(songItems)
        notifyItemRangeInserted(0, data.size)
    }

    private fun clearSongs() {
        val size = data.size
        data.clear()
        notifyItemRangeRemoved(0, size)
    }

    public fun invalidateData() {
        clearSongs()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemId(position: Int): Long = data[position].id.hashCode().toLong()

    override fun onBindViewHolder(holder: SongHolder, position: Int) {
        val songItem = data.get(position)
        holder.title.text = songItem.songTitle
        holder.subTitle.text = songItem.artist
        holder.status.text = songItem.score.toString()
        Picasso.with(context).load(songItem.image).error(R.drawable.text_backdrop).fit().centerCrop().into(holder.image)
    }

    public inner class SongHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {
        val title: TextView by bindView(R.id.row_main_title)
        val subTitle: TextView by bindView(R.id.row_main_subtitle)
        val status: TextView by bindView(R.id.row_main_status)
        val image: ImageView by bindView(R.id.row_main_thumb)

        public override fun onClick(v: View) {
            itemClickListener.invoke(v, adapterPosition)
            log.d("Invoked Click")
        }

        public override fun onLongClick(v: View): Boolean {
            itemLongClickListener.invoke(v, adapterPosition)
            return true
        }
    }
}

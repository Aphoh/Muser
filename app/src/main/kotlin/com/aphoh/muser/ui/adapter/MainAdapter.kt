package com.aphoh.muser.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.bindView
import com.aphoh.muser.R
import com.aphoh.muser.data.db.model.SongItem
import com.aphoh.muser.util.LogUtil
import com.squareup.picasso.Picasso
import java.util.ArrayList

/**
 * Created by Will on 7/11/2015.
 */
public class MainAdapter(val context: Context) : RecyclerView.Adapter<MainAdapter.SongHolder>() {
    var data: ArrayList<SongItem> = ArrayList()
    var log: LogUtil = LogUtil(javaClass<MainAdapter>().getSimpleName())
    public var itemClickListener: (View, Int) -> Any = { v, int -> }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SongHolder {
        var v = LayoutInflater.from(parent?.getContext()).inflate(R.layout.row_main, parent, false)
        var holder = SongHolder(v)
        v.setOnClickListener(holder)
        return holder
    }

    public fun addItem(songItem: SongItem) {
        data.add(songItem)
        notifyItemInserted(data.indexOf(songItem))
    }

    public fun removeItem(songItem: SongItem) {
        var index = data.indexOf(songItem)
        data.remove(songItem)
        notifyItemRemoved(index)
    }

    public fun updateItems(songItems: List<SongItem>) {
        if(!data.isEmpty()) {
            var s = data.size() - 1
            for (i in s downTo 0) {
                data.remove(i)
                notifyItemRemoved(i)
            }
        }
        log.d("songItems size: ${songItems.size()}")
        for (i in songItems.indices) {
            data.add(songItems.get(i))
            notifyItemInserted(i)
        }
    }

    override fun getItemCount(): Int {
        return data.size()
    }

    override fun getItemId(position: Int): Long {
        return data.get(position).getId().hashCode().toLong()
    }

    override fun onBindViewHolder(holder: SongHolder, position: Int) {
        val songItem = data.get(position)
        holder.title.setText(songItem.getSongTitle())
        holder.subTitle.setText(songItem.getArtist())
        holder.status.setText(songItem.getScore().toString())
        Picasso.with(context).load(songItem.getImage()).error(R.drawable.fab_background).fit().centerCrop().into(holder.image)
    }

    public inner class SongHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val title: TextView by bindView(R.id.row_main_title)
        val subTitle: TextView by bindView(R.id.row_main_subtitle)
        val status: TextView by bindView(R.id.row_main_status)
        val image: ImageView by bindView(R.id.row_main_thumb)

        public override fun onClick(v: View) {
            itemClickListener.invoke(v, getAdapterPosition())
            log.d("Invoked Click")
        }
    }
}

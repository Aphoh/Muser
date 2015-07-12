package com.aphoh.muser.ui.activitiy

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import butterknife.bindView
import com.aphoh.muser.R
import com.aphoh.muser.base.BaseNucleusActivity
import com.aphoh.muser.data.db.model.SongItem
import com.aphoh.muser.ui.adapter.MainAdapter
import com.aphoh.muser.ui.presenter.MainPresenter
import jp.wasabeef.recyclerview.animators.FlipInLeftYAnimator
import jp.wasabeef.recyclerview.animators.ScaleInAnimator
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter
import jp.wasabeef.recyclerview.animators.adapters.SlideInBottomAnimationAdapter
import nucleus.factory.RequiresPresenter

RequiresPresenter(MainPresenter::class)
public class MainActivity : BaseNucleusActivity<MainPresenter, List<SongItem>>() {

    val recyclerView: RecyclerView by bindView(R.id.recyclerview_main)
    val swipeRefreshLayout: SwipeRefreshLayout by bindView(R.id.swiperefresh_main)
    var view = this
    var adapter: MainAdapter = MainAdapter(this)

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        swipeRefreshLayout setOnRefreshListener{
            adapter.removeItems()
            getPresenter().refresh(this)
        }

        recyclerView setLayoutManager(LinearLayoutManager(this))
        recyclerView setItemAnimator(ScaleInAnimator())
        recyclerView.getItemAnimator().setAddDuration(100)
        recyclerView.getItemAnimator().setRemoveDuration(100)

        adapter.setHasStableIds(true)
        adapter.itemClickListener = { v, position ->
            var songItem = adapter.data.get(position)
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse(songItem.getUrl()))
            if (intent.resolveActivity(getPackageManager()) != null) startActivity(intent)
            Any()
        }
        recyclerView setAdapter ScaleInAnimationAdapter(adapter)
    }

    override fun publish(items: List<SongItem>) {
        if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout setRefreshing(false)
        adapter.removeItems()

        adapter.updateItems(items)
    }

}

package com.aphoh.muser.ui.activitiy

import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.bindView
import com.aphoh.muser.BuildConfig
import com.aphoh.muser.R
import com.aphoh.muser.base.BaseNucleusActivity
import com.aphoh.muser.data.db.model.SongItem
import com.aphoh.muser.ui.adapter.MainAdapter
import com.aphoh.muser.ui.presenter.MainPresenter
import com.aphoh.muser.util.LogUtil
import com.squareup.picasso.Picasso
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter
import nucleus.factory.RequiresPresenter

RequiresPresenter(MainPresenter::class)
public class MainActivity : BaseNucleusActivity<MainPresenter, List<SongItem>>() {
    var log = LogUtil(javaClass<MainActivity>().getSimpleName())

    val recyclerView: RecyclerView by bindView(R.id.recyclerview_main)
    val swipeRefreshLayout: SwipeRefreshLayout by bindView(R.id.swiperefresh_main)

    val mainContent: RelativeLayout by bindView(R.id.relativelayout_main_content)
    val drawerLayout: DrawerLayout by bindView(R.id.drawerlayout_main)
    val songCover: ImageView by bindView(R.id.imageview_main_song)
    val waveForm: ImageView by bindView(R.id.imageview_waveform)
    val titleText: TextView by bindView(R.id.textview_main_song_title)
    val artistText: TextView by bindView(R.id.textview_main_song_artist)

    var view = this
    var adapter: MainAdapter = MainAdapter(this)
    var hasSong = false;

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (BuildConfig.DEBUG) Picasso.with(this).setIndicatorsEnabled(true)

        if (savedInstanceState != null) {
            hasSong = savedInstanceState.getBoolean("hasSong")
        }

        val top = getStatusBarHeight()
        log.d("top: ${top}")
        getToolbar().setBottom(getToolbar().getBottom() + top)

        var params = waveForm.getLayoutParams() as RelativeLayout.LayoutParams
        params.bottomMargin += getNavigationBarHeight(this)
        waveForm.setLayoutParams(params)

        drawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.primary))
        drawerLayout.setDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerClosed(drawerView: View?) {
                swipeRefreshLayout.setOnTouchListener { view, motionEvent ->
                    log.d("OnTouch return false")
                    false
                }
                log.d("SetClickable true")
            }

            override fun onDrawerSlide(drawerView: View?, slideOffset: Float) {

            }

            override fun onDrawerStateChanged(newState: Int) {
                log d("New drawer state: ${newState}")
            }

            override fun onDrawerOpened(drawerView: View?) {
                swipeRefreshLayout.setOnTouchListener { view, motionEvent ->
                    log.d("OnTouch return true")
                    true
                }
            }
        })

        swipeRefreshLayout setOnRefreshListener{
            getPresenter().refresh(this)
        }

        recyclerView setLayoutManager(LinearLayoutManager(this))
        recyclerView setItemAnimator(DefaultItemAnimator())

        adapter.setHasStableIds(true)
        adapter.itemClickListener = { v, position ->
            if (!drawerLayout.isDrawerOpen(Gravity.END)) {
                var songItem = adapter.data.get(position)
                /*var intent = Intent(Intent.ACTION_VIEW, Uri.parse(songItem.getLinkUrl()))
                if (intent.resolveActivity(getPackageManager()) != null) startActivity(intent)*/
                getPresenter().onSongSelected(songItem)
            }
        }
        recyclerView setAdapter ScaleInAnimationAdapter(adapter)
    }

    override fun onResume() {
        super.onResume()
        if (!hasSong) drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putBoolean("hasSong", hasSong)
    }

    public fun publishSongPlay(songItem: SongItem) {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        if (songItem.getStreamUrl() != null) {
            hasSong = true
            if (!drawerLayout.isDrawerOpen(Gravity.END)) {
                drawerLayout.openDrawer(Gravity.END)
            }
            Picasso.with(this)
                    .load(songItem.getImage())
                    .fit()
                    .centerCrop()
                    .into(songCover)
            Picasso.with(this)
                    .load(songItem.getWaveformUrl())
                    .into(waveForm)
            titleText.setText(songItem.getSongTitle())
            artistText.setText(songItem.getArtist())
        }
    }

    override fun publish(items: List<SongItem>) {
        if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout setRefreshing(false)
        if (drawerLayout.isDrawerOpen(Gravity.END)) drawerLayout.closeDrawer(Gravity.END)
        adapter.updateItems(items)
    }

}

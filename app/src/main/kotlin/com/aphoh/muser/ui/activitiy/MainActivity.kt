package com.aphoh.muser.ui.activitiy

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.content.ContextCompat
import android.support.v4.widget.DrawerLayout
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import bindView
import com.aphoh.muser.BuildConfig
import com.aphoh.muser.R
import com.aphoh.muser.base.BaseNucleusActivity
import com.aphoh.muser.data.db.model.SongItem
import com.aphoh.muser.music.MusicView
import com.aphoh.muser.ui.adapter.MainAdapter
import com.aphoh.muser.ui.presenter.MainPresenter
import com.aphoh.muser.ui.view.ControlsView
import com.aphoh.muser.ui.view.PlayPauseView
import com.aphoh.muser.util.LogUtil
import com.squareup.picasso.Picasso
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator
import nucleus.factory.RequiresPresenter
import java.util.*

@RequiresPresenter(MainPresenter::class)
public class MainActivity : BaseNucleusActivity<MainPresenter, List<SongItem>>(), MusicView {
    private final val HAS_SONG = "HAS_SONG"
    private final val NAV_MENU_SELECTED = "NAV_MENU_SELECTED"

    var log = LogUtil(MainActivity::class.java.simpleName)

    val recyclerViewMain: RecyclerView by bindView(R.id.recyclerview_main)
    val swipeRefreshLayout: SwipeRefreshLayout by bindView(R.id.swiperefresh_main)

    val drawerLayout: DrawerLayout by bindView(R.id.drawerlayout_main)

    val drawerContentSongView: RelativeLayout by bindView(R.id.drawercontent_main)
    val songCover: ImageView by bindView(R.id.imageview_main_song)
    val waveForm: ImageView by bindView(R.id.imageview_waveform)
    val titleText: TextView by bindView(R.id.textview_main_song_title)
    val artistText: TextView by bindView(R.id.textview_main_song_artist)
    val playPauseView: PlayPauseView by bindView(R.id.drawer_pause_play_view)

    val navigationView: NavigationView by bindView(R.id.navigation_view)

    var view = this
    var adapter: MainAdapter = MainAdapter(this)
    var hasSong = false;

    override fun getLayoutId(): Int = R.layout.activity_main

    /*
    * Android-specific methods
    * */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Picasso.with(this).isLoggingEnabled = BuildConfig.DEBUG

        var params = waveForm.layoutParams as RelativeLayout.LayoutParams
        params.bottomMargin += getNavigationBarHeight(this)
        waveForm.layoutParams = params

        drawerLayout.setStatusBarBackgroundColor(ContextCompat.getColor(this, R.color.primary))
        drawerLayout.setDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerClosed(drawerView: View?) {
                swipeRefreshLayout.setOnTouchListener { view, motionEvent ->
                    false
                }
                log.d("SetClickable true")
            }

            override fun onDrawerSlide(drawerView: View?, slideOffset: Float) {

            }

            override fun onDrawerStateChanged(newState: Int) {
                log d("New drawer state: $newState")
            }

            override fun onDrawerOpened(drawerView: View?) {
                swipeRefreshLayout.setOnTouchListener { view, motionEvent ->
                    log.d("OnTouch return true")
                    true
                }
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            presenter.refresh(this)
        }

        recyclerViewMain.layoutManager = LinearLayoutManager(this)
        recyclerViewMain.itemAnimator = FadeInUpAnimator()

        adapter.setHasStableIds(true)
        adapter.itemClickListener = { v, position ->
            if (!drawerLayout.isDrawerOpen(Gravity.END)) {
                val afterPositions = ArrayList(adapter.data.subList(position, adapter.data.size))
                presenter.requestPlayAll(afterPositions)
            }
        }

        adapter.itemLongClickListener = { v, position ->
            if (!drawerLayout.isDrawerOpen(Gravity.END)) {
                var songItem = adapter.data.get(position)
                var intent = Intent(Intent.ACTION_VIEW, Uri.parse(songItem.linkUrl))
                if (intent.resolveActivity(packageManager) != null) startActivity(intent)
            }
        }

        recyclerViewMain.adapter = adapter

        /*Consume all touch events so none are passed to the recyclerview below*/
        drawerContentSongView.setOnTouchListener { view, motionEvent -> true }

        navigationView.setNavigationItemSelectedListener {
            presenter.refresh(this)
            swipeRefreshLayout.isRefreshing = true
            drawerLayout.closeDrawer(Gravity.START)
            setToolbarText(it.title)
            true
        }
        navigationView.itemIconTintList = null

        for (i in 0..navigationView.menu.size() - 1) {
            val item = navigationView.menu.getItem(i)
            if (item.isChecked) {
                setToolbarText(item.title)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!hasSong) drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, drawerContentSongView)
    }

    private fun setToolbarText(text: CharSequence) {
        toolbar.title = text
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_play_all -> {
                toast("Play all")
                presenter.requestPlayAll(adapter.data)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        var selected = getSelectedId(navigationView.menu)
        if (selected != null) outState?.putInt(NAV_MENU_SELECTED, selected)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        val selected = savedInstanceState?.getInt(NAV_MENU_SELECTED, -1)
        if (selected != null && selected != -1) navigationView.setCheckedItem(selected)
    }

    /*
    * Presenter-exposed methods
    * */

    public fun invalidateDataset() {
        adapter.invalidateData()
    }

    override fun publish(items: List<SongItem>) {
        swipeRefreshLayout.isRefreshing = false
        if (drawerLayout.isDrawerOpen(Gravity.END)) drawerLayout.closeDrawer(Gravity.END)
        adapter.updateItems(items)
    }

    private fun publishToOpenDrawer(action: () -> Unit) {
        if (!drawerLayout.isDrawerOpen(Gravity.END)) {
            drawerLayout.openDrawer(Gravity.END)
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        }
        action.invoke()
    }

    public fun getSelectedId(menu: Menu): Int? {
        for (i in 0..menu.size() - 1) {
            if (menu.getItem(i).isChecked) return menu.getItem(i).itemId
        }
        return null
    }

    public fun getSubreddit(): String {
        var subreddit = ""
        for (i in 0..navigationView.menu.size() - 1) {
            val item = navigationView.menu.getItem(i);
            if (item.isChecked) subreddit = item.title.toString()
        }
        log.d("Subreddit: " + subreddit)
        return subreddit
    }

    /*
    * MusicView Methods
    * */

    override var id: Int = 0

    override fun publishAlbumArt(albumArt: String) {
        publishToOpenDrawer { Picasso.with(this).load(albumArt).fit().centerCrop().into(songCover) }
    }

    override fun publishSongName(songName: String) {
        publishToOpenDrawer { titleText.text = songName }
    }

    override fun publishSongArtist(songArtist: String?) {
        songArtist?.let {
            publishToOpenDrawer { artistText.text = it }
        }
    }

    override fun publishProgress(seconds: Int) {
        log.d("Secs: " + seconds)
    }

    override fun publishError(error: String) {
        toast(error)
        swipeRefreshLayout.isRefreshing = false
    }

}

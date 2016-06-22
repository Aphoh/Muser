package com.aphoh.muser.ui.activitiy

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.content.ContextCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v4.widget.DrawerLayout
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import bindView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.Theme
import com.aphoh.muser.BuildConfig
import com.aphoh.muser.R
import com.aphoh.muser.base.BaseNucleusActivity
import com.aphoh.muser.data.db.model.SongItem
import com.aphoh.muser.ui.adapter.MainAdapter
import com.aphoh.muser.ui.presenter.MainPresenter
import com.aphoh.muser.ui.view.PlayPauseView
import com.aphoh.muser.util.LogUtil
import com.squareup.picasso.Picasso
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator
import nucleus.factory.RequiresPresenter
import java.util.*

@RequiresPresenter(MainPresenter::class)
class MainActivity : BaseNucleusActivity<MainPresenter, List<SongItem>>() {
    private final val NAV_MENU_SELECTED = "NAV_MENU_SELECTED"

    var log = LogUtil(MainActivity::class.java.simpleName)

    val recyclerViewMain: RecyclerView by bindView(R.id.recyclerview_main)
    val swipeRefreshLayout: SwipeRefreshLayout by bindView(R.id.swiperefresh_main)

    val drawerLayout: DrawerLayout by bindView(R.id.drawerlayout_main)

    val drawerContentSongView: RelativeLayout by bindView(R.id.drawercontent_main)
    val songCover: ImageView by bindView(R.id.imageview_main_song)
    val titleText: TextView by bindView(R.id.textview_main_song_title)
    val artistText: TextView by bindView(R.id.textview_main_song_artist)
    val playPauseView: PlayPauseView by bindView(R.id.drawer_pause_play_view)
    val progressLoading: ProgressBar by bindView(R.id.progress_loading)
    val skipForward: ImageView by bindView(R.id.drawer_fast_forward)
    val skipBack: ImageView by bindView(R.id.drawer_rewind)

    val navigationView: NavigationView by bindView(R.id.navigation_view)

    var canOpenSongDrawer = false

    var view = this
    var adapter: MainAdapter = MainAdapter(this)

    override fun getLayoutId(): Int = R.layout.activity_main

    /*
    * Android-specific methods
    * */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Picasso.with(this).isLoggingEnabled = BuildConfig.DEBUG

        toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_action_navigation_menu)
        toolbar.setNavigationOnClickListener { drawerLayout.openDrawer(Gravity.START) }

        drawerLayout.setStatusBarBackgroundColor(ContextCompat.getColor(this, R.color.primary))
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.END) //By default lock the song drawer view
        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerClosed(drawerView: View?) {
                swipeRefreshLayout.setOnTouchListener { view, motionEvent ->
                    false
                }
                log.d("SetClickable true")
            }

            override fun onDrawerSlide(drawerView: View?, slideOffset: Float) {

            }

            override fun onDrawerStateChanged(newState: Int) {
                if (newState == DrawerLayout.STATE_IDLE) {
                    //Prevent the closing of one drawer to open the other
                    if (drawerLayout.isDrawerOpen(Gravity.START)) {
                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.END)
                    } else if (drawerLayout.isDrawerOpen(Gravity.END)) {
                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.START)
                    } else {
                        //Neither drawer is open, set the behavior as normal
                        if (canOpenSongDrawer) drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.END)
                        //Normal drawer should always be unlocked
                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.START)
                    }
                }
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
            playFrom(position)
            toast("Does it stop services though?")
        }

        adapter.itemLongClickListener = { v, position ->
            showSelector(position)
        }

        recyclerViewMain.adapter = adapter

        /*Consume all touch events so none are passed to the recyclerview below*/
        drawerContentSongView.setOnTouchListener { view, motionEvent -> true }

        navigationView.setNavigationItemSelectedListener {
            drawerLayout.closeDrawer(Gravity.START)
            setToolbarText(it.title)
            presenter.refresh(this, it.title.toString())
            true
        }
        //Allows for colored icons
        navigationView.itemIconTintList = null

        for (i in 0..navigationView.menu.size() - 1) {
            val item = navigationView.menu.getItem(i)
            if (item.isChecked) {
                setToolbarText(item.title)
            }
        }


        //OnClick Listeners
        playPauseView.setOnClickListener {
            if (playPauseView.playing) {
                presenter.requestPause()
            } else {
                presenter.requestPlay()
            }
        }

        skipForward.setOnClickListener { presenter.requestNext() }
        skipBack.setOnClickListener { presenter.requestPrevious() }


    }

    private fun showSelector(position: Int) {
        val songItem = adapter.data[position]
        MaterialDialog.Builder(this)
                .theme(Theme.DARK)
                .title(songItem.songTitle)
                .items(R.array.selection_items)
                .itemsCallback { materialDialog, view, i, charSequence ->
                    when (i) {
                        0 -> {
                            //Play from here
                            playFrom(position)
                        }
                        1 -> {
                            //Open on Soundcloud
                            if (!drawerLayout.isDrawerOpen(Gravity.END)) {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(songItem.linkUrl))
                                if (intent.resolveActivity(packageManager) != null) startActivity(intent)
                            }
                        }
                        2 -> {
                            //Open on Reddit
                            if (!drawerLayout.isDrawerOpen(Gravity.END)) {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(songItem.redditUrl))
                                if (intent.resolveActivity(packageManager) != null) startActivity(intent)
                            }
                        }
                        3 -> {
                            //Share
                            if (!drawerLayout.isDrawerOpen(Gravity.END)) {
                                Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TEXT, songItem.linkUrl)
                                    startActivity(Intent.createChooser(this, "Share link using"))
                                }
                            }
                        }
                    }
                }.show()
    }

    private fun playFrom(position: Int) {
        if (presenter.isPlaying(adapter.data[position])) {
            openDrawer()
        } else if (!drawerLayout.isDrawerOpen(Gravity.END)) {
            val afterPositions = ArrayList(adapter.data.subList(position, adapter.data.size))
            presenter.requestPlayAll(afterPositions)
        }
    }

    fun setToolbarText(text: CharSequence) {
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val selected = getSelectedId(navigationView.menu)
        if (selected != null) outState.putInt(NAV_MENU_SELECTED, selected)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        val selected = savedInstanceState?.getInt(NAV_MENU_SELECTED, -1)
        if (selected != null && selected != -1) navigationView.setCheckedItem(selected)
    }

    private fun closeDrawer() {
        canOpenSongDrawer = false
        drawerLayout.closeDrawer(Gravity.END)
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.END)
    }

    private fun publishToOpenDrawer(action: () -> Unit) {
        if (!drawerLayout.isDrawerOpen(Gravity.END)) openDrawer()
        action.invoke()
    }

    private fun openDrawer() {
        canOpenSongDrawer = true
        drawerLayout.openDrawer(Gravity.END)
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.END)
    }

    /*
    * Presenter-exposed methods
    * */

    fun invalidateDataset() {
        adapter.invalidateData()
    }

    fun setRefreshing(refreshing: Boolean) {
        swipeRefreshLayout.post {
            if (swipeRefreshLayout.isRefreshing != refreshing)
                swipeRefreshLayout.isRefreshing = refreshing
        }
    }

    override fun publish(items: List<SongItem>) {
        if (drawerLayout.isDrawerOpen(Gravity.END)) closeDrawer()
        adapter.updateItems(items.sortedBy { it.score }.asReversed())
    }

    fun publishMetadata(metadata: MediaMetadataCompat) {
        val iconUri = metadata.description.iconUri
        if (iconUri != null) publishAlbumArt(iconUri.toString())
        publishSongName((metadata.description.title ?: "").toString())
        publishSongArtist((metadata.description.subtitle ?: "").toString())
        openDrawer()
    }

    fun publishPlaybackState(state: PlaybackStateCompat) {

        state.position
        when (state.state) {
            PlaybackStateCompat.STATE_PLAYING -> {
                playPauseView.playing = true
                playPauseView.visibility = View.VISIBLE
                progressLoading.visibility = View.INVISIBLE
                openDrawer()
            }
            PlaybackStateCompat.STATE_PAUSED -> {
                playPauseView.playing = false
                playPauseView.visibility = View.VISIBLE
                progressLoading.visibility = View.INVISIBLE
            }
            PlaybackStateCompat.STATE_NONE, PlaybackStateCompat.STATE_STOPPED -> {
                playPauseView.visibility = View.VISIBLE
                playPauseView.playing = false
                progressLoading.visibility = View.INVISIBLE
                closeDrawer()
            }
            PlaybackStateCompat.STATE_BUFFERING -> {
                playPauseView.visibility = View.INVISIBLE
                progressLoading.visibility = View.VISIBLE
            }
        }

        publishProgress(state.position.toInt())

        skipForward.visibility = if (state.actions.and(PlaybackStateCompat.ACTION_SKIP_TO_NEXT) == 0.toLong()) View.INVISIBLE else View.VISIBLE
        skipBack.visibility = if (state.actions.and(PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS) == 0.toLong()) View.INVISIBLE else View.VISIBLE

    }

    fun hasData(): Boolean = adapter.data.isNotEmpty()

    fun getSelectedId(menu: Menu): Int? {
        for (i in 0..menu.size() - 1) {
            if (menu.getItem(i).isChecked) return menu.getItem(i).itemId
        }
        return null
    }

    fun getSubreddit(): String {
        var subreddit = ""
        for (i in 0..navigationView.menu.size() - 1) {
            val item = navigationView.menu.getItem(i);
            if (item.isChecked) subreddit = item.title.toString()
        }
        log.d("Subreddit: " + subreddit)
        return subreddit
    }

    fun publishAlbumArt(albumArt: String) {
        publishToOpenDrawer { Picasso.with(this).load(albumArt).fit().centerCrop().into(songCover) }
    }

    fun publishSongName(songName: String) {
        publishToOpenDrawer { titleText.text = songName }
    }

    fun publishSongArtist(songArtist: String?) {
        songArtist?.let {
            publishToOpenDrawer { artistText.text = it }
        }
    }

    fun publishProgress(seconds: Int) {
        log.d("seconds published: $seconds")
    }

    fun publishError(error: String) {
        toast(error)
    }
}

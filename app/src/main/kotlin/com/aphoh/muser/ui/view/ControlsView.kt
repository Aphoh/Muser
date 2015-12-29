package com.aphoh.muser.ui.view

/**
 * Created by Will on 11/16/15.
 */

public interface ControlsView {
    var playing: Boolean
    public fun removeCallbacks()
    public fun addPlayPauseCallback(func: (playing: Boolean) -> Unit)
    public fun addPreviousNextCallback(func: (state: Int) -> Unit)
    companion object{
        public final val PLAY = 0;
        public final val PAUSE = 1;
    }
}
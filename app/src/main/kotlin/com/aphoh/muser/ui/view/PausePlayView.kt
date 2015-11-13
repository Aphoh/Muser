package com.aphoh.muser.ui.view

import android.content.Context
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.widget.ImageView
import com.aphoh.muser.R

/**
 * Created by Will on 11/5/15.
 */
public class PausePlayView : ImageView {

    val play = lazy { ResourcesCompat.getDrawable(context.resources, R.drawable.ic_play_arrow, context.theme) }
    val pause = lazy { ResourcesCompat.getDrawable(context.resources, R.drawable.ic_pause, context.theme) }

    public var playing = false
        set(value) {
            this.setImageDrawable(if (value) pause.value else play.value)
        }

    constructor(context: Context?) : super(context) {
        this.setImageDrawable(play.value)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

}
package com.aphoh.muser.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.view.View
import com.aphoh.muser.R

/**
 * Created by Will on 11/5/15.
 */
public class PlayPauseView : View{

    val play = lazy { ResourcesCompat.getDrawable(context.resources, R.drawable.ic_play_arrow, context.theme) }
    val pause = lazy { ResourcesCompat.getDrawable(context.resources, R.drawable.ic_pause, context.theme) }


    public var playing = false
        set(value) {
            invalidate()
            field = value
        }

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawDrawable(if (playing) pause.value else play.value)
    }

    private fun Canvas.drawDrawable(drawable: Drawable) {
        drawable.bounds = Rect(0, 0, width, height)
        drawable.draw(this)
    }

}
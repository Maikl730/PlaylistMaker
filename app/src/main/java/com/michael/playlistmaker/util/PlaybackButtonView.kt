package com.michael.playlistmaker.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.graphics.drawable.toBitmap
import com.michael.playlistmaker.R
import kotlin.math.min

class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0,
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private var play = false
    private val imageBitmap1: Bitmap?
    private val imageBitmap2: Bitmap?
    private var imageRect = RectF(0f, 0f, 0f, 0f)

    private val minViewSize = resources.getDimensionPixelSize(
        R.dimen.play_button_view_min_size
    )

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PlaybackButtonView,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {
                imageBitmap1 = getDrawable(R.styleable.PlaybackButtonView_imageResId)?.toBitmap()
                imageBitmap2 = getDrawable(R.styleable.PlaybackButtonView_imageResId2)?.toBitmap()
            } finally {
                recycle()
            }
        }
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) return false

        when (event.action) {

            MotionEvent.ACTION_DOWN -> {
                return true
            }

            MotionEvent.ACTION_UP -> {
                performClick()
                return true
            }
        }
        // return true
        return super.onTouchEvent(event)
    }


    override fun performClick(): Boolean {
        super.performClick()

        play = !play
        invalidate()

        return true
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        imageRect = RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        val bitmap = if (play) imageBitmap1 else imageBitmap2

        bitmap?.let {
            canvas.drawBitmap(it, null, imageRect, null)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val contentWidth = when (widthMode) {

            MeasureSpec.UNSPECIFIED -> minViewSize
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> widthSize

            else -> error("Неизвестный режим ширины ($widthMode)")
        }

        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val contentHeight = when (heightMode) {

            MeasureSpec.UNSPECIFIED -> minViewSize
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> heightSize

            else -> error("Неизвестный режим высоты ($heightMode)")
        }

        val size = min(contentWidth, contentHeight)
        setMeasuredDimension(size, size)
    }

    fun change(state: Boolean) {
        play = !state
        invalidate()
    }

}
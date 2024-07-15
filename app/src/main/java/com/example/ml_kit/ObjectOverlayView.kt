package com.example.ml_kit

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View

class ObjectOverlayView(context: Context?, attrs: AttributeSet?) :
    View(context, attrs) {
    private var boundingBoxes: List<Rect>? = null
    private val paint = Paint()

    init {
        paint.color = -0x10000
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5.0f
    }

    fun setBoundingBoxes(boundingBoxes: List<Rect>?) {
        this.boundingBoxes = boundingBoxes
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (boundingBoxes != null) {
            for (rect in boundingBoxes!!) {
                canvas.drawRect(rect, paint)
            }
        }
    }
}
package com.example.dailykitessentials.providers

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView

interface TouchListener {
    fun onTouch(view: View, position: Int)
    fun onHold(view: View, position: Int)
}

class RowTouchListener(context: Context,
                       list: RecyclerView,
                       private val touchListener: TouchListener?) : RecyclerView.OnItemTouchListener {

    private val gestureDetector: GestureDetector

    init {
        gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return true
            }

            override fun onLongPress(e: MotionEvent) {
                val row = list.findChildViewUnder(e.x, e.y)
                if (row != null && touchListener != null) {
                    touchListener.onHold(row, list.getChildAdapterPosition(row))
                }
            }
        })
    }

    override fun onInterceptTouchEvent(list: RecyclerView, e: MotionEvent): Boolean {
        val child = list.findChildViewUnder(e.x, e.y)
        if (child != null && touchListener != null && gestureDetector.onTouchEvent(e)) {
            touchListener.onTouch(child, list.getChildAdapterPosition(child))
        }

        return false
    }

    override fun onTouchEvent(list: RecyclerView, e: MotionEvent) {}

    override fun onRequestDisallowInterceptTouchEvent(dissallowIntercept: Boolean) {}
}
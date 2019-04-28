package com.example.dailykitessentials.providers

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

@Suppress("NAME_SHADOWING")
open class ListRowDecorator(context: Context, direction: Int, swipeListener: RowSwipeListener) : RecyclerView.ItemDecoration() {
    private val itemSeparator : Drawable?
    private var itemDirection : Int = 0
    private var swipeListener : RowSwipeListener? = null

    init {
        val attributes = context.obtainStyledAttributes(ATTRS)
        itemSeparator = attributes.getDrawable(0)
        attributes.recycle()
        setItemDirection(direction)
        this.swipeListener = swipeListener
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        swipeListener?.onDraw(c)
        super.onDraw(c, parent, state)
    }

    fun drawVerticalList(canvas: Canvas, list: RecyclerView) {
        val left = list.paddingLeft
        val right = list.width - list.paddingRight

        val rowCount = list.childCount
        for (i in 0 until rowCount) {
            val row = list.getChildAt(i)

            val layoutParams = row.layoutParams as RecyclerView.LayoutParams
            val top = layoutParams.bottomMargin + row.bottom
            val bot = top + itemSeparator!!.intrinsicHeight

            itemSeparator.setBounds(left, top, right, bot)
            itemSeparator.draw(canvas)
        }
    }

    fun drawHorizontalList(canvas: Canvas, list: RecyclerView) {
        val top = list.paddingTop
        val bot = list.height - list.paddingBottom

        val rowCount = list.childCount
        for (i in 0 until rowCount) {
            val row = list.getChildAt(i)

            val layoutParams = row.layoutParams as RecyclerView.LayoutParams
            val left = layoutParams.rightMargin + row.right
            val right = left + itemSeparator!!.intrinsicHeight

            itemSeparator.setBounds(left, top, right, bot)
            itemSeparator.draw(canvas)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, list: RecyclerView, state: RecyclerView.State) {
        if (itemDirection == VERTICAL)
            outRect.set(0, 0, 0, itemSeparator!!.intrinsicHeight)
        else
            outRect.set(0, 0, itemSeparator!!.intrinsicWidth, 0)
    }

    override fun onDrawOver(canvas: Canvas, list: RecyclerView, state: RecyclerView.State) {
        if (itemDirection == VERTICAL)
            drawVerticalList(canvas, list)
        else
            drawHorizontalList(canvas, list)
    }

    fun setItemDirection(direction: Int) {
        var direction = direction
        if (direction != HORIZONTAL && direction != VERTICAL)
            direction = HORIZONTAL

        itemDirection = direction
    }

    companion object {
        private val ATTRS = intArrayOf(android.R.attr.listDivider)
        private const val HORIZONTAL = LinearLayoutManager.HORIZONTAL
        private const val VERTICAL = LinearLayoutManager.VERTICAL
    }
}
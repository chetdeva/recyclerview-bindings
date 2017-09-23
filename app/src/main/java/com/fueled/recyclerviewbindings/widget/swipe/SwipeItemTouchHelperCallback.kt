package com.fueled.recyclerviewbindings.widget.swipe

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper

import com.fueled.recyclerviewbindings.util.ViewUtil

/**
 * Reference @link {https://www.learn2crack.com/2016/02/custom-swipe-recyclerview.html}

 * @author chetansachdeva on 26/07/17
 */

class SwipeItemTouchHelperCallback private constructor(dragDirs: Int, swipeDirs: Int) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    private lateinit var drawableLeft: Drawable
    private lateinit var drawableRight: Drawable
    private val paintLeft: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintRight: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private lateinit var onItemSwipeLeftListener: OnItemSwipeListener
    private lateinit var onItemSwipeRightListener: OnItemSwipeListener
    private var swipeEnabled: Boolean = false

    private constructor(builder: Builder) : this(builder.dragDirs, builder.swipeDirs) {
        setPaintColor(paintLeft, builder.bgColorSwipeLeft)
        setPaintColor(paintRight, builder.bgColorSwipeRight)
        drawableLeft = builder.drawableLeft
        drawableRight = builder.drawableRight
        swipeEnabled = builder.swipeEnabled
        onItemSwipeLeftListener = builder.onItemSwipeLeftListener
        onItemSwipeRightListener = builder.onItemSwipeRightListener
    }

    private fun setPaintColor(paint: Paint, color: Int) {
        paint.color = color
    }

    override fun isItemViewSwipeEnabled() = swipeEnabled

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        if (direction == ItemTouchHelper.LEFT) {
            onItemSwipeLeftListener.onItemSwiped(position)
        } else if (direction == ItemTouchHelper.RIGHT) {
            onItemSwipeRightListener.onItemSwiped(position)
        }
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

            val itemView = viewHolder.itemView
            val height = itemView.bottom.toFloat() - itemView.top.toFloat()
            val width = height / 3

            if (dX > 0) {
                val background = RectF(itemView.left.toFloat(), itemView.top.toFloat(), dX, itemView.bottom.toFloat())
                val iconDest = RectF(itemView.left.toFloat() + width, itemView.top.toFloat() + width, itemView.left.toFloat() + 2 * width, itemView.bottom.toFloat() - width)
                c.drawRect(background, paintLeft)
                c.drawBitmap(ViewUtil.getBitmap(drawableLeft), null, iconDest, paintLeft)
            } else {
                val background = RectF(itemView.right.toFloat() + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
                val iconDest = RectF(itemView.right.toFloat() - 2 * width, itemView.top.toFloat() + width, itemView.right.toFloat() - width, itemView.bottom.toFloat() - width)
                c.drawRect(background, paintRight)
                c.drawBitmap(ViewUtil.getBitmap(drawableRight), null, iconDest, paintRight)
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    interface OnItemSwipeListener {
        fun onItemSwiped(position: Int)
    }

    class Builder(internal val dragDirs: Int, internal val swipeDirs: Int) {
        internal lateinit var drawableLeft: Drawable
        internal lateinit var drawableRight: Drawable
        internal var bgColorSwipeLeft: Int = 0
        internal var bgColorSwipeRight: Int = 0
        internal lateinit var onItemSwipeLeftListener: OnItemSwipeListener
        internal lateinit var onItemSwipeRightListener: OnItemSwipeListener
        internal var swipeEnabled: Boolean = false

        fun drawableLeft(value: Drawable): Builder {
            drawableLeft = value
            return this
        }

        fun drawableRight(value: Drawable): Builder {
            drawableRight = value
            return this
        }

        fun bgColorSwipeLeft(value: Int): Builder {
            bgColorSwipeLeft = value
            return this
        }

        fun bgColorSwipeRight(value: Int): Builder {
            bgColorSwipeRight = value
            return this
        }

        fun onItemSwipeLeftListener(value: OnItemSwipeListener): Builder {
            onItemSwipeLeftListener = value
            return this
        }

        fun onItemSwipeRightListener(value: OnItemSwipeListener): Builder {
            onItemSwipeRightListener = value
            return this
        }

        fun swipeEnabled(value: Boolean): Builder {
            swipeEnabled = value
            return this
        }

        fun build() = SwipeItemTouchHelperCallback(this)
    }
}

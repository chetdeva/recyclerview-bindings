package com.fueled.recyclerviewbindings.widget.drag

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper

/**
 * Reference @link {https://medium.com/@ipaulpro/drag-and-swipe-with-recyclerview-b9456d2b1aaf}

 * @author chetansachdeva on 26/07/17
 */

class DragItemTouchHelperCallback private constructor(dragDirs: Int, swipeDirs: Int) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    private var dragEnabled: Boolean = false
    private lateinit var onItemDragListener: OnItemDragListener

    private constructor(builder: Builder) : this(builder.dragDirs, builder.swipeDirs) {
        dragEnabled = builder.dragEnabled
        onItemDragListener = builder.onItemDragListener
    }

    override fun isLongPressDragEnabled() = dragEnabled

    override fun onMove(recyclerView: RecyclerView, source: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        if (source.itemViewType != target.itemViewType) {
            return false
        }
        // Notify the adapter of the move
        onItemDragListener.onItemDragged(source.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        // We only want the active item to change
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            viewHolder!!.itemView.alpha = ALPHA_FULL / 2
            viewHolder.itemView.setBackgroundColor(Color.LTGRAY)
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder) {
        viewHolder.itemView.alpha = ALPHA_FULL
        viewHolder.itemView.setBackgroundColor(0)
        super.clearView(recyclerView, viewHolder)
    }

    interface OnItemDragListener {
        fun onItemDragged(indexFrom: Int, indexTo: Int)
    }

    class Builder(internal val dragDirs: Int, internal val swipeDirs: Int) {
        internal lateinit var onItemDragListener: OnItemDragListener
        internal var dragEnabled: Boolean = false

        fun onItemDragListener(value: OnItemDragListener): Builder {
            onItemDragListener = value
            return this
        }

        fun dragEnabled(value: Boolean): Builder {
            dragEnabled = value
            return this
        }

        fun build() = DragItemTouchHelperCallback(this)
    }

    companion object {
        val ALPHA_FULL = 1.0f
    }
}

package com.fueled.recyclerviewbindings.core

/**
 * Available `ItemDecoration` of `RecyclerView` doesn't allow option of show / hide
 * top / bottom dividers. It also doesn't allow us to specify height / width of divider if it's not a drawable
 * and just space.
 *
 *
 * Created to have control over item decoration.
 * Ability to show / not show divider on top bottom.
 * Created by garima-fueled on 21/07/17.
 */

import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

class SpaceItemDecoration : RecyclerView.ItemDecoration {

    private val space: Int
    private var showFirstDivider = false
    private var showLastDivider = false

    internal var orientation = -1

    constructor(spaceInPx: Int) {
        space = spaceInPx
    }

    constructor(spaceInPx: Int, showFirstDivider: Boolean,
                showLastDivider: Boolean) : this(spaceInPx) {
        this.showFirstDivider = showFirstDivider
        this.showLastDivider = showLastDivider
    }

    constructor(ctx: Context, resId: Int) {
        space = ctx.resources.getDimensionPixelSize(resId)
    }

    constructor(ctx: Context, resId: Int, showFirstDivider: Boolean,
                showLastDivider: Boolean) : this(ctx, resId) {
        this.showFirstDivider = showFirstDivider
        this.showLastDivider = showLastDivider
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                state: RecyclerView.State?) {
        if (space == 0) {
            return
        }

        if (orientation == -1) {
            getOrientation(parent)
        }

        val position = parent.getChildAdapterPosition(view)
        if (position == RecyclerView.NO_POSITION || position == 0 && !showFirstDivider) {
            return
        }

        if (orientation == LinearLayoutManager.VERTICAL) {
            outRect.top = space
            if (showLastDivider && position == state!!.itemCount - 1) {
                outRect.bottom = outRect.top
            }
        } else {
            outRect.left = space
            if (showLastDivider && position == state!!.itemCount - 1) {
                outRect.right = outRect.left
            }
        }
    }

    private fun getOrientation(parent: RecyclerView): Int {
        if (orientation == -1) {
            if (parent.layoutManager is LinearLayoutManager) {
                val layoutManager = parent.layoutManager as LinearLayoutManager
                orientation = layoutManager.orientation
            } else {
                throw IllegalStateException(
                        "SpaceItemDecoration can only be used with a LinearLayoutManager.")
            }
        }
        return orientation
    }
}

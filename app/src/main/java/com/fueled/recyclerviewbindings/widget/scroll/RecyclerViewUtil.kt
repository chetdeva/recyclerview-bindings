package com.fueled.recyclerviewbindings.widget.scroll

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager

/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 *
 * @author chetansachdeva on 28/11/17
 */

object RecyclerViewUtil {

    fun computeLayoutManagerType(layoutManager: RecyclerView.LayoutManager): LayoutManagerType {
        return when (layoutManager) {
            is GridLayoutManager -> LayoutManagerType.GRID
            is LinearLayoutManager -> LayoutManagerType.LINEAR
            is StaggeredGridLayoutManager -> LayoutManagerType.STAGGERED_GRID
            else -> LayoutManagerType.DEFAULT
        }
    }

    fun computeVisibleThreshold(layoutManager: RecyclerView.LayoutManager,
                                layoutManagerType: LayoutManagerType, visibleThreshold: Int): Int =
            when (layoutManagerType) {
                LayoutManagerType.GRID -> (layoutManager as GridLayoutManager).spanCount * visibleThreshold
                LayoutManagerType.STAGGERED_GRID -> (layoutManager as StaggeredGridLayoutManager).spanCount * visibleThreshold
                LayoutManagerType.LINEAR, LayoutManagerType.DEFAULT -> visibleThreshold
            }

    fun getLastVisibleItemPosition(layoutManager: RecyclerView.LayoutManager,
                                   layoutManagerType: LayoutManagerType): Int =
            when (layoutManagerType) {
                LayoutManagerType.LINEAR, LayoutManagerType.GRID -> (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                LayoutManagerType.STAGGERED_GRID -> {
                    val lastVisibleItemPositions = (layoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(null)
                    getStaggeredLayoutLastVisibleItem(lastVisibleItemPositions)
                }
                LayoutManagerType.DEFAULT -> 0
            }

    private fun getStaggeredLayoutLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }
}

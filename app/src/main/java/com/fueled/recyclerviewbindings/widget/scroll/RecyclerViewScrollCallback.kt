package com.fueled.recyclerviewbindings.core

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

class RecyclerViewScrollCallback private constructor(private val visibleThreshold: Int, private val layoutManager: RecyclerView.LayoutManager) : RecyclerView.OnScrollListener() {
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    // The current offset index of data you have loaded
    private var currentPage = 0
    // The total number of items in the dataset after the last load
    private var previousTotalItemCount = 0
    // True if we are still waiting for the last set of data to load.
    private var loading = true
    // Sets the starting page index
    private val startingPageIndex = 0

    private lateinit var onScrolledListener: OnScrolledListener

    constructor(builder: Builder) : this(builder.visibleThreshold, builder.layoutManager) {
        this.onScrolledListener = builder.onScrolledListener
        if (builder.resetLoadingState) {
            resetLoadingState()
        }
    }

    fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
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

    // This happens many times a second during a scroll, so be wary of the code you place here.
    // We are given a few useful parameters to help us work out if we need to load some more data,
    // but first we check if we are waiting for the previous load to finish.
    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        var visibleItemCount = view.getChildCount()
        var totalItemCount = layoutManager.getItemCount()
        var firstVisibleItem = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

        if (loading) {
            if (totalItemCount > previousTotalItemCount) {
                loading = false;
                previousTotalItemCount = totalItemCount;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount)
                <= (firstVisibleItem + visibleThreshold)) {
            // End has been reached

            // Do something
            currentPage++

            onScrolledListener.onScrolledToBottom(currentPage)

            loading = true
        }
    }

    // Call this method whenever performing new searches
    fun resetLoadingState() {
        this.currentPage = this.startingPageIndex
        this.previousTotalItemCount = 0
        this.loading = true
    }

    interface OnScrolledListener {
        fun onScrolledToBottom(page: Int = 1)
    }

    class Builder(internal val visibleThreshold: Int, internal val layoutManager: RecyclerView.LayoutManager) {
        internal lateinit var onScrolledListener: OnScrolledListener
        internal var resetLoadingState: Boolean = false

        fun onScrolledToBottom(value: OnScrolledListener): Builder {
            onScrolledListener = value
            return this
        }

        fun resetLoadingState(value: Boolean): Builder {
            resetLoadingState = value
            return this
        }

        fun build() = RecyclerViewScrollCallback(this)
    }
}
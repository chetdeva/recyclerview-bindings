package com.fueled.recyclerviewbindings.mvp

/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 *
 * @author chetansachdeva on 24/09/17
 */

interface MainHandler {
    fun onPulledToRefresh()

    fun onScrolledToBottom(page: Int)
}

package com.fueled.recyclerviewbindings.mvp

import com.fueled.recyclerviewbindings.core.OnLoadMoreListener
import com.fueled.recyclerviewbindings.model.User
import io.reactivex.Flowable

/**
 * Copyright (c) 2017 Fueled. All rights reserved.

 * @author chetansachdeva on 02/09/17
 */

interface MainContract {

    fun getItemsFromServer(page: Int): Flowable<List<User>>

    interface Presenter : OnLoadMoreListener {
        fun initialize()

        fun terminate()
    }

    interface View {
        fun showProgress(): Boolean

        fun hideProgress(): Boolean

        fun showItems(items: List<User>)

        fun showError(message: String)
    }

    companion object {
        const val LOAD_DELAY_IN_MILLISECONDS: Long = 1000
        const val PAGE_SIZE: Int = 10
    }
}

package com.fueled.recyclerviewbindings.mvp

import com.fueled.recyclerviewbindings.entity.User
import io.reactivex.Flowable

/**
 * Copyright (c) 2017 Fueled. All rights reserved.

 * @author chetansachdeva on 02/09/17
 */

interface MainContract {

    fun getUsersFromServer(page: Int): Flowable<List<User>>

    interface Presenter {
        fun initialize()

        fun getUsers(page: Int)

        fun terminate()
    }

    interface View {
        fun showProgress()

        fun hideProgress()

        fun showItems(items: List<User>)

        fun showError(message: String)
    }

    companion object {
        const val LOAD_DELAY_IN_MILLISECONDS: Long = 1000
        const val PAGE_SIZE: Int = 10
    }
}

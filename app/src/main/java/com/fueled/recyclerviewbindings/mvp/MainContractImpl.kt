package com.fueled.recyclerviewbindings.mvp

import com.fueled.recyclerviewbindings.entity.User
import java.util.concurrent.TimeUnit
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Copyright (c) 2017 Fueled. All rights reserved.

 * @author chetansachdeva on 02/09/17
 */

class MainContractImpl : MainContract {

    /**
     * get items from server with a delay of MainContract.LOAD_DELAY_IN_MILLISECONDS
     */
    override fun getUsersFromServer(page: Int): Flowable<List<User>> {
        return Flowable.just(page)
                .observeOn(AndroidSchedulers.mainThread())
                .delay(MainContract.LOAD_DELAY_IN_MILLISECONDS, TimeUnit.MILLISECONDS)
                .map { p -> getItems(p) }
    }

    /**
     * iterate from start to end where end = page * page_size
     */
    private fun getItems(page: Int): List<User> {
        return (getStartIndex(page) until page * MainContract.PAGE_SIZE)
                .map { makeUser(it) }
    }

    /**
     * make user from id
     */
    private fun makeUser(id: Int) = User(id, "User " + id)

    /**
     * deduce start index of page from page number
     */
    private fun getStartIndex(page: Int) = (page - 1) * MainContract.PAGE_SIZE
}

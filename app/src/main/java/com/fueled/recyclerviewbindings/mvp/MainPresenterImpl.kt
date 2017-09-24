package com.fueled.recyclerviewbindings.mvp

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.PublishProcessor

/**
 * Copyright (c) 2017 Fueled. All rights reserved.

 * @author chetansachdeva on 02/09/17
 */

class MainPresenterImpl(private val view: MainContract.View) : MainContract.Presenter {

    private val contract: MainContract
    private var currentPage: Int = 0
    private val disposables: CompositeDisposable
    private lateinit var paginator: PublishProcessor<Int>

    init {
        contract = MainContractImpl()
        disposables = CompositeDisposable()
        initialize()
    }

    /**
     * initialize all resources
     * set current page to 1
     * create paginator and subscribe to events
     */
    override fun initialize() {
        currentPage = 1
        paginator = PublishProcessor.create()

        val d = paginator.onBackpressureDrop()
                .doOnNext { view.showProgress() }
                .concatMap { contract.getUsersFromServer(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view.hideProgress()
                    view.showItems(it)
                    currentPage++
                }, {
                    view.hideProgress()
                    view.showError(it.localizedMessage)
                })

        disposables.add(d)

        getUsers(currentPage)
    }

    /**
     * called when list is scrolled to its bottom
     * @param page current page (not used)
     */
    override fun getUsers(page: Int) {
        paginator.onNext(currentPage)
    }

    /**
     * terminate presenter and dispose subscriptions
     */
    override fun terminate() {
        disposables.clear()
    }
}

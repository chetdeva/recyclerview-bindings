package com.fueled.recyclerviewbindings.mvp

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Predicate
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
    private var loading: Boolean = false

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
        currentPage = 1                                         // set page = 1
        paginator = PublishProcessor.create()                   // create PublishProcessor

        val d = paginator.onBackpressureDrop()
                .filter { !loading }
                .doOnNext { loading = view.showProgress() }     // loading = true
                .concatMap { contract.getUsersFromServer(it) }  // API call
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    loading = view.hideProgress()               // loading = false
                    view.showItems(it)                          // show items
                    currentPage++                               // increment page
                }, {
                    loading = view.hideProgress()               // loading = false
                    view.showError(it.localizedMessage)         // show error
                })

        disposables.add(d)

        onLoadMore(currentPage)
    }

    /**
     * called when list is scrolled to its bottom
     * @param page current page (not used)
     */
    override fun onLoadMore(page: Int) {
        paginator.onNext(currentPage)                           // increment page if not loading
    }

    /**
     * terminate presenter and dispose subscriptions
     */
    override fun terminate() {
        disposables.clear()                                     // clear disposable onDestroy
    }
}

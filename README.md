## RecyclerView Bindings

RecyclerViewBindings provides a wrapper class RecyclerViewScrollCallback which can be used to add Scroll to Bottom (Endless Scroll) and Pull to Refresh capability to your RecyclerView. You can make use of DataBinding to bind it via XML.

<img src="./README_images/recyclerview_bindings.gif" width="300" height="534"/>

## How to Use

```kotlin
    val callback = RecyclerViewScrollCallback
            .Builder(visibleThreshold, recyclerView.layoutManager)
            .resetLoadingState(resetLoadingState)
            .onScrolledToBottom(onScrolledToBottom)
            .build()

    recyclerView.clearOnScrollListeners()
    recyclerView.addOnScrollListener(callback)
```

## How to Bind

In your `Gradle`

```groovy
    dataBinding {
        enabled = true
    }
```

In your `BindingAdapter`

```kotlin
    /**
     * @param recyclerView  RecyclerView to bind to RecyclerViewScrollCallback
     * @param visibleThreshold  The minimum number of items to have below your current scroll position before loading more.
     * @param resetLoadingState  Reset endless scroll listener when performing a new search
     * @param onScrolledToBottom    OnScrolledListener for RecyclerView scrolled
     */
    @BindingAdapter(value = *arrayOf("visibleThreshold", "resetLoadingState", "onScrolledToBottom"), requireAll = false)
    fun setRecyclerViewScrollCallback(recyclerView: RecyclerView, visibleThreshold: Int, resetLoadingState: Boolean,
                                      onScrolledToBottom: RecyclerViewScrollCallback.OnScrolledListener) {

		... // add addOnScrollListener to RecyclerView using OnScrolledListener as above
    }

    /**
     * @param swipeRefreshLayout Bind swipeRefreshLayout with OnRefreshListener
     * @param onRefresh Listener for onRefresh when swiped
     */
    @BindingAdapter("onPulledToRefresh")
    fun setOnSwipeRefreshListener(swipeRefreshLayout: SwipeRefreshLayout, onPulledToRefresh: Runnable) {
        swipeRefreshLayout.setOnRefreshListener { onPulledToRefresh.run() }
    }
```

In your `XML` file

```xml
    <android.support.v4.widget.SwipeRefreshLayout
        android:id="@+id/srl"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        bind:onPulledToRefresh="@{() -> presenter.initialize()}">

        <android.support.v7.widget.RecyclerView
            android:id="@+id/rv"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:background="@color/grey"
            app:layoutManager="android.support.v7.widget.LinearLayoutManager"
            bind:onScrolledToBottom="@{(page) -> presenter.onLoadMore(page)}"
            bind:resetLoadingState="@{model.resetLoadingState}"
            bind:spaceItemDecoration="@{@dimen/space_very_low}"
            bind:visibleThreshold="@{model.visibleThreshold}"
            tools:listitem="@layout/item_main" />

    </android.support.v4.widget.SwipeRefreshLayout>
```

## Pagination using RxJava (using Subjects)

```kotlin
    /**
     * initialize all resources
     * set current page to 1
     * create paginator and subscribe to events
     */
    override fun initialize() {
        currentPage = 1                                         // set page = 1
        paginator = PublishProcessor.create()                   // create PublishProcessor

        val d = paginator.onBackpressureDrop()
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
        if (loading) return                                     // return if it is still loading
        paginator.onNext(currentPage)                           // increment page if not loading
    }
```

## Library used

Add Android Support Design, RxJava and RxAndroid dependency to your gradle file.

```groovy
    dependencies {
        compile 'com.android.support:design:{latest_version}'
        compile 'io.reactivex.rxjava2:rxandroid:{latest_version}'
        compile 'io.reactivex.rxjava2:rxjava:{latest_version}'
    }
```

## Also try

- [Swipeable RecyclerView](https://github.com/chetdeva/swipeablerecyclerview)
- [Draggable RecyclerView](https://github.com/chetdeva/draggablerecyclerview)

## Reference

- [Endless Scrolling with AdapterViews and RecyclerView, CodePath](https://github.com/codepath/android_guides/wiki/Endless-Scrolling-with-AdapterViews-and-RecyclerView)
- [Pagination with Rx (using Subjects), Kaushik Gopal](https://github.com/kaushikgopal/RxJava-Android-Samples#14-pagination-with-rx-using-subjects)
package com.fueled.recyclerviewbindings.model

import android.databinding.BaseObservable
import android.databinding.Bindable

import com.fueled.recyclerviewbindings.BR

/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 *
 * @author chetansachdeva on 24/09/17
 */

class MainModel : BaseObservable() {

    @get:Bindable
    var resetLoadingState: Boolean = false
        set(resetLoadingState) {
            field = resetLoadingState
            notifyPropertyChanged(BR.resetLoadingState)
        }

    @get:Bindable
    var visibleThreshold: Int = 0
        set(visibleThreshold) {
            field = visibleThreshold
            notifyPropertyChanged(BR.visibleThreshold)
        }
}

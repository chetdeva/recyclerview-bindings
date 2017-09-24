package com.fueled.recyclerviewbindings.model

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.fueled.recyclerviewbindings.BR

/**
 * Copyright (c) 2017 Fueled. All rights reserved.

 * @author chetansachdeva on 15/09/17
 */

class UserModel : BaseObservable() {

    @get:Bindable
    var id: Int = 0
        set(id) {
            field = id
            notifyPropertyChanged(BR.id)
        }

    @get:Bindable
    var name: String = ""
        set(name) {
            field = name
            notifyPropertyChanged(BR.name)
        }
}
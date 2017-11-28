package com.fueled.recyclerviewbindings

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.text.TextUtils

/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 *
 * @author chetansachdeva on 13/10/17
 */

class LoginModel : BaseObservable() {

    @get:Bindable
    var email: String? = null
        set(email) {
            field = email
            notifyPropertyChanged(BR.email)
            isLoginEnabled = isEmailAndPasswordSet
        }
    @get:Bindable
    var password: String? = null
        set(password) {
            field = password
            notifyPropertyChanged(BR.password)
            isLoginEnabled = isEmailAndPasswordSet
        }
    @get:Bindable
    var isLoginEnabled: Boolean = false
        set(loginEnabled) {
            field = loginEnabled
            notifyPropertyChanged(BR.loginEnabled)
        }

    /**
     * checks if email and password fields are set
     *
     * @return isEmailAndPasswordSet
     */
    private val isEmailAndPasswordSet: Boolean
        get() = !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)
}

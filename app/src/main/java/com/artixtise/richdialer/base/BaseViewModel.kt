/*
 * Copyright (c) Ishant Sharma
 * Android Developer
 * ishant.sharma1947@gmail.com
 * 7732993378
 */
package com.artixtise.richdialer.base

import androidx.lifecycle.ViewModel
import java.lang.ref.WeakReference

abstract class BaseViewModel<N> : ViewModel() {
    private var mNavigator: WeakReference<N>? = null
    var navigator: N
        get() = mNavigator!!.get()!!
        set(Navigator) {
            mNavigator = WeakReference<N>(Navigator)
        }


}
package com.pthw.mymovieapp.utils.fastadapter

import android.view.View
import android.view.ViewGroup

interface LayoutFactory {
    fun createView(parent: ViewGroup, type: Int): View
}
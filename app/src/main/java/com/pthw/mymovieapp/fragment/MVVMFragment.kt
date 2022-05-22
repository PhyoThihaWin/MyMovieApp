package com.pthw.mymovieapp.fragment

import android.os.Bundle
import android.view.View
import com.pthw.mymovieapp.base.BaseFragment
import com.pthw.mymovieapp.base.BaseViewModel

abstract class MVVMFragment<VM : BaseViewModel<E>, E> : BaseFragment {

    constructor() : super()

    constructor(contentLayoutId: Int) : super(contentLayoutId)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.filterTouchesWhenObscured = true

        getOrCreateViewModel().eventLD.observe(viewLifecycleOwner, ::renderEvent)
        getOrCreateViewModel().eventErrorLD.observe(viewLifecycleOwner, ::renderError)
    }


    abstract fun getOrCreateViewModel(): VM

    abstract fun renderEvent(event: E)

    abstract fun renderError(message: String)

    protected fun handleError(code: String) {
        showErrorMessage(code)
    }
}
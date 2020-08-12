package me.ostafin.livedatatutorial.base

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {

    protected val disposables: CompositeDisposable = CompositeDisposable()

    private val isInitialized = false

    fun initializeIfNeeded() {
        if (!isInitialized) {
            onInitialize()
        }
    }

    protected abstract fun onInitialize()

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }

}
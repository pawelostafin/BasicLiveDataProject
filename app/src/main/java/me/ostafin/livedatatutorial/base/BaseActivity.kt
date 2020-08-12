package me.ostafin.livedatatutorial.base

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.rxjava3.disposables.CompositeDisposable
import me.ostafin.livedatatutorial.ViewModelFactory
import javax.inject.Inject

abstract class BaseActivity<VM : BaseViewModel> : DaggerAppCompatActivity() {

    protected abstract val viewModelClass: Class<VM>
    protected abstract val layoutResId: Int

    protected val disposables = CompositeDisposable()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    protected val viewModel: VM by lazy {
        ViewModelProvider(this, viewModelFactory).get(viewModelClass)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResId)

        setupView()
        observeViewModel()

        viewModel.initializeIfNeeded()
    }

    protected open fun setupView() {}
    protected open fun observeViewModel() {}

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

}
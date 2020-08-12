package me.ostafin.livedatatutorial.ui

import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.View.*
import android.widget.Toast
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.kotlin.addTo
import kotlinx.android.synthetic.main.activity_main.*
import me.ostafin.livedatatutorial.R
import me.ostafin.livedatatutorial.base.BaseActivity
import me.ostafin.livedatatutorial.ui.adapter.MainAdapter
import me.ostafin.livedatatutorial.util.observeEvent

class MainActivity : BaseActivity<MainViewModel>() {

    override val viewModelClass: Class<MainViewModel>
        get() = MainViewModel::class.java

    override val layoutResId: Int
        get() = R.layout.activity_main

    override fun setupView() {
        super.setupView()

        setupButtons()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        recyclerView.apply {
            setHasFixedSize(true)
            adapter = provideAdapter()
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }

    private fun provideAdapter(): MainAdapter {
        return MainAdapter(
            onBookItemClickedListener = viewModel::bookItemClicked,
            onFavoriteButtonClickedListener = viewModel::favoriteButtonClicked
        )
    }

    private fun setupButtons() {
        textView.setOnClickListener {
            viewModel.navigationButtonClicked()
        }

        button.setOnClickListener {
            viewModel.buttonClicked()
        }
    }

    override fun observeViewModel() {
        super.observeViewModel()

        viewModel.showToast.observe(this, ::showToast)
        viewModel.navigation.observeEvent(this, ::handleNavigation)

        viewModel.progressBarVisibility.observe(this) {
            loadingView.isVisible = it
        }

        viewModel.mainItems.observe(this) {
            (recyclerView.adapter as MainAdapter).submitList(it)
        }

        viewModel.navigationObs
            .subscribe(::handleNavigation)
            .addTo(disposables)
    }

    var View.isVisible: Boolean
        get() = visibility == VISIBLE
        set(value) {
            visibility = if (value) VISIBLE else GONE
        }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun handleNavigation(navigation: MainViewModel.Navigation) {
        when (navigation) {
            MainViewModel.Navigation.MainActivity -> MainActivity.start(this)
        }
    }

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, MainActivity::class.java)
            activity.startActivity(intent)
        }
    }
}
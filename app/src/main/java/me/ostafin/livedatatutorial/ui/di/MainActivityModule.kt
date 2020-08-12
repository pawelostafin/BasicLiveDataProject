package me.ostafin.livedatatutorial.ui.di

import android.app.Activity
import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import me.ostafin.livedatatutorial.di.ViewModelKey
import me.ostafin.livedatatutorial.ui.MainActivity
import me.ostafin.livedatatutorial.ui.MainViewModel

@Module
abstract class MainActivityModule {

    @Binds
    abstract fun mainActivity(activity: MainActivity): Activity

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun mainViewModel(mainViewModel: MainViewModel): ViewModel

    companion object {
        //TODO provides

        @Provides
        fun provideId(): Long {
            return 666L
        }
    }

}
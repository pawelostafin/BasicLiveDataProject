package me.ostafin.livedatatutorial.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import me.ostafin.livedatatutorial.di.scope.ActivityScope
import me.ostafin.livedatatutorial.ui.MainActivity
import me.ostafin.livedatatutorial.ui.di.MainActivityModule

@Module
abstract class ActivityBindingModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun mainActivity(): MainActivity

}
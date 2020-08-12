package me.ostafin.livedatatutorial

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import me.ostafin.livedatatutorial.di.DaggerAppComponent

class MyApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }

}
package me.ostafin.livedatatutorial.di

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import me.ostafin.livedatatutorial.MyApplication
import me.ostafin.livedatatutorial.di.module.ActivityBindingModule
import me.ostafin.livedatatutorial.di.scope.ApplicationScope

@ApplicationScope
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ActivityBindingModule::class
    ]
)
interface AppComponent : AndroidInjector<MyApplication> {

    @Component.Factory
    abstract class Factory : AndroidInjector.Factory<MyApplication>

}
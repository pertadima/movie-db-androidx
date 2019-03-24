package id.co.moviedb.deps

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule
import id.co.moviedb.MainApp
import id.co.moviedb.deps.module.AppModule
import id.co.moviedb.deps.module.NetworkModule
import javax.inject.Singleton

/**
 * Created by pertadima on 22,March,2019
 */

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    NetworkModule::class,
    AppModule::class
])

interface AppComponent : AndroidInjector<MainApp> {
    fun inject(instance: DaggerApplication)
    override fun inject(application: MainApp)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun create(app: Application): Builder
        fun networkModule(networkModule: NetworkModule): Builder
        fun build(): AppComponent
    }
}
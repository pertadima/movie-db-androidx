package id.co.moviedb

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import id.co.moviedb.deps.AppComponent
import id.co.moviedb.deps.DaggerAppComponent
import id.co.moviedb.deps.module.AppModule
import id.co.moviedb.deps.module.NetworkModule

/**
 * Created by pertadima on 22,March,2019
 */


class MainApp : DaggerApplication() {

    lateinit var appComponent: AppComponent

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        appComponent = DaggerAppComponent
            .builder()
            .create(this)
            .networkModule(NetworkModule("https://api.themoviedb.org/3/"))
            .appModule(AppModule(this))
            .build()
        appComponent.inject(this)
        return appComponent
    }
}
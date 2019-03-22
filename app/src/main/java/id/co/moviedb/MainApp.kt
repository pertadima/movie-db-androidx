package id.co.moviedb

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import id.co.moviedb.deps.DaggerAppComponent
import id.co.moviedb.deps.module.NetworkModule

/**
 * Created by pertadima on 22,March,2019
 */


class MainApp : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent
            .builder()
            .create(this)
            .networkModule(NetworkModule("https://inyomanw.com/MasakinMa/"))
            .build()
    }
}
package id.co.moviedb.deps.builder

import dagger.Module
import dagger.android.ContributesAndroidInjector
import id.co.moviedb.deps.ActivityScoped
import id.co.moviedb.ui.detail.DetailMovieActivity
import id.co.moviedb.ui.home.MainActivity

/**
 * Created by pertadima on 24,March,2019
 */

@Module
abstract class ActivityBuilder {
    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity

    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun bindDetailMovieActivity(): DetailMovieActivity
}
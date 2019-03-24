package id.co.moviedb.deps.module

import android.content.Context
import dagger.Module
import dagger.Provides
import id.co.moviedb.commons.DiffCallback
import id.co.moviedb.deps.builder.ActivityBuilder
import id.co.moviedb.networking.ConnectionLiveData
import javax.inject.Singleton

/**
 * Created by pertadima on 24,March,2019
 */

@Module(includes = [
    ActivityBuilder::class
])
class AppModule {
    @Provides
    @Singleton
    fun providesDiffCallback() = DiffCallback()

    @Provides
    @Singleton
    fun provideConnectionLiveData(context: Context) = ConnectionLiveData(context)
}
package id.co.moviedb.deps.module

import android.content.Context
import dagger.Module
import dagger.Provides
import id.co.core.commons.DiffCallback
import id.co.core.network.ConnectionLiveData
import id.co.moviedb.deps.builder.ActivityBuilder
import id.co.moviedb.networking.NetworkAdapter
import id.co.moviedb.networking.NetworkService
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by pertadima on 24,March,2019
 */

@Module(
    includes = [
        ActivityBuilder::class
    ]
)
open class AppModule(private val context: Context) {
    @Provides
    @Singleton
    fun providesDiffCallback() = DiffCallback()

    @Provides
    @Singleton
    fun provideConnectionLiveData() = ConnectionLiveData(context)


    @Provides
    @Singleton
    fun providesNetworkServices(retrofit: Retrofit): NetworkService = retrofit.create(NetworkService::class.java)

    @Provides
    @Singleton
    fun providesNetworkManager(networkService: NetworkService) = NetworkAdapter(networkService)
}
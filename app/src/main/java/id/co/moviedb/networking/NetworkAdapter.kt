package id.co.moviedb.networking

import id.co.moviedb.data.GenreModel
import id.co.moviedb.data.MoviesResponse
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function4
import javax.inject.Inject

/**
 * Created by pertadima on 22,March,2019
 */

class NetworkAdapter @Inject constructor(private val networkService: NetworkService) {
    fun getMoviesGenre(apiKey: String): Single<List<GenreModel>?> {
        return networkService.getMoviesGenre(apiKey)
            .map {
                it.genres
            }
    }

    fun getNowPlayingMovie(apiKey: String): Single<MoviesResponse> {
        return networkService.getNowPlayingMovies(apiKey)
    }

    fun getPopularMovie(apiKey: String): Single<MoviesResponse> {
        return networkService.getPopularMovies(apiKey)
    }

    fun getUpComingMovie(apiKey: String): Single<MoviesResponse> {
        return networkService.getUpComingMovies(apiKey)
    }
}
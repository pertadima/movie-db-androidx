package id.co.moviedb.networking

import id.co.moviedb.data.GenreModel
import id.co.moviedb.data.MoviesResponse
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by pertadima on 22,March,2019
 */

class NetworkAdapter @Inject constructor(private val networkService: NetworkService) {
    fun getMoviesGenre(): Single<List<GenreModel>> {
        return networkService.getMoviesGenre("")
            .map {
                it.genres
            }
    }

    fun getNowPlayingMovie(): Single<MoviesResponse> {
        return networkService.getNowPlayingMovies("")
    }

    fun getPopularMovie(): Single<MoviesResponse> {
        return networkService.getPopularMovies("")
    }

    fun getUpComingMovie(): Single<MoviesResponse> {
        return networkService.getUpComingMovies("")
    }
}
package id.co.moviedb.networking

import id.co.moviedb.data.GenresResponse
import id.co.moviedb.data.MoviesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by pertadima on 22,March,2019
 */

interface NetworkService {
    @GET("genre/movie/list")
    fun getMoviesGenre(@Query("api_key") apiKey: String): Single<GenresResponse>

    @GET("movie/now_playing")
    fun getUpComingMovies(@Query("api_key") apiKey: String): Single<MoviesResponse>

    @GET("movie/now_playing")
    fun getNowPlayingMovies(@Query("api_key") apiKey: String): Single<MoviesResponse>

    @GET("movie/popular")
    fun getPopularMovies(@Query("api_key") apiKey: String): Single<MoviesResponse>
}
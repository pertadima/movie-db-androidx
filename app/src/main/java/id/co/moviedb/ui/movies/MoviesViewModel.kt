package id.co.moviedb.ui.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import id.co.moviedb.base.BaseViewModel
import id.co.moviedb.data.MoviesModel
import id.co.moviedb.data.MoviesResponse
import id.co.moviedb.deps.ActivityScoped
import id.co.moviedb.networking.NetworkAdapter
import javax.inject.Inject

/**
 * Created by pertadima on 26,March,2019
 */

@ActivityScoped
class MoviesViewModel @Inject constructor(private val networkAdapter: NetworkAdapter) : BaseViewModel() {

    private val moviesResponse = MutableLiveData<MoviesResponse?>()
    fun observeMovies(): LiveData<MoviesResponse?> = moviesResponse

    fun fetchNowPlayingMovie(apiKey: String, page: Int) {
        networkAdapter.getNowPlayingMovie(apiKey, page).onResult({
            moviesResponse.postValue(it)
        }, {
            isError.postValue(it)
        })
    }

    fun fetchPopularMovie(apiKey: String, page: Int) {
        networkAdapter.getPopularMovie(apiKey, page).onResult({
            moviesResponse.postValue(it)
        }, {
            isError.postValue(it)
        })
    }

    fun fetchUpComingMovie(apiKey: String, page: Int) {
        networkAdapter.getUpComingMovie(apiKey, page).onResult({
            moviesResponse.postValue(it)
        }, {
            isError.postValue(it)
        })
    }

    fun fetchDiscoverMovie(apiKey: String, page: Int, genre: Int) {
        networkAdapter.getDiscoverMovies(apiKey, page, genre).onResult({
            moviesResponse.postValue(it)
        }, {
            isError.postValue(it)
        })
    }
}
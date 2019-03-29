package id.co.moviedb.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import id.co.moviedb.base.BaseViewModel
import id.co.moviedb.data.GenreModel
import id.co.moviedb.data.MoviesModel
import id.co.moviedb.deps.ActivityScoped
import id.co.moviedb.networking.NetworkAdapter
import javax.inject.Inject

/**
 * Created by pertadima on 24,March,2019
 */

@ActivityScoped
class MainViewModel @Inject constructor(private val networkAdapter: NetworkAdapter) : BaseViewModel() {

    companion object {
        const val TAKE_DATA = 10
    }

    private val listGenre = MutableLiveData<List<GenreModel>?>()
    private val listNowPlayingMovie = MutableLiveData<List<MoviesModel>?>()
    private val listUpComingMovie = MutableLiveData<List<MoviesModel>?>()
    private val listPopularMovie = MutableLiveData<List<MoviesModel>?>()

    fun observeGenreMovies(): LiveData<List<GenreModel>?> = listGenre
    fun observeNowPlayingMovie(): LiveData<List<MoviesModel>?> = listNowPlayingMovie
    fun observeUpComingMovie(): LiveData<List<MoviesModel>?> = listUpComingMovie
    fun observePopularMovie(): LiveData<List<MoviesModel>?> = listPopularMovie

    private val isLoadingGenre = MutableLiveData<Boolean>()
    fun observeLoadingGenre(): LiveData<Boolean> = isLoadingGenre

    private val isLoadingPopularMovie = MutableLiveData<Boolean>()
    fun observeLoadingPopularMovie(): LiveData<Boolean> = isLoadingPopularMovie

    private val isLoadingNowPlayingMovie = MutableLiveData<Boolean>()
    fun observeLoadingNowPlayingMovie(): LiveData<Boolean> = isLoadingNowPlayingMovie

    private val isLoadingUpComingMovie = MutableLiveData<Boolean>()
    fun observeLoadingUpComingMovie(): LiveData<Boolean> = isLoadingUpComingMovie

    init {
        listGenre.value = listOf()
        listNowPlayingMovie.value = listOf()
        listUpComingMovie.value = listOf()
        listPopularMovie.value = listOf()
    }

    fun fetchHome(apiKey: String, page: Int) {
        fetchGenre(apiKey)
        fetchPopularMovie(apiKey, page)
        fetchNowPlayingMovie(apiKey, page)
        fetchUpComingMovie(apiKey, page)
    }

    private fun fetchGenre(apiKey: String) {
        isLoadingGenre.postValue(true)
        networkAdapter.getMoviesGenre(apiKey).onResult({
            listGenre.postValue(it)
            isLoadingGenre.postValue(false)
        }, {
            isError.postValue(it)
            isLoadingGenre.postValue(false)
        })
    }

    private fun fetchPopularMovie(apiKey: String, page: Int) {
        isLoadingPopularMovie.postValue(true)
        networkAdapter.getPopularMovie(apiKey, page).onResult({
            listPopularMovie.postValue(it.results?.take(TAKE_DATA))
            isLoadingPopularMovie.postValue(false)
        }, {
            isError.postValue(it)
            isLoadingPopularMovie.postValue(false)
        })
    }

    private fun fetchNowPlayingMovie(apiKey: String, page: Int) {
        isLoadingNowPlayingMovie.postValue(true)
        networkAdapter.getNowPlayingMovie(apiKey, page).onResult({
            listNowPlayingMovie.postValue(it.results)
            isLoadingNowPlayingMovie.postValue(false)
        }, {
            isError.postValue(it)
            isLoadingNowPlayingMovie.postValue(false)
        })
    }

    private fun fetchUpComingMovie(apiKey: String, page: Int) {
        isLoadingUpComingMovie.postValue(true)
        networkAdapter.getUpComingMovie(apiKey, page).onResult({
            listUpComingMovie.postValue(it.results)
            isLoadingUpComingMovie.postValue(false)
        }, {
            isError.postValue(it)
            isLoadingUpComingMovie.postValue(false)
        })
    }
}
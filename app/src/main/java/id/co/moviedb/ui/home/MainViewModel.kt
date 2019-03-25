package id.co.moviedb.ui.home

import android.util.Log
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

    private val listGenre = MutableLiveData<List<GenreModel>?>()
    private val listNowPlayingMovie = MutableLiveData<List<MoviesModel>?>()
    private val listUpComingMovie = MutableLiveData<List<MoviesModel>?>()
    private val listPopularMovie = MutableLiveData<List<MoviesModel>?>()

    fun observeGenreMovies(): LiveData<List<GenreModel>?> = listGenre
    fun observeNowPlayingMovie(): LiveData<List<MoviesModel>?> = listNowPlayingMovie
    fun observeUpComingMovie(): LiveData<List<MoviesModel>?> = listUpComingMovie
    fun observePopularMovie(): LiveData<List<MoviesModel>?> = listPopularMovie

    private val isEmptyGenre = MutableLiveData<Boolean>()
    fun observeEmptyGenre(): LiveData<Boolean> = isEmptyData

    private val isLoadingGenre = MutableLiveData<Boolean>()
    fun observeLoadingGenre(): LiveData<Boolean> = isLoadingGenre

    private val isEmptyPopularMovie = MutableLiveData<Boolean>()
    fun observeEmptyPopularMovie(): LiveData<Boolean> = isEmptyPopularMovie

    private val isLoadingPopularMovie = MutableLiveData<Boolean>()
    fun observeLoadingPopularMovie(): LiveData<Boolean> = isLoadingPopularMovie

    private val isEmptyNowPlayingMovie = MutableLiveData<Boolean>()
    fun observeLoadingEmptyNowPlayingMovie(): LiveData<Boolean> = isEmptyNowPlayingMovie

    private val isLoadingNowPlayingMovie = MutableLiveData<Boolean>()
    fun observeLoadingNowPlayingMovie(): LiveData<Boolean> = isLoadingNowPlayingMovie

    private val isEmptyUpComingMovie = MutableLiveData<Boolean>()
    fun observeEmptyUpComingMovie(): LiveData<Boolean> = isEmptyUpComingMovie

    private val isLoadingUpComingMovie = MutableLiveData<Boolean>()
    fun observeLoadingUpComingMovie(): LiveData<Boolean> = isLoadingUpComingMovie

    init {
        listGenre.value = listOf()
        listNowPlayingMovie.value = listOf()
        listUpComingMovie.value = listOf()
        listPopularMovie.value = listOf()
    }

    fun fetchHome(apiKey: String) {
        fetchGenre(apiKey)
        fetchPopularMovie(apiKey)
        fetchNowPlayingMovie(apiKey)
        fetchUpComingMovie(apiKey)
    }

    private fun fetchGenre(apiKey: String) {
        isLoadingGenre.postValue(true)
        networkAdapter.getMoviesGenre(apiKey).onResult({
            isEmptyGenre.postValue(it.isNullOrEmpty())
            listGenre.postValue(it)
            isLoadingGenre.postValue(false)
        },{
            isError.postValue(it)
            isLoadingGenre.postValue(false)
        })
    }

    private fun fetchPopularMovie(apiKey: String) {
        isLoadingPopularMovie.postValue(true)
        networkAdapter.getPopularMovie(apiKey).onResult({
            isEmptyPopularMovie.postValue(it.results.isNullOrEmpty())
            listPopularMovie.postValue(it.results?.take(6))
            isLoadingPopularMovie.postValue(false)
        }, {
            isError.postValue(it)
            isLoadingPopularMovie.postValue(false)
        })
    }

    private fun fetchNowPlayingMovie(apiKey: String) {
        isLoadingNowPlayingMovie.postValue(true)
        networkAdapter.getNowPlayingMovie(apiKey).onResult({
            isEmptyNowPlayingMovie.postValue(it.results.isNullOrEmpty())
            listNowPlayingMovie.postValue(it.results)
            isLoadingNowPlayingMovie.postValue(false)
        }, {
            isError.postValue(it)
            isLoadingNowPlayingMovie.postValue(false)
        })
    }

    private fun fetchUpComingMovie(apiKey: String) {
        isLoadingUpComingMovie.postValue(true)
        networkAdapter.getUpComingMovie(apiKey).onResult({
            isEmptyUpComingMovie.postValue(it.results.isNullOrEmpty())
            listUpComingMovie.postValue(it.results)
            isLoadingUpComingMovie.postValue(false)
        }, {
            isError.postValue(it)
            isLoadingUpComingMovie.postValue(false)
        })
    }
}
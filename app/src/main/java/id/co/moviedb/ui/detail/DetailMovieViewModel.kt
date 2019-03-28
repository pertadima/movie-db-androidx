package id.co.moviedb.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import id.co.moviedb.base.BaseViewModel
import id.co.moviedb.data.DetailMoviesResponse
import id.co.moviedb.deps.ActivityScoped
import id.co.moviedb.networking.NetworkAdapter
import javax.inject.Inject

/**
 * Created by pertadima on 26,March,2019
 */

@ActivityScoped
class DetailMovieViewModel @Inject constructor(private val networkAdapter: NetworkAdapter) : BaseViewModel() {
    private val dataDetail = MutableLiveData<DetailMoviesResponse>()
    fun observeDetailMovie(): LiveData<DetailMoviesResponse?> = dataDetail


    fun fetchDetailMovies(id: Int, apiKey: String) {
        networkAdapter.getDetailMovies(id, apiKey).onResult({
            dataDetail.postValue(it)
        }, {
            isError.postValue(it)
        })
    }
}
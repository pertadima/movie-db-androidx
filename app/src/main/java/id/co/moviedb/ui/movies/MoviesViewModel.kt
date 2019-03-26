package id.co.moviedb.ui.movies

import id.co.moviedb.base.BaseViewModel
import id.co.moviedb.deps.ActivityScoped
import id.co.moviedb.networking.NetworkAdapter
import javax.inject.Inject

/**
 * Created by pertadima on 26,March,2019
 */

@ActivityScoped
class MoviesViewModel @Inject constructor(networkAdapter: NetworkAdapter): BaseViewModel() {

}
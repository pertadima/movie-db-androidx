package id.co.moviedb.ui.home

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.*
import id.co.moviedb.R
import id.co.moviedb.base.BaseActivity
import id.co.moviedb.commons.DiffCallback
import id.co.moviedb.commons.GeneralRecyclerView
import id.co.moviedb.commons.goneIf
import id.co.moviedb.commons.loadImage
import id.co.moviedb.data.MoviesModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.default_toolbar.view.*
import kotlinx.android.synthetic.main.viewholder_now_playing_movies.view.*
import kotlinx.android.synthetic.main.viewholder_popular_movies.view.*
import javax.inject.Inject


class MainActivity : BaseActivity() {
    @Inject
    lateinit var mainViewModel: MainViewModel

    @Inject
    lateinit var diffCallback: DiffCallback

    private val nowPlayingAdapter by lazy {
        GeneralRecyclerView<MoviesModel>(
            diffCallback = diffCallback,
            holderResId = R.layout.viewholder_now_playing_movies,
            onBind = { moviesModel, view ->
                setupNowPlayingMovie(moviesModel, view)
            },
            itemListener = { moviesModel, _, _ ->

            }
        )
    }

    private val popularMovieAdapter by lazy {
        GeneralRecyclerView<MoviesModel>(
            diffCallback = diffCallback,
            holderResId = R.layout.viewholder_popular_movies,
            onBind = { moviesModel, view ->
                setupPopularMovie(moviesModel, view)
            },
            itemListener = { moviesModel, _, _ ->

            }
        )
    }

    override fun onSetupLayout(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_main)
        setupToolbarProperties(
            toolbar as Toolbar,
            toolbar.toolbar_title,
            R.string.app_name
        )
    }

    override fun onViewReady(savedInstanceState: Bundle?) {
        initRecyclerView()
        observeState()
    }

    private fun observeState() {
        mainViewModel.observeGenreMovies().onResult {

        }

        mainViewModel.observeNowPlayingMovie().onResult { result ->
            result?.let {
                nowPlayingAdapter.setData(it)
            }
        }

        mainViewModel.observeUpComingMovie().onResult {

        }

        mainViewModel.observePopularMovie().onResult { result ->
            result?.let {
                popularMovieAdapter.setData(it)
            }

        }

        mainViewModel.observeLoadingNowPlayingMovie().onResult {
            pb_now_playing.goneIf(it)
        }

        mainViewModel.observeLoadingPopularMovie().onResult {
            pb_popular_movie.goneIf(it)
        }

        mainViewModel.fetchHome(getString(R.string.api_key_movie_db))
    }

    private fun initRecyclerView() {
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(rv_now_playing)

        with(rv_now_playing) {
            adapter = nowPlayingAdapter
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayout.HORIZONTAL, false)
            isNestedScrollingEnabled = false
        }

        with(rv_popular_movie) {
            adapter = popularMovieAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            isNestedScrollingEnabled = false
        }
    }

    private fun setupNowPlayingMovie(model: MoviesModel, view: View) {
        with(view) {
            tv_movie_title.text = model.title
            tv_rating.text = model.voteAverage.toString()
            img_thumnail.loadImage(getString(R.string.image_url, model.posterPath))
        }
    }

    private fun setupPopularMovie(model: MoviesModel,view: View) {
        with(view) {
            img_thumnail_popular.loadImage(getString(R.string.image_url, model.posterPath))
            tv_movie_title_popular.text = model.title
            tv_desc.text = model.overview
            tv_rating_popular.text = model.voteAverage.toString()
        }
    }
}

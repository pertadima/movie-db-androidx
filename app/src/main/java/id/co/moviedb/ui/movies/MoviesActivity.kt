package id.co.moviedb.ui.movies

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import id.co.moviedb.R
import id.co.moviedb.base.BaseActivity
import id.co.moviedb.commons.DiffCallback
import id.co.moviedb.commons.GeneralRecyclerView
import id.co.moviedb.commons.goneIf
import id.co.moviedb.commons.loadImage
import id.co.moviedb.data.MoviesModel
import id.co.moviedb.ui.detail.DetailMovieActivity
import id.co.moviedb.ui.home.MainActivity.Companion.GENRE_ID_TAG
import id.co.moviedb.ui.home.MainActivity.Companion.GENRE_NAME_TAG
import id.co.moviedb.ui.home.MainActivity.Companion.MOVIES_ENUM_TAG
import id.co.moviedb.ui.home.MainActivity.Companion.MOVIES_ID_TAG
import id.co.moviedb.commons.SpacesItemDecoration
import kotlinx.android.synthetic.main.activity_movies.*
import kotlinx.android.synthetic.main.default_toolbar.view.*
import kotlinx.android.synthetic.main.viewholder_movies.view.*
import javax.inject.Inject

/**
 * Created by pertadima on 26,March,2019
 */

class MoviesActivity : BaseActivity() {
    @Inject
    lateinit var moviesViewModel: MoviesViewModel

    @Inject
    lateinit var diffCallback: DiffCallback

    lateinit var titleToolbar: String

    private var currentPage: Int = 1
    private var maxPage: Int = 1

    private val moviesAdapter by lazy {
        GeneralRecyclerView<MoviesModel>(
            diffCallback = diffCallback,
            holderResId = R.layout.viewholder_movies,
            onBind = { moviesModel, view ->
                setupMovies(moviesModel, view)
            },
            itemListener = { moviesModel, _, _ ->
                startActivity(Intent(this@MoviesActivity, DetailMovieActivity::class.java).apply {
                    putExtra(MOVIES_ID_TAG, moviesModel.id)
                })
            }
        )
    }

    override fun onSetupLayout(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_movies)
        setupToolbarPropertiesWithBackButton(
            toolbar as Toolbar,
            toolbar.toolbar_title,
            R.string.empty_string
        )
    }

    override fun onViewReady(savedInstanceState: Bundle?) {
        val enumMovie: MoviesEnum = intent.getSerializableExtra(MOVIES_ENUM_TAG) as MoviesEnum
        observeViewModel(enumMovie)
        with(rv_movies) {
            adapter = moviesAdapter
            layoutManager = GridLayoutManager(this@MoviesActivity, 2)
            addItemDecoration(SpacesItemDecoration(2, 10, false))
        }
    }

    private fun observeViewModel(enumMovie: MoviesEnum) {
        with(moviesViewModel) {
            observeMovies().onResult { response ->
                response?.results?.let {
                    moviesAdapter.setData(it)
                }
            }

            observeLoading().onResult {
                pb_circular.goneIf(it)
            }

            when (enumMovie) {
                MoviesEnum.BYNOWPLAYING -> {
                    titleToolbar = getString(R.string.now_playing_text)
                    boundNetwork {
                        if (it) {
                            fetchNowPlayingMovie(getString(R.string.api_key_movie_db), currentPage)
                        }
                    }
                }
                MoviesEnum.BYPOPULAR -> {
                    titleToolbar = getString(R.string.popular_movie_text)
                    boundNetwork {
                        if (it) {
                            fetchPopularMovie(getString(R.string.api_key_movie_db), currentPage)
                        }
                    }
                }
                MoviesEnum.BYUPCOMING -> {
                    titleToolbar = getString(R.string.upcoming_movies_text)
                    boundNetwork {
                        if (it) {
                            fetchUpComingMovie(getString(R.string.api_key_movie_db), currentPage)
                        }
                    }
                }
                MoviesEnum.BYGENRE -> {
                    titleToolbar = intent.getStringExtra(GENRE_NAME_TAG)
                    boundNetwork {
                        if (it) {
                            fetchDiscoverMovie(
                                getString(R.string.api_key_movie_db), currentPage,
                                intent.getIntExtra(GENRE_ID_TAG, 0)
                            )
                        }
                    }
                }
            }
            titleToolbar.changeToolbarTitle(toolbar.toolbar_title)
        }
    }

    private fun setupMovies(moviesModel: MoviesModel, view: View) {
        with(view) {
            img_thumnail.loadImage(getString(R.string.image_url, moviesModel.posterPath))
            tv_movie_title.text = moviesModel.title
        }
    }
}
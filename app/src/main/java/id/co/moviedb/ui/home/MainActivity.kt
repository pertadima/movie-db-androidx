package id.co.moviedb.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import id.co.core.commons.DiffCallback
import id.co.core.commons.GeneralRecyclerView
import id.co.core.commons.SpacesItemDecoration
import id.co.core.commons.goneIf
import id.co.core.commons.loadImage
import id.co.moviedb.R
import id.co.moviedb.base.BaseActivity
import id.co.moviedb.data.GenreModel
import id.co.moviedb.data.MoviesModel
import id.co.moviedb.ui.detail.DetailMovieActivity
import id.co.moviedb.ui.movies.MoviesActivity
import id.co.moviedb.ui.movies.MoviesEnum
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.default_toolbar.view.*
import kotlinx.android.synthetic.main.viewholder_genres.view.*
import kotlinx.android.synthetic.main.viewholder_now_playing_movies.view.*
import kotlinx.android.synthetic.main.viewholder_popular_movies.view.*
import kotlinx.android.synthetic.main.viewholder_upcoming_movies.view.*
import javax.inject.Inject


class MainActivity : BaseActivity() {
    companion object {
        const val MOVIES_ID_TAG = "movies_id"
        const val MOVIES_ENUM_TAG = "movies_enum"
        const val GENRE_ID_TAG = "genre_id"
        const val GENRE_NAME_TAG = "genre_name"
        const val STATIC_PAGE = 1
        const val SPAN_COUNT = 2
        const val SPAN_WIDTH = 10
    }

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
                startActivity(Intent(this@MainActivity, DetailMovieActivity::class.java).apply {
                    putExtra(MOVIES_ID_TAG, moviesModel.id)
                })
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
                startActivity(Intent(this@MainActivity, DetailMovieActivity::class.java).apply {
                    putExtra(MOVIES_ID_TAG, moviesModel.id)
                })
            }
        )
    }

    private val genresAdapter by lazy {
        GeneralRecyclerView<GenreModel>(
            diffCallback = diffCallback,
            holderResId = R.layout.viewholder_genres,
            onBind = { genreModel, view ->
                setupGenresMovie(genreModel, view)
            },
            itemListener = { genreModel, _, _ ->
                startActivity(Intent(this@MainActivity, MoviesActivity::class.java).apply {
                    putExtra(MOVIES_ENUM_TAG, MoviesEnum.BYGENRE)
                    putExtra(GENRE_ID_TAG, genreModel.id)
                    putExtra(GENRE_NAME_TAG, genreModel.name)
                })
            }
        )
    }

    private val upComingMoviesAdapter by lazy {
        GeneralRecyclerView<MoviesModel>(
            diffCallback = diffCallback,
            holderResId = R.layout.viewholder_upcoming_movies,
            onBind = { moviesModel, view ->
                setUpUpComingMovie(moviesModel, view)
            },
            itemListener = { moviesModel, _, _ ->
                startActivity(Intent(this@MainActivity, DetailMovieActivity::class.java).apply {
                    putExtra(MOVIES_ID_TAG, moviesModel.id)
                })
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
        onClickSeeAll()
    }

    private fun observeState() {
        with(mainViewModel) {
            observeGenreMovies().onResult { result ->
                result?.let {
                    genresAdapter.setData(it)
                }
            }

            observeNowPlayingMovie().onResult { result ->
                result?.let {
                    nowPlayingAdapter.setData(it)
                }
            }

            observeUpComingMovie().onResult { result ->
                result?.let {
                    upComingMoviesAdapter.setData(it)
                }
            }

            observePopularMovie().onResult { result ->
                result?.let {
                    popularMovieAdapter.setData(it)
                }

            }

            observeError().onResult {

            }

            observeLoadingNowPlayingMovie().onResult {
                pb_now_playing.goneIf(it)
            }

            observeLoadingPopularMovie().onResult {
                pb_popular_movie.goneIf(it)
            }

            observeLoadingGenre().onResult {
                pb_genres.goneIf(it)
            }

            observeLoadingUpComingMovie().onResult {
                pb_upcoming_movies.goneIf(it)
            }

            boundNetwork {
                connectionView(it)
                if (it) {
                    fetchHome(getString(R.string.api_key_movie_db), STATIC_PAGE)
                }
            }
        }
    }

    private fun initRecyclerView() {
        val snapHelper = LinearSnapHelper()
        val upComingSnapHelper = LinearSnapHelper()

        with(rv_now_playing) {
            adapter = nowPlayingAdapter
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayout.HORIZONTAL, false)
            isNestedScrollingEnabled = false
            snapHelper.attachToRecyclerView(this)
        }

        with(rv_popular_movie) {
            adapter = popularMovieAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            isNestedScrollingEnabled = false
        }

        with(rv_genres) {
            adapter = genresAdapter
            layoutManager = GridLayoutManager(this@MainActivity, SPAN_COUNT)
            isNestedScrollingEnabled = false
            addItemDecoration(SpacesItemDecoration(SPAN_COUNT, SPAN_WIDTH, false))
        }

        with(rv_upcoming) {
            adapter = upComingMoviesAdapter
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayout.HORIZONTAL, false)
            isNestedScrollingEnabled = false
            upComingSnapHelper.attachToRecyclerView(this)
        }
    }

    private fun onClickSeeAll() {
        tv_see_all_now_playing.setOnClickListener {
            toMoviesActivity(MoviesEnum.BYNOWPLAYING)
        }

        tv_see_all_popular_movie.setOnClickListener {
            toMoviesActivity(MoviesEnum.BYPOPULAR)
        }

        tv_see_all_upcoming_movie.setOnClickListener {
            toMoviesActivity(MoviesEnum.BYUPCOMING)
        }
    }

    private fun toMoviesActivity(moviesEnum: MoviesEnum) {
        startActivity(Intent(this@MainActivity, MoviesActivity::class.java).apply {
            putExtra(MOVIES_ENUM_TAG, moviesEnum)
        })
    }

    private fun setupNowPlayingMovie(model: MoviesModel, view: View) {
        with(view) {
            tv_movie_title.text = model.title
            tv_rating.text = model.voteAverage.toString()
            img_thumnail.loadImage(getString(R.string.image_url, model.posterPath))
        }
    }

    private fun setupPopularMovie(model: MoviesModel, view: View) {
        with(view) {
            img_thumnail_popular.loadImage(getString(R.string.image_url, model.posterPath))
            tv_movie_title_popular.text = model.title
            tv_desc.text = model.overview
            tv_rating_popular.text = model.voteAverage.toString()
        }
    }

    private fun setupGenresMovie(model: GenreModel, view: View) {
        with(view) {
            tv_genre_name.text = model.name
        }
    }

    private fun setUpUpComingMovie(model: MoviesModel, view: View) {
        with(view) {
            img_thumnail_upcoming.loadImage(getString(R.string.image_url, model.posterPath))
            tv_movie_title_upcoming.text = model.title
        }
    }

    private fun connectionView(isConnect: Boolean) {
        nested_view.goneIf(isConnect)
        no_internet.goneIf(!isConnect)
    }
}

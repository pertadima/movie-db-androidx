package id.co.moviedb.ui.movies

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.co.core.commons.DiffCallback
import id.co.core.commons.GeneralRecyclerView
import id.co.core.commons.SpacesItemDecoration
import id.co.core.commons.goneIf
import id.co.core.commons.loadImage
import id.co.moviedb.R
import id.co.moviedb.base.BaseActivity
import id.co.moviedb.data.MoviesModel
import id.co.moviedb.ui.detail.DetailMovieActivity
import id.co.moviedb.ui.home.MainActivity.Companion.GENRE_ID_TAG
import id.co.moviedb.ui.home.MainActivity.Companion.GENRE_NAME_TAG
import id.co.moviedb.ui.home.MainActivity.Companion.MOVIES_ENUM_TAG
import id.co.moviedb.ui.home.MainActivity.Companion.MOVIES_ID_TAG
import kotlinx.android.synthetic.main.activity_movies.*
import kotlinx.android.synthetic.main.default_toolbar.view.*
import kotlinx.android.synthetic.main.viewholder_movies.view.*
import javax.inject.Inject


/**
 * Created by pertadima on 26,March,2019
 */

class MoviesActivity : BaseActivity() {
    companion object {
        const val SPAN_COUNT = 2
        const val SPAN_WIDTH = 10
        const val DEFAULT_GENRE_ID = 0
        const val DEFAULT_PAGE = 1
    }

    @Inject
    lateinit var moviesViewModel: MoviesViewModel

    @Inject
    lateinit var diffCallback: DiffCallback

    lateinit var titleToolbar: String

    private var currentPage: Int = 1
    private var maxPage: Int = 1
    private var totalRecords: Int = 0
    private var totalCurrentItem: Int = 0
    private var isLoading = false

    private lateinit var enumMovie: MoviesEnum

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
        enumMovie = intent.getSerializableExtra(MOVIES_ENUM_TAG) as MoviesEnum
        observeViewModel()
        setToolbar()
        setRecyclerView()
    }

    private fun setRecyclerView() {
        with(rv_movies) {
            val gridLayoutManager = GridLayoutManager(this@MoviesActivity, SPAN_COUNT)
            adapter = moviesAdapter
            layoutManager = gridLayoutManager
            addItemDecoration(SpacesItemDecoration(SPAN_COUNT, SPAN_WIDTH, false))

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val totalItem = gridLayoutManager.itemCount
                    val lastVisible = gridLayoutManager.findLastVisibleItemPosition()

                    if (!isLoading && totalItem == lastVisible + DEFAULT_PAGE
                        && totalRecords != totalCurrentItem
                        && currentPage != maxPage
                    ) {
                        currentPage++
                        fetchItem(moviesViewModel)
                    }
                }
            })
        }
    }

    private fun observeViewModel() {
        with(moviesViewModel) {
            observeMovies().onResult { response ->
                totalRecords = response?.totalResults ?: DEFAULT_GENRE_ID
                maxPage = response?.totalPages ?: DEFAULT_PAGE

                response?.results?.let {
                    totalCurrentItem += it.size
                    moviesAdapter.addData(it)
                }
            }

            observeLoading().onResult {
                isLoading = it
                pb_paging.goneIf(it)
            }

            boundNetwork {
                if (it) {
                    fetchItem(this)
                }
            }
        }
    }

    private fun setToolbar() {
        titleToolbar = when (enumMovie) {
            MoviesEnum.BYNOWPLAYING -> {
                getString(R.string.now_playing_text)
            }
            MoviesEnum.BYPOPULAR -> {
                getString(R.string.popular_movie_text)
            }
            MoviesEnum.BYUPCOMING -> {
                getString(R.string.upcoming_movies_text)
            }
            MoviesEnum.BYGENRE -> {
                intent.getStringExtra(GENRE_NAME_TAG)
            }
        }
        titleToolbar.changeToolbarTitle(toolbar.toolbar_title)
    }

    private fun fetchItem(viewModel: MoviesViewModel) {
        with(viewModel) {
            when (enumMovie) {
                MoviesEnum.BYNOWPLAYING -> {
                    fetchNowPlayingMovie(getString(R.string.api_key_movie_db), currentPage)
                }
                MoviesEnum.BYPOPULAR -> {
                    fetchPopularMovie(getString(R.string.api_key_movie_db), currentPage)
                }
                MoviesEnum.BYUPCOMING -> {
                    fetchUpComingMovie(getString(R.string.api_key_movie_db), currentPage)
                }
                MoviesEnum.BYGENRE -> {
                    fetchDiscoverMovie(
                        getString(R.string.api_key_movie_db), currentPage,
                        intent.getIntExtra(GENRE_ID_TAG, DEFAULT_GENRE_ID)
                    )
                }
            }
        }
    }


    private fun setupMovies(moviesModel: MoviesModel, view: View) {
        with(view) {
            img_thumnail.loadImage(getString(R.string.image_url, moviesModel.posterPath))
            tv_movie_title.text = moviesModel.title
        }
    }
}
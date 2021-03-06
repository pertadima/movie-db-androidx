package id.co.moviedb.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import id.co.core.commons.DiffCallback
import id.co.core.commons.GeneralRecyclerView
import id.co.core.commons.goneIf
import id.co.core.commons.loadImage
import id.co.core.commons.SpacesItemDecoration
import id.co.moviedb.R
import id.co.moviedb.base.BaseActivity
import id.co.moviedb.data.DetailMoviesResponse
import id.co.moviedb.data.GenreModel
import id.co.moviedb.ui.home.MainActivity.Companion.GENRE_ID_TAG
import id.co.moviedb.ui.home.MainActivity.Companion.GENRE_NAME_TAG
import id.co.moviedb.ui.home.MainActivity.Companion.MOVIES_ENUM_TAG
import id.co.moviedb.ui.home.MainActivity.Companion.MOVIES_ID_TAG
import id.co.moviedb.ui.movies.MoviesActivity
import id.co.moviedb.ui.movies.MoviesEnum
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.default_toolbar.view.*
import kotlinx.android.synthetic.main.viewholder_genres.view.*
import javax.inject.Inject
/**
 * Created by pertadima on 26,March,2019
 */

class DetailMovieActivity : BaseActivity() {
    companion object {
        const val SPAN_COUNT = 2
        const val SPAN_WIDTH = 10
        const val DEFAULT_NULL = 0
    }

    @Inject
    lateinit var detailMovieViewModel: DetailMovieViewModel

    @Inject
    lateinit var diffCallback: DiffCallback

    private val genresAdapter by lazy {
        GeneralRecyclerView<GenreModel>(
            diffCallback = diffCallback,
            holderResId = R.layout.viewholder_genres,
            onBind = { genreModel, view ->
                setupGenresMovie(genreModel, view)
            },
            itemListener = { genreModel, _, _ ->
                startActivity(Intent(this@DetailMovieActivity, MoviesActivity::class.java).apply {
                    putExtra(MOVIES_ENUM_TAG, MoviesEnum.BYGENRE)
                    putExtra(GENRE_ID_TAG, genreModel.id)
                    putExtra(GENRE_NAME_TAG, genreModel.name)
                })
            }
        )
    }

    override fun onSetupLayout(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_detail)
        setupToolbarPropertiesWithBackButton(
            toolbar as Toolbar,
            toolbar.toolbar_title,
            R.string.detail_movie_text
        )
    }

    override fun onViewReady(savedInstanceState: Bundle?) {
        val idMovies = intent.getIntExtra(MOVIES_ID_TAG, DEFAULT_NULL)
        observeState(idMovies)
        with(rv_genres) {
            adapter = genresAdapter
            layoutManager = GridLayoutManager(this@DetailMovieActivity, SPAN_COUNT)
            isNestedScrollingEnabled = false
            addItemDecoration(SpacesItemDecoration(SPAN_COUNT, SPAN_WIDTH, false))
        }
    }

    private fun observeState(idMovies: Int) {
        with(detailMovieViewModel) {
            observeDetailMovie().onResult {
                it?.let { model ->
                    model.genres?.let { genres ->
                        genresAdapter.setData(genres)
                    }
                    setupViewDetail(model)
                }
            }

            observeLoading().onResult {
                progress_horizontal.goneIf(it)
            }

            boundNetwork {
                connectionView(it)
                if (it) {
                    fetchDetailMovies(idMovies, getString(R.string.api_key_movie_db))
                }
            }
        }
    }

    private fun setupGenresMovie(model: GenreModel, view: View) {
        with(view) {
            tv_genre_name.text = model.name
        }
    }

    private fun setupViewDetail(model: DetailMoviesResponse) {
        image_background.loadImage(getString(R.string.image_url, model.backdropPath))
        image_poster.loadImage(getString(R.string.image_url, model.posterPath))
        tv_movie_title.text = model.title
        tv_vote_count.text = model.voteCount.toString()
        tv_popularity.text = model.popularity.toString()
        tv_rating.text = model.voteAverage.toString()
        tv_overview_desc.text = model.overview.toString()
        tv_web_desc.text = model.homepage.toString()
    }

    private fun connectionView(isConnect: Boolean) {
        nested_view.goneIf(isConnect)
        no_internet.goneIf(!isConnect)
    }
}
package id.co.moviedb.ui.movies

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import id.co.moviedb.R
import id.co.moviedb.base.BaseActivity
import id.co.moviedb.ui.home.MainActivity.Companion.MOVIES_ENUM_TAG
import kotlinx.android.synthetic.main.activity_movies.*
import kotlinx.android.synthetic.main.default_toolbar.view.*

/**
 * Created by pertadima on 26,March,2019
 */

class MoviesActivity: BaseActivity() {
    lateinit var titleToolbar: String

    override fun onSetupLayout(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_movies)
        setupToolbarPropertiesWithBackButton(
            toolbar as Toolbar,
            toolbar.toolbar_title,
            R.string.empty_string
        )
    }

    override fun onViewReady(savedInstanceState: Bundle?) {
        val enumMovie : MoviesEnum = intent.getSerializableExtra(MOVIES_ENUM_TAG) as MoviesEnum
        when(enumMovie) {
            MoviesEnum.BYNOWPLAYING -> {
                titleToolbar = getString(R.string.now_playing_text)
            }
            MoviesEnum.BYPOPULAR -> {
                titleToolbar = getString(R.string.popular_movie_text)
            }
            MoviesEnum.BYUPCOMING -> {
                titleToolbar = getString(R.string.upcoming_movies_text)
            }
            MoviesEnum.BYGENRE -> {
                titleToolbar = ""
            }
        }
        titleToolbar.changeToolbarTitle(toolbar.toolbar_title)
    }
}
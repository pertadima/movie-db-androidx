package id.co.moviedb

import android.os.Bundle
import id.co.moviedb.base.BaseActivity

class MainActivity : BaseActivity() {
    override fun onSetupLayout(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_main)
    }

    override fun onViewReady(savedInstanceState: Bundle?) {

    }
}

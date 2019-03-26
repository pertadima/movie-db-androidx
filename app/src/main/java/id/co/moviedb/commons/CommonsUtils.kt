package id.co.moviedb.commons

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import id.co.moviedb.R

/**
 * Created by pertadima on 25,March,2019
 */

fun View.goneIf(isShowing: Boolean) {
    this.visibility = if (isShowing) View.VISIBLE else View.GONE
}

fun ImageView.loadImage(url: String?) {
    Glide.with(this)
        .load(url)
        .apply(
            RequestOptions()
                .centerCrop()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(R.color.lineColor)
                .error(R.color.lineColor)
                .dontAnimate()
        )
        .into(this)
}
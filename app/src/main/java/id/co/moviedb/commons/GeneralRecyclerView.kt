package id.co.moviedb.commons

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.animation.AlphaAnimation
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



/**
 * Created by pertadima on 25,March,2019
 */

class GeneralRecyclerView<T : Any>(
    private val diffCallback: DiffCallback,
    @LayoutRes val holderResId: Int,
    @IdRes val specificResViewId: Int? = null,
    private val onBind: (T, View) -> Unit,
    private val itemListener: (T, pos: Int, View) -> Unit = { _, _, _ -> kotlin.run {} },
    private val specificViewListener: (T, pos: Int, View) -> Unit = { _, _, _ -> kotlin.run {} }
) : RecyclerView.Adapter<GeneralRecyclerView.ViewHolder<T>>() {
    private val listData = mutableListOf<T>()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder<T> {
        val itemView = LayoutInflater.from(p0.context).inflate(holderResId, p0, false)
        var specificView: View? = null
        specificResViewId?.let {
            specificView = itemView?.findViewById(it)
        }
        return ViewHolder(itemView, specificView)
    }

    override fun onBindViewHolder(p0: ViewHolder<T>, p1: Int) {
        setFadeAnimation(p0.itemView)
        p0.bindView(listData[p0.adapterPosition], onBind, itemListener, specificViewListener)
    }

    override fun getItemCount(): Int = listData.size

    fun setData(datas: List<T>) {
        calculateDiff(datas)
    }

    fun addData(newDatas: List<T>) {
        val list = ArrayList(this.listData)
        list.addAll(newDatas)
        calculateDiff(list)
    }

    fun clearData() {
        calculateDiff(emptyList())
    }

    private fun calculateDiff(newDatas: List<T>) {
        diffCallback.setList(listData, newDatas)
        val result = DiffUtil.calculateDiff(diffCallback)
        with(listData) {
            clear()
            addAll(newDatas)
        }
        result.dispatchUpdatesTo(this)
    }

    class ViewHolder<T : Any>(itemView: View, private val specificView: View? = null) :
        RecyclerView.ViewHolder(itemView) {

        fun bindView(
            item: T,
            onBind: (T, View) -> Unit,
            itemListener: (T, pos: Int, View) -> Unit,
            specificViewListener: (T, pos: Int, View) -> Unit
        ) {
            with(itemView) {
                onBind.invoke(item, this)
                setOnClickListener {
                    itemListener.invoke(item, adapterPosition, this)
                }
            }
            specificView?.setOnClickListener {
                specificViewListener.invoke(item, adapterPosition, itemView)
            }
        }

    }

    private fun setFadeAnimation(view: View) {
        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 400
        view.startAnimation(anim)
    }
}
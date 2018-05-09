package client.kimjiwon.swiperefreshlayoutex

import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView

object DataBindingComponents {
    @BindingAdapter("Items")
    fun setItems(recyclerView: RecyclerView, items: ArrayList<MainModel>) {
        val recyclerAdapter: RecyclerAdapter = recyclerView.adapter as RecyclerAdapter
        recyclerAdapter.setItems(items)
    }

}
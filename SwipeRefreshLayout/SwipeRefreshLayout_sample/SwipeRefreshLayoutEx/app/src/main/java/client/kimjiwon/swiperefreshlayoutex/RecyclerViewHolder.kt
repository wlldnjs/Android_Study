package client.kimjiwon.swiperefreshlayoutex

import android.support.v7.widget.RecyclerView
import client.kimjiwon.swiperefreshlayoutex.databinding.ItemUserBinding

class RecyclerViewHolder(binding : ItemUserBinding) : RecyclerView.ViewHolder(binding.root){
    private var mBinding : ItemUserBinding = binding

    fun bind(item : MainModel){
        mBinding.data = item
    }
}
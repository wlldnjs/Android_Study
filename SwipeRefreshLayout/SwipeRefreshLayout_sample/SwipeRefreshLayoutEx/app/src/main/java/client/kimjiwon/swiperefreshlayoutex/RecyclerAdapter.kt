package client.kimjiwon.swiperefreshlayoutex

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import client.kimjiwon.swiperefreshlayoutex.databinding.ItemUserBinding

class RecyclerAdapter(var itemData: ArrayList<MainModel>) : RecyclerView.Adapter<RecyclerViewHolder>() {
    private var itemDatas: ArrayList<MainModel> = itemData

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val binding: ItemUserBinding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecyclerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return if (itemDatas.size > 0) itemDatas.size else 0
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bind(itemDatas[position])
    }

    fun setItems(itemDatas: ArrayList<MainModel>){
        this.itemDatas = itemDatas
    }

    fun addItems(itemDatas: ArrayList<MainModel>){
        this.itemDatas.addAll(itemDatas)
        notifyDataSetChanged()
    }
}
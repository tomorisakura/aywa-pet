package com.grevi.aywapet.ui.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.grevi.aywapet.databinding.ListImageBinding
import com.grevi.aywapet.model.Pictures
import com.grevi.aywapet.utils.Constant.BASE_URL

class PicturesAdapter : RecyclerView.Adapter<PicturesAdapter.PicturesVH>() {

    private val pictureList : MutableList<Pictures> = ArrayList()

    inner class PicturesVH(private val binding : ListImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pictures: Pictures) {
            Glide.with(binding.root).load("$BASE_URL/${pictures.picUrl}").into(binding.petPictures)
        }
    }

    internal fun addItem(list : List<Pictures>) {
        pictureList.clear()
        pictureList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PicturesVH {
        val binding = ListImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PicturesVH(binding)
    }

    override fun onBindViewHolder(holder: PicturesVH, position: Int) {
        holder.bind(pictureList[position])
    }

    override fun getItemCount(): Int {
        return pictureList.size
    }
}
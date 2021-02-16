package com.grevi.aywapet.ui.keep.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.grevi.aywapet.R
import com.grevi.aywapet.databinding.ListKeepBinding
import com.grevi.aywapet.model.Keep
import com.grevi.aywapet.utils.Constant.BASE_URL

class KeepAdapter : RecyclerView.Adapter<KeepAdapter.KeepVH>() {

    private val keepList : MutableList<Keep> = ArrayList()
    internal var onItemClick : ((keep : Keep) -> Unit)? = null

    inner class KeepVH(private val binding : ListKeepBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(keep: Keep) {
            with(binding) {
                petName.text = keep.petId.petName
                petType.text = keep.petId.ras
                genderPet.text = keep.petId.gender
                Glide.with(this.root).load("$BASE_URL/${keep.petId.pictures[0].picUrl}").placeholder(R.drawable.ic_image_placeholder).into(petPic)
            }
        }
    }

    internal fun addItem(item : List<Keep>) {
        keepList.clear()
        keepList.addAll(item)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeepAdapter.KeepVH {
        val binding = ListKeepBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return KeepVH(binding)
    }

    override fun onBindViewHolder(holder: KeepAdapter.KeepVH, position: Int) {
        holder.bind(keepList[position])
        holder.itemView.setOnClickListener { onItemClick?.invoke(keepList[position]) }
    }

    override fun getItemCount(): Int = keepList.size
}
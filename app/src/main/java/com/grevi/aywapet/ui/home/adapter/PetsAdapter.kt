package com.grevi.aywapet.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.grevi.aywapet.databinding.ListPetBinding
import com.grevi.aywapet.model.Pets
import com.grevi.aywapet.utils.Constant.BASE_URL
import com.grevi.aywapet.utils.TouchHelper

class PetsAdapter : RecyclerView.Adapter<PetsAdapter.PetsVH>() {

    private val petsList : MutableList<Pets> = ArrayList()
    private var touchHelper : TouchHelper? = null

    inner class PetsVH(private val binding : ListPetBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pets: Pets) {
            val provSplit = pets.clinicId.address.split(",").toTypedArray()
            with(binding){
                petsName.text = pets.petName
                petsType.text = pets.types.jenis
                petsRas.text = pets.ras
                province.text = provSplit[2]
                Glide.with(this.root).load("$BASE_URL/${pets.pictures[0].picUrl}").into(petsPic)
            }
        }
    }

    internal fun onItemClick(listener : TouchHelper) {
        this.touchHelper = listener
    }

    internal fun addItem(list: List<Pets>) {
        petsList.clear()
        petsList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetsVH {
        val binding = ListPetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PetsVH(binding)
    }

    override fun onBindViewHolder(holder: PetsVH, position: Int) {
        holder.bind(petsList[position])
        holder.itemView.setOnClickListener { touchHelper?.onClickItem(petsList[position]) }
    }

    override fun getItemCount(): Int {
        return petsList.size
    }
}
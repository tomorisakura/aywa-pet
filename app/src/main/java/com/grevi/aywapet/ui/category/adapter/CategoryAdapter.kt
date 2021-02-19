package com.grevi.aywapet.ui.category.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.grevi.aywapet.databinding.ListPetsBinding
import com.grevi.aywapet.model.Pets
import com.grevi.aywapet.utils.Constant

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.CategoryVH>() {

    private val petsList : MutableList<Pets> = ArrayList()

    var itemClickHelper : ((Pets) -> Unit)? = null

    inner class CategoryVH(private val binding : ListPetsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pets : Pets) {
            with(binding) {
                val provSplit = pets.clinicId.address.split(",").toTypedArray()
                petsName.text = pets.petName
                petsType.text = pets.types.jenis
                petsRas.text = pets.ras
                province.text = provSplit[2]
                Glide.with(this.root).load("${Constant.BASE_URL}/${pets.pictures[0].picUrl}").into(petsPic)
            }
        }
    }

    internal fun addItem(list: List<Pets>) {
        petsList.clear()
        petsList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryVH {
        val binding = ListPetsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryVH(binding)
    }

    override fun onBindViewHolder(holder: CategoryVH, position: Int) {
        holder.bind(petsList[position])
        holder.itemView.setOnClickListener { itemClickHelper?.invoke(petsList[position]) }
    }

    override fun getItemCount(): Int = petsList.size
}
package com.grevi.aywapet.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.grevi.aywapet.databinding.ListTypeAnimalBinding
import com.grevi.aywapet.model.Animal

class TypesAdapter : RecyclerView.Adapter<TypesAdapter.TypesVH>() {

    private val listTypes : MutableList<Animal> = ArrayList()

    inner class TypesVH(private val binding : ListTypeAnimalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(animal: Animal) {
            binding.typeName.text = animal.jenis
        }
    }

    internal fun addItem(list: List<Animal>) {
        listTypes.clear()
        listTypes.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypesVH {
        val binding = ListTypeAnimalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TypesVH(binding)
    }

    override fun onBindViewHolder(holder: TypesVH, position: Int) {
        holder.bind(listTypes[position])
    }

    override fun getItemCount(): Int {
        return listTypes.size
    }

}
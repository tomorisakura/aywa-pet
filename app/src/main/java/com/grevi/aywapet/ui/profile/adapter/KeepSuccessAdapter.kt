package com.grevi.aywapet.ui.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.grevi.aywapet.databinding.ListKeepSuccessBinding
import com.grevi.aywapet.model.KeepSuccess
import com.grevi.aywapet.utils.Constant.BASE_URL

class KeepSuccessAdapter(private val listSuccess : ArrayList<KeepSuccess>) : RecyclerView.Adapter<KeepSuccessAdapter.KeepSuccessVH>() {

    inner class KeepSuccessVH(private val binding : ListKeepSuccessBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(keepSuccess: KeepSuccess) {
            with(binding) {
                Glide.with(this.root).load("$BASE_URL/${keepSuccess.petId.pictures[0].picUrl}").into(petsPic)
                petsName.text = keepSuccess.petId.petName
                genderPet.text = keepSuccess.petId.gender
                petsRas.text = keepSuccess.petId.ras
                status.text = keepSuccess.status
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeepSuccessAdapter.KeepSuccessVH {
        val binding = ListKeepSuccessBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return KeepSuccessVH(binding)
    }

    override fun onBindViewHolder(holder: KeepSuccessAdapter.KeepSuccessVH, position: Int) {
        return holder.bind(listSuccess[position])
    }

    override fun getItemCount(): Int {
        return listSuccess.size
    }
}
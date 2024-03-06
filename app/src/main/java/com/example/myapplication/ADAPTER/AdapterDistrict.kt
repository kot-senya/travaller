package com.example.myapplication.ADAPTER

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.myapplication.DATA_CLASS.District
import com.example.myapplication.databinding.PartDistrictBinding

class AdapterDistrict(var collection: MutableList<District>) :
    RecyclerView.Adapter<HolderDistrict>() {
    private var listener: Listener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDistrict {
        val inflate = LayoutInflater.from(parent.context)
        val bind = PartDistrictBinding.inflate(inflate)
        return HolderDistrict(bind)
    }

    override fun getItemCount(): Int {
        return collection.size
    }

    override fun onBindViewHolder(holder: HolderDistrict, position: Int) {
        holder.binding.name.text = collection[position].name
        holder.binding.name.setOnClickListener {
            if (listener != null) {
                listener!!.onClick(holder, position)
            }
        }
    }

    fun setOnListener(listener: Listener){
        this.listener = listener
    }

    interface Listener {
        fun onClick(holder: ViewHolder, position: Int)
    }
}

class HolderDistrict(var binding: PartDistrictBinding) : RecyclerView.ViewHolder(binding.root)
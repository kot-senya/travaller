package com.example.myapplication.ADAPTER

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.BASE.StaticData
import com.example.myapplication.DATA_CLASS.City
import com.example.myapplication.databinding.PartCityBinding

class AdapterCity(var collection: MutableList<City>, var visiblebtn: Boolean):RecyclerView.Adapter<HolderCity>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderCity {
        val inflate = LayoutInflater.from(parent.context)
        val binding = PartCityBinding.inflate(inflate)
        return HolderCity(binding)
    }

    override fun getItemCount(): Int {
        return collection.size
    }

    override fun onBindViewHolder(holder: HolderCity, position: Int) {
        holder.binding.name.text = collection[position].name
        holder.binding.region.text = StaticData.getRegion(collection[position].idRegion)
        if(visiblebtn) holder.binding.btnFavorite.visibility = View.VISIBLE
        else holder.binding.btnFavorite.visibility = View.GONE
    }

}

class HolderCity(var binding: PartCityBinding): RecyclerView.ViewHolder(binding.root)


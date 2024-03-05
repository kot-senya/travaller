package com.example.myapplication.ADAPTER

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.DATA_CLASS.CityImage
import com.example.myapplication.databinding.PartCityImageBinding
import com.squareup.picasso.Picasso

class AdapterCityImage(var collection: MutableList<CityImage>):RecyclerView.Adapter<HolderCityImage>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderCityImage {
        var inflater = LayoutInflater.from(parent.context)
        var bind = PartCityImageBinding.inflate(inflater)
        return HolderCityImage(bind)
    }

    override fun getItemCount(): Int {
        return  collection.size
    }

    override fun onBindViewHolder(holder: HolderCityImage, position: Int) {
        Picasso.get().load(collection[position].image).into(holder.binding.image)
    }
}

class HolderCityImage(var binding: PartCityImageBinding): RecyclerView.ViewHolder(binding.root)
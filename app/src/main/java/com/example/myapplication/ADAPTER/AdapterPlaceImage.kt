package com.example.myapplication.ADAPTER

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.DATA_CLASS.CityImage
import com.example.myapplication.DATA_CLASS.PlaceImage
import com.example.myapplication.databinding.PartCityImageBinding
import com.squareup.picasso.Picasso

class AdapterPlaceImage(var collection: MutableList<PlaceImage>): RecyclerView.Adapter<HolderPlaceImage>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderPlaceImage {
        var inflater = LayoutInflater.from(parent.context)
        var bind = PartCityImageBinding.inflate(inflater)
        return HolderPlaceImage(bind)
    }

    override fun getItemCount(): Int {
        return  collection.size
    }

    override fun onBindViewHolder(holder: HolderPlaceImage, position: Int) {
        Picasso.get().load(collection[position].image).into(holder.binding.image)
    }
}

class HolderPlaceImage(var binding: PartCityImageBinding): RecyclerView.ViewHolder(binding.root)
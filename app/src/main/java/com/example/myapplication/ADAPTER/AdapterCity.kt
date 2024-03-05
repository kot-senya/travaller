package com.example.myapplication.ADAPTER

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.BASE.StaticData
import com.example.myapplication.DATA_CLASS.City
import com.example.myapplication.R
import com.example.myapplication.databinding.PartCityBinding
import com.squareup.picasso.Picasso

class AdapterCity(var collection: MutableList<City>, var visiblebtn: Boolean) :
    RecyclerView.Adapter<HolderCity>() {
    private var favoriteListener: FavoriteListener? = null
    private var lookListener: LookGuide? = null

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

        if(collection[position].image == null || collection[position].image == "" || collection[position].image == "NULL")
        {
            holder.binding.image.setBackgroundResource(R.drawable.ic_city)
        }

        if (collection[position].image != null || collection[position].image != "" || collection[position].image != "NULL") {
            Picasso.get().load(collection[position].image.toString()).into(holder.binding.image)
        }

        if (visiblebtn && StaticData.auth) {
            holder.binding.btnFavorite.visibility = View.VISIBLE
            if (!StaticData.city_favorite.filter { it.idCity == collection[position].id }
                    .toMutableList().isEmpty()) {
                holder.binding.btnFavorite.setBackgroundResource(R.drawable.ic_favorite_city1)
            } else holder.binding.btnFavorite.setBackgroundResource(R.drawable.ic_favorite_city)
        } else holder.binding.btnFavorite.visibility = View.GONE
        holder.binding.btnFavorite.setOnClickListener {
            if (favoriteListener != null) {
                favoriteListener!!.onClick(holder, position)
            }
        }
        holder.itemView.setOnClickListener {
            if (lookListener != null) {
                lookListener!!.onClick(holder, position)
            }
        }
    }

    fun setFavoriteListener(listener: FavoriteListener) {
        favoriteListener = listener
    }

    fun setLookListener(listener: LookGuide) {
        lookListener = listener
    }

    interface FavoriteListener {
        fun onClick(holder: HolderCity, position: Int)
    }

    interface LookGuide {
        fun onClick(holder: HolderCity, position: Int)
    }
}

class HolderCity(var binding: PartCityBinding) : RecyclerView.ViewHolder(binding.root)


package com.example.myapplication.ADAPTER

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.DATA_CLASS.Place
import com.example.myapplication.DATA_CLASS.PlaceImage
import com.example.myapplication.R
import com.example.myapplication.databinding.PartObjectBinding
import com.squareup.picasso.Picasso

open class AdapterPlace(var collection: MutableList<Place>, var image: MutableList<PlaceImage>) :
    RecyclerView.Adapter<HolderPlace>() {
        private var listener: Listener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderPlace {
        var inflater = LayoutInflater.from(parent.context)
        var bind = PartObjectBinding.inflate(inflater)
        return HolderPlace(bind)
    }

    override fun getItemCount(): Int {
        return collection.size
    }

    override fun onBindViewHolder(holder: HolderPlace, position: Int) {
        holder.binding.name.text = collection[position].name
        holder.binding.time.text = collection[position].workingHours

        if (collection[position].cost == 0f)
            holder.binding.cost.text = "Вход: бесплатно"
        else
            holder.binding.cost.text = "Вход: " + collection[position].cost.toString() + " ₽"

        var i: MutableList<PlaceImage> =
            image.filter { it.idPlace == collection[position].id }.toMutableList()

        if (i.size > 0)
            Picasso.get().load(i[0].image).into(holder.binding.image)
        else
            holder.binding.image.setBackgroundResource(R.drawable.ic_place)

        holder.itemView.setOnClickListener {
            if(listener != null){
                listener!!.onClick(holder,i, collection[position])
            }
        }
    }

    fun setListener(listener: Listener){
        this.listener = listener
    }

    interface Listener{
       fun onClick(holder: HolderPlace,image: MutableList<PlaceImage>, place: Place)
    }
}

class HolderPlace(var binding: PartObjectBinding) : RecyclerView.ViewHolder(binding.root)
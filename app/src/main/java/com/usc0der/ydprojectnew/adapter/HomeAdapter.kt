package com.usc0der.ydprojectnew.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.usc0der.ydprojectnew.R
import com.usc0der.ydprojectnew.databinding.ItemHomeBinding
import com.usc0der.ydprojectnew.model.HomeModel

class HomeAdapter(var list: List<HomeModel>, var onItemClick: OnItemClick, val context: Context) :
    RecyclerView.Adapter<HomeAdapter.Vh>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        val binding = ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Vh(binding)
    }
    override fun onBindViewHolder(holder: Vh, position: Int) {

        Glide.with(context).load(list[position].icon)
//            .centerCrop().apply(RequestOptions.circleCropTransform())
            .placeholder(R.drawable.yangidavrsrc).into(holder.binding.img);

        holder.binding.tvTitle.text = list[position].name

        holder.binding.rl.setOnClickListener {
            onItemClick.itemClick(position,list[position].id,list[position])
        }
    }

    override fun getItemCount(): Int = list.size;



    class Vh(val binding: ItemHomeBinding) : RecyclerView.ViewHolder(binding.root)
    interface OnItemClick {
        fun itemClick(position:Int,id:Int,model: HomeModel)
    }

}
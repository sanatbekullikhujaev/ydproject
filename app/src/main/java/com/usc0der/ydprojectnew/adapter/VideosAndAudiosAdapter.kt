package com.usc0der.ydprojectnew.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.usc0der.ydprojectnew.R
import com.usc0der.ydprojectnew.databinding.ItemVideosBinding
import com.usc0der.ydprojectnew.model.VideosAndAudios

class VideosAndAudiosAdapter(
    val list: List<VideosAndAudios>,
    val context: Context,
    var onItemClick: OnItemClick
) : RecyclerView.Adapter<VideosAndAudiosAdapter.Vh>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        val binding = ItemVideosBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Vh(binding)
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {

        val circularProgressDrawable =
            androidx.swiperefreshlayout.widget.CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        Glide.with(context)
            .load(list[position].image_file)
            .error(R.drawable.ic_about_us_light_gray)
            .placeholder(circularProgressDrawable)
            .into(holder.binding.img)

        holder.binding.tvTitle.text = list[position].title.toString()
        holder.binding.tvWord.text = list[position].content.toString()

        holder.binding.cons.setOnClickListener {
            onItemClick.itemClick(position, list[position].id)
        }
    }

    override fun getItemCount(): Int = list.size
    class Vh(val binding: ItemVideosBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClick {
        fun itemClick(position: Int, id: Int)
    }
}
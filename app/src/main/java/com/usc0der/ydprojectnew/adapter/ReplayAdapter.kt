package com.usc0der.ydprojectnew.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.usc0der.ydprojectnew.databinding.ItemReplayBinding
import com.usc0der.ydprojectnew.model.ReplayComment
import com.usc0der.ydprojectnew.model.ReplayObj

class ReplayAdapter(val list: ArrayList<ReplayObj>, val onItemClick: OnItemClick) :
    RecyclerView.Adapter<ReplayAdapter.Vh>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        val binding = ItemReplayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Vh(binding)
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.binding.rl.setOnClickListener {
            onItemClick.itemClick(
                list.size - 1 - position,
                list[list.size - 1 - position].answer.id,
                list[list.size - 1 - position].answer
            )
        }
        holder.binding.tvMessage.text = list[list.size - 1 - position].answer.body
        holder.binding.tvUsername.text = list[list.size - 1 - position].answer.username
        holder.binding.tvDate.text =
            list[list.size - 1 - position].answer.created_date.substring(0,16)
    }

    override fun getItemCount(): Int = list.size
    class Vh(val binding: ItemReplayBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClick {
        fun itemClick(position: Int, id: Int,replayComment: ReplayComment)
    }
}
package com.usc0der.ydprojectnew.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.usc0der.ydprojectnew.app.App
import com.usc0der.ydprojectnew.databinding.ItemCommitBinding
import com.usc0der.ydprojectnew.model.Comment
import com.usc0der.ydprojectnew.model.CommentObj

/**
 * Created by Ullixo'jayev San'atbek on 12.06.2021 11:50
 **/
class CommentAdapter(val list: ArrayList<CommentObj>, var onItemClick: OnItemClick) :
    RecyclerView.Adapter<CommentAdapter.Vh>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        val binding = ItemCommitBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Vh(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Vh, position: Int) {

        holder.binding.rl.setOnClickListener {
            onItemClick.onClick(position, list[position].comment.id)
        }
        holder.binding.rl.setOnLongClickListener {
            if (App.mApp.getUserLogin().toString() == list[position].comment.username) {
                onItemClick.longClick(
                    position,
                    list[position].comment.id,
                    list[position].comment
                )
            }
            true
        }
        holder.binding.tvCommentCount.text = list[position].answer_count + " ta fikr "
        holder.binding.tvMessage.text = list[position].comment.body
        holder.binding.tvDate.text =
            list[position].comment.created_time.substring(0, 16)
        holder.binding.tvUsername.text = list[position].comment.username

    }

    override fun getItemCount(): Int = list.size
    class Vh(val binding: ItemCommitBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClick {
        fun onClick(position: Int, id: Int)
        fun longClick(position: Int, id: Int, comment: Comment)
    }
}
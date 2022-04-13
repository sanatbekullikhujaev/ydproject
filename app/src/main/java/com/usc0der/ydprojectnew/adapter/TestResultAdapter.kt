package com.usc0der.ydprojectnew.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.usc0der.ydprojectnew.R
import com.usc0der.ydprojectnew.databinding.ItemHomeBinding
import com.usc0der.ydprojectnew.databinding.ItemTestResultBinding
import com.usc0der.ydprojectnew.model.HistoryTest

class TestResultAdapter(var list: List<HistoryTest>) :
    RecyclerView.Adapter<TestResultAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding =
            ItemTestResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    class VH(val binding: ItemTestResultBinding) : RecyclerView.ViewHolder(binding.root) {
        private val question=binding.question
        private val numeric=binding.numericTxt
        private val answer=binding.answer
        @SuppressLint("SetTextI18n")
        fun bind(data: HistoryTest) {
            question.text= data.question
            answer.text=data.selected
            numeric.text="${adapterPosition+1}-savol"
            if (data.correct){
                answer.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check, 0);
            }else  answer.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_clear, 0);
        }
    }
}
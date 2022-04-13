package com.usc0der.ydprojectnew.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.usc0der.ydprojectnew.MainActivity
import com.usc0der.ydprojectnew.R
import com.usc0der.ydprojectnew.databinding.ActivityResultsBinding

class ResultsActivity : AppCompatActivity() {
    private var _binding: ActivityResultsBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        val correct = intent.getIntExtra("correct", 0)
        val size = intent.getIntExtra("size", 0)
        val percent = intent.getFloatExtra("percent", 0F)
        val historyId = intent.getIntExtra("historyId", 0)
        val wrongs = size - correct
        binding.correct.text = "$correct ta"
        binding.wrong.text = "$wrongs ta"
        binding.point.text = "$percent %"

        binding.btnStart.setOnClickListener {
            startActivity(Intent(this, QuizQuestionsActivity::class.java))
        }
        binding.btnEnd.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        binding.history.setOnClickListener {
            val intent=Intent(this, HistoryTestActivity::class.java)
            intent.putExtra("historyId",historyId)
            startActivity(intent)
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
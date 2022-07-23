package com.usc0der.ydprojectnew.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.usc0der.ydprojectnew.R
import com.usc0der.ydprojectnew.api.network.ApiClient
import com.usc0der.ydprojectnew.api.network.ApiHelper
import com.usc0der.ydprojectnew.api.network.ApiService
import com.usc0der.ydprojectnew.api.network.UIState
import com.usc0der.ydprojectnew.api.viewmodel.*
import com.usc0der.ydprojectnew.databinding.ActivityQuizQuestionsBinding
import com.usc0der.ydprojectnew.model.Question
import com.usc0der.ydprojectnew.utils.SharePref
import kotlinx.android.synthetic.main.activity_quiz_questions.*
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class QuizQuestionsActivity : AppCompatActivity() {
    private var _binding: ActivityQuizQuestionsBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityQuizQuestionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun onClick() = with(binding) {
        tvAnswerA.setOnClickListener {

        }
        tvAnswerB.setOnClickListener {

        }
        tvAnswerC.setOnClickListener {

        }
        tvAnswerD.setOnClickListener {

        }
    }
}
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
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.usc0der.ydprojectnew.R
import com.usc0der.ydprojectnew.api.network.ApiClient
import com.usc0der.ydprojectnew.api.network.ApiHelper
import com.usc0der.ydprojectnew.api.network.ApiService
import com.usc0der.ydprojectnew.api.network.UIState
import com.usc0der.ydprojectnew.api.viewmodel.*
import com.usc0der.ydprojectnew.databinding.ActivityQuizQuestionsBinding
import com.usc0der.ydprojectnew.databinding.ActivityTestBinding
import com.usc0der.ydprojectnew.model.Question
import com.usc0der.ydprojectnew.utils.ConstantsQuiz
import com.usc0der.ydprojectnew.utils.SharePref
import kotlinx.android.synthetic.main.activity_quiz_questions.*
import kotlinx.coroutines.flow.collect
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class QuizQuestionsActivity : AppCompatActivity() {
    private var _binding: ActivityQuizQuestionsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TestViewModel
    private var mCurrentPosition: Int = 1
    private var correctAnswer: Int = 0
    private var wrongAnswer: Int = 0
    private var listSize: Int = 0
    private var id = 0
    private lateinit var map: HashMap<String, Any>
    private lateinit var mQuestionList: ArrayList<Question>

    private val sharePref by lazy { SharePref(applicationContext) }
    private var mSelectedOptionPosition: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityQuizQuestionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        viewModel = ViewModelProvider(
            this, TestViewModelFactory(
                ApiHelper(
                    ApiClient.createServiceWithToken(
                        ApiService::class.java,
                        this
                    )
                )
            )
        ).get(TestViewModel::class.java)

        id = sharePref.getVideoOrAudioId()
        map = HashMap()
        mQuestionList = ArrayList()
        getQuestions()
        onClickItem()

    }


    private val countDownTimer = object : CountDownTimer(120000, 1000) {

        @SuppressLint("SetTextI18n")
        override fun onTick(millisUntilFinished: Long) {
            binding.tvCount.text = "${(millisUntilFinished / 1000)} s"
        }

        override fun onFinish() {

            if (mSelectedOptionPosition == 0) {
                mCurrentPosition++

                when {
                    mCurrentPosition <= mQuestionList!!.size -> {
                        setQuestion()
                        widgetEnable(true)
                    }
                    else -> {
                        cancel()
                        sendData()
                    }
                }
            } else {
                val question = mQuestionList[mCurrentPosition - 1]

                map[question.id.toString()] = mSelectedOptionPosition.toString()

                if (question.correctOption != mSelectedOptionPosition) {

                    widgetEnable(false)
                    answerView(mSelectedOptionPosition, R.drawable.wrong_option_border_bg)
                    wrongAnswer++
                }
                widgetEnable(false)
                answerView(question.correctOption, R.drawable.correct_option_border_bg)
                if (mCurrentPosition == mQuestionList.size) {
                    btn_submit.text = "Finish"
                } else {
                    btn_submit.text = "Keyingisi"
                }
                mSelectedOptionPosition = 0
            }

        }
    }


    @SuppressLint("SetTextI18n")
    private fun setQuestion() {
        val question = mQuestionList[mCurrentPosition - 1]
        defaultOptionsView()
        if (mCurrentPosition == mQuestionList.size) {
            binding.btnSubmit.text = "Finish"
        } else {
            binding.btnSubmit.text = "Tasdiqlash"

        }
        countDownTimer.start()

        binding.progressBar.progress = mCurrentPosition
        binding.progressBar.max = mQuestionList.size
        binding.tvProgress.text = "$mCurrentPosition" + "/" + mQuestionList.size

        binding.tvQuestion.text = question.question
        binding.tvOptionOne.text = question.optionOne
        binding.tvOptionTwo.text = question.optionTwo
        binding.tvOptionThree.text = question.optionThree
        binding.tvOptionFour.text = question.optionFour
    }

    private fun defaultOptionsView() {
        binding.btnSubmit.isEnabled = false
        binding.btnSubmit.alpha = 0.5f
        val options = ArrayList<TextView>()
        options.add(0, binding.tvOptionOne)
        options.add(1, binding.tvOptionTwo)
        options.add(2, binding.tvOptionThree)
        options.add(3, binding.tvOptionFour)

        for (option in options) {
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(
                this,
                R.drawable.default_option_border_bg
            )
        }

    }

    private fun onClickItem() = with(binding) {
        tvOptionOne.setOnClickListener { selectedOptionView(tvOptionOne, 1) }
        tvOptionTwo.setOnClickListener { selectedOptionView(tvOptionTwo, 2) }
        tvOptionThree.setOnClickListener { selectedOptionView(tvOptionThree, 3) }
        tvOptionFour.setOnClickListener { selectedOptionView(tvOptionFour, 4) }

        btnSubmit.setOnClickListener {
            if (mSelectedOptionPosition == 0) {
                mCurrentPosition++

                when {
                    mCurrentPosition <= mQuestionList!!.size -> {
                        setQuestion()
                        widgetEnable(true)
                    }
                    else -> {
                        countDownTimer.cancel()
                        sendData()
                    }
                }
            } else {
                val question = mQuestionList?.get(mCurrentPosition - 1)
                map[question!!.id.toString()] = mSelectedOptionPosition.toString()

                if (question!!.correctOption != mSelectedOptionPosition) {
                    widgetEnable(false)
                    answerView(mSelectedOptionPosition, R.drawable.wrong_option_border_bg)
                    wrongAnswer++

                }
                widgetEnable(false)
                answerView(question.correctOption, R.drawable.correct_option_border_bg)

                if (mCurrentPosition == mQuestionList!!.size) {
                    btn_submit.text = "Finish"
                } else {
                    countDownTimer.cancel()
                    btn_submit.text = "Keyingisi"
                }
                mSelectedOptionPosition = 0
            }


        }

    }

    private fun widgetEnable(it: Boolean) = with(binding) {
        tvOptionOne.isEnabled = it
        tvOptionTwo.isEnabled = it
        tvOptionThree.isEnabled = it
        tvOptionFour.isEnabled = it

    }

    private fun selectedOptionView(tv: TextView, selectedOptionNum: Int) {
        defaultOptionsView()
        mSelectedOptionPosition = selectedOptionNum
        tv.setTextColor(Color.parseColor("#363A43"))
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(
            this,
            R.drawable.selected_option_border_bg
        )
        binding.btnSubmit.isEnabled = true
        binding.btnSubmit.alpha = 1f
    }

    private fun answerView(answer: Int, drawableView: Int) {
        when (answer) {
            1 -> {
                binding.tvOptionOne.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }
            2 -> {
                binding.tvOptionTwo.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }
            3 -> {
                binding.tvOptionThree.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }
            4 -> {
                binding.tvOptionFour.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }
        }
    }

    private fun getQuestions() {
        lifecycleScope.launchWhenStarted {
            viewModel.getQuestion(id)
            viewModel.questionState.collect {
                when (it) {
                    is UIState.Success -> {
                        binding.progress.isVisible=false
                        binding.scrollView.isVisible=true
                        mQuestionList.clear()
                        mQuestionList.addAll(it.data)
                        setQuestion()
                    }
                    is UIState.Error -> {
                        binding.progress.isVisible = false
                    }

                    else -> Unit
                }
            }
        }
    }


    private fun sendData() {
        val json=JSONObject(map as Map<*, *>)
        //Toast.makeText(applicationContext, json.toString(2), Toast.LENGTH_SHORT).show()
        lifecycleScope.launchWhenStarted {
            viewModel.postResult(json.toString(2))
            viewModel.resultState.collect {
                when (it) {
                    is UIState.Success -> {
                        listSize = mQuestionList.size
                        val intent = Intent(applicationContext, ResultsActivity::class.java)
                        intent.putExtra("size", it.data.countAnswers)
                        intent.putExtra("correct", it.data.correctAnswers)
                        intent.putExtra("percent", it.data.percent)
                        intent.putExtra("historyId", it.data.historyId)
                        startActivity(intent)
                        finish()
                    }
                    is UIState.Error -> {
                        Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }

        }


    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel()
    }
}
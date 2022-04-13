package com.usc0der.ydprojectnew.ui

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.usc0der.ydprojectnew.R
import com.usc0der.ydprojectnew.api.network.ApiClient
import com.usc0der.ydprojectnew.api.network.ApiHelper
import com.usc0der.ydprojectnew.api.network.ApiService
import com.usc0der.ydprojectnew.api.network.UIState
import com.usc0der.ydprojectnew.api.viewmodel.LikeAndDislikeFactory
import com.usc0der.ydprojectnew.api.viewmodel.LikeAndDislikeViewModel
import com.usc0der.ydprojectnew.api.viewmodel.TestViewModel
import com.usc0der.ydprojectnew.api.viewmodel.TestViewModelFactory
import com.usc0der.ydprojectnew.databinding.ActivityDictionariyBinding
import com.usc0der.ydprojectnew.model.Dictionary
import com.usc0der.ydprojectnew.room.VideoDbModel
import com.usc0der.ydprojectnew.utils.SharePref
import com.usc0der.ydprojectnew.utils.SharedPref
import kotlinx.android.synthetic.main.activity_quiz_questions.*
import kotlinx.coroutines.flow.collect
import java.util.*
import kotlin.collections.ArrayList

class DictionaryActivity : AppCompatActivity() {
    private var _binding: ActivityDictionariyBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TestViewModel
    private var dictionaryIndex = 0
    private var id = 0
    private var action: Boolean = false
    private val sharePref by lazy { SharePref(applicationContext) }
    private val dictionaryList by lazy { ArrayList<Dictionary>() }
    private lateinit var audioManager: AudioManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDictionariyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(
            this, TestViewModelFactory(
                ApiHelper(
                    ApiClient.createServiceWithToken(
                        ApiService::class.java, this
                    )
                )
            )
        ).get(TestViewModel::class.java)
        id = sharePref.getVideoOrAudioId()
        onClick()
        getDictionary()
    }


    private fun onClick() = with(binding) {
        audioMediaPlayer = android.media.MediaPlayer()
        iVPlayPause.setOnClickListener {
            if (audioMediaPlayer.isPlaying) {
                audioMediaPlayerPause()
//                isPlayingStatus = false
            } else {
//                isPlayingStatus = true
                audioMediaPlayerPlay()

            }

        }

        seekbarAudio.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                audioMediaPlayer.seekTo(progress)
                seekbarAudio.progress = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                binding.seekbarAudio.progress=0
            }

        })

        btnNext.setOnClickListener {
            btnBack.isEnabled = true
            dictionaryIndex++
            if (dictionaryIndex == dictionaryList.size - 1) {
                btnNext.isEnabled = false
                dicSetText(dictionaryIndex)
                playAudio()
            } else {
                playAudio()
                dicSetText(dictionaryIndex)
            }

        }
        btnBack.setOnClickListener {
            dictionaryIndex--
            btnNext.isEnabled = true
            if (0 == dictionaryIndex) {
                btnBack.isEnabled = false
                dicSetText(dictionaryIndex)
                playAudio()
            } else {
                playAudio()
                dicSetText(dictionaryIndex)
            }

        }

    }

    private fun audioMediaPlayerPlay() = with(binding) {
        audioMediaPlayer.start()
        iVPlayPause.setImageResource(R.drawable.ic_pause_blue)
    }

    private fun audioMediaPlayerPause() = with(binding) {
        audioMediaPlayer.pause()
        iVPlayPause.setImageResource(R.drawable.ic_play_blue)
    }


    private lateinit var audioMediaPlayer: MediaPlayer

    private fun playAudio() {

        //Toast.makeText(applicationContext, "play ${dictionaryList[dictionaryIndex].audio_file}", Toast.LENGTH_SHORT).show()
        val audioUri = Uri.parse(dictionaryList[dictionaryIndex].audio_file)
        audioMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        audioMediaPlayer.reset()

        try {
            audioMediaPlayer.setDataSource(this, audioUri)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        audioMediaPlayer.prepareAsync()
        audioMediaPlayer.setOnPreparedListener {
            setAudioProgress()

        }
        audioMediaPlayer.setOnCompletionListener {
            binding.seekbarAudio.progress=0
            binding.iVPlayPause.setImageResource(R.drawable.ic_play_blue)
        }


    }

    private val handler = Handler()
    private var runnableStatusAudio = true
    private fun setAudioProgress() {
        try {
            //get the video duration
            var currentPos = audioMediaPlayer.currentPosition
            val totalDuration = audioMediaPlayer.duration

//            Toast.makeText(applicationContext, "$totalDuration", Toast.LENGTH_SHORT).show()

            //display video duration
            binding.tvAudioEndTime.text = timeConversion(totalDuration.toLong())
            binding.tvAudioStartTime.text = timeConversion(currentPos.toLong())
            binding.seekbarAudio.max = totalDuration
            val runnable = object : Runnable {
                override fun run() {
                    try {
                        if (runnableStatusAudio) {
                            currentPos = audioMediaPlayer.currentPosition
                            binding.tvAudioStartTime.text = timeConversion(currentPos.toLong())
                            binding.seekbarAudio.progress = currentPos
                            handler.postDelayed(this, 1000)

//                            Toast.makeText(applicationContext, "/n$currentPos", Toast.LENGTH_SHORT).show()
                        }
                    } catch (ed: IllegalStateException) {
                        ed.printStackTrace()
                    }
                }
            }
            handler.postDelayed(runnable, 1000)
            //seekbar change listner
            binding.seekbarAudio.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar,
                    progress: Int,
                    fromUser: Boolean
                ) {
//                    audioMediaPlayer.seekTo(progress)
//                    binding.seekbarAudio.progress = progress
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    currentPos = seekBar.progress
                    audioMediaPlayer.seekTo(currentPos)
                }
            })

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun timeConversion(value: Long): String {
        val dur = value
        val hrs = dur / 3600000
        val mns = dur / 60000 % 60000
        val scs = dur % 60000 / 1000
        return if (hrs > 0) {
            String.format("%02d:%02d:%02d", hrs, mns, scs)
        } else {
            String.format("%02d:%02d", mns, scs)
        }
    }


    private fun dicSetText(index: Int) {
        binding.txtRu.text = dictionaryList[index].key
        binding.txtUz.text = dictionaryList[index].value

    }

    private fun getDictionary() {
        lifecycleScope.launchWhenStarted {
            viewModel.getDictionary(id)
            viewModel.dictionaryState.collect {
                when (it) {
                    is UIState.Success -> {
                        dictionaryList.clear()
                        dictionaryList.addAll(it.data)
                        binding.txtRu.text = dictionaryList[dictionaryIndex].key
                        binding.txtUz.text = dictionaryList[dictionaryIndex].value
                        playAudio()
                        binding.pbLogon.isVisible = false
                    }
                    is UIState.Error -> {
                        binding.pbLogon.isVisible = false
                        //Toast.makeText(applicationContext, "error", Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }

        }

    }
    override fun onBackPressed() {
        super.onBackPressed()
        runnableStatusAudio = false
        audioMediaPlayer.stop()
        this.finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
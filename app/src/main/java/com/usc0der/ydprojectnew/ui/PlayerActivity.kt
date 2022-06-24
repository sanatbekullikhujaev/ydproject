package com.usc0der.ydprojectnew.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Color
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.children
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.core.view.size
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding4.view.scrollChangeEvents
import com.norulab.exofullscreen.MediaPlayer
import com.norulab.exofullscreen.setSource
import com.usc0der.ydprojectnew.room.AppDatabase
import com.usc0der.ydprojectnew.api.network.ApiClient
import com.usc0der.ydprojectnew.api.network.ApiHelper
import com.usc0der.ydprojectnew.api.network.ApiService
import com.usc0der.ydprojectnew.api.network.UIState
import com.usc0der.ydprojectnew.api.viewmodel.LikeAndDislikeFactory
import com.usc0der.ydprojectnew.api.viewmodel.LikeAndDislikeViewModel
import com.usc0der.ydprojectnew.api.viewmodel.PlayerViewModel
import com.usc0der.ydprojectnew.api.viewmodel.PlayerViewModelFactory
import com.usc0der.ydprojectnew.app.App
import com.usc0der.ydprojectnew.databinding.ActivityPlayerBinding
import com.usc0der.ydprojectnew.databinding.DateOffDialogBinding
import com.usc0der.ydprojectnew.model.Player
import com.usc0der.ydprojectnew.room.RoomModel
import com.usc0der.ydprojectnew.room.VideoAppDatabase
import com.usc0der.ydprojectnew.room.VideoDbModel
import com.usc0der.ydprojectnew.utils.SharePref
import com.usc0der.ydprojectnew.utils.SharedPref
import kotlinx.coroutines.flow.collect
import com.usc0der.ydprojectnew.utils.NetworkLiveData
import com.usc0der.ydprojectnew.R
import kotlinx.android.synthetic.main.activity_player.*
import okhttp3.MultipartBody
import java.io.File

class PlayerActivity : AppCompatActivity(), CommentChatFragment.OnItemClick,
    ReplayChatFragment.OnItemClick, com.google.android.exoplayer2.Player.Listener {
    private var _binding: ActivityPlayerBinding? = null
    private val binding get() = _binding!!
    private var telegramPath = ""
    private lateinit var commentChatFragment: CommentChatFragment
    private lateinit var replayChatFragment: ReplayChatFragment
    private lateinit var viewmodel: PlayerViewModel
    private val sharedPref by lazy { SharedPref(this) }


    private val sharePref by lazy { SharePref(this) }
    private var id = 0
    private lateinit var player: Player

    private var TAG = "TAG"

    private var key = ""
    private lateinit var audioManager: AudioManager
    private val database by lazy { AppDatabase.getDataseClient(this) }
    private var dataStatus = true
    private val videoAppDatabase by lazy { VideoAppDatabase.getDataseClient(this) }
    private lateinit var likeAndDislikeViewModel: LikeAndDislikeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPlayerBinding.inflate(layoutInflater)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        setContentView(binding.root)
        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        key = intent.getStringExtra("key").toString()
        id = sharePref.getVideoOrAudioId()
        setTextBtn(key)
        viewmodel = ViewModelProvider(
            this, PlayerViewModelFactory(
                ApiHelper(
                    ApiClient.createServiceWithToken(
                        ApiService::class.java, this
                    )
                )
            )
        ).get(PlayerViewModel::class.java)

        likeAndDislikeViewModel = ViewModelProvider(
            this, LikeAndDislikeFactory(
                ApiHelper(
                    ApiClient.createServiceWithToken(
                        ApiService::class.java, this
                    )
                )
            )
        ).get(LikeAndDislikeViewModel::class.java)

        binding.btnCommentChat.setOnClickListener {
            commentChatFragment = CommentChatFragment(this)
            commentChatFragment.show(supportFragmentManager, "TAG")
        }
        NetworkLiveData(this).observe(this) {
            if (it && dataStatus) {
                initData()
            } else {
                Snackbar.make(binding.root.rootView, "Internet aloqasi yo'q", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
        onclick()
        binding.toolbar.imgBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setTextBtn(key: String) {
        if (key == "audio") {
            binding.btnTest.text = "Lug`at"
        } else {
            binding.btnTest.text = "Test"
        }

    }

    private var isPlayingStatus = false

    private val mediaPlayer by lazy { MediaPlayer }

    private fun initPlayer(url: String) {
        try {

            mediaPlayer.initialize(applicationContext)
            preparePlayer(binding.videoView, true)
//        mediaPlayer.exoPlayer?.preparePlayer(binding.videoView, true)
            mediaPlayer.exoPlayer?.setSource(
                this,
                url
            )
            try {
                val position = videoAppDatabase.videoPosition().getObjPositionId(id).videoPosition
                mediaPlayer.exoPlayer?.seekTo(position)
                mediaPlayer.startPlayer()

            } catch (e: Exception) {
                mediaPlayer.startPlayer()
                e.printStackTrace()
            }
            if (audioMediaPlayer.isPlaying) {
                audioMediaPlayer.pause()
            }
                setVideoProgress()
            binding.videoView.setShowNextButton(false)
            binding.videoView.setShowPreviousButton(false)
            imgAudioTimer.start()
        } catch (e: Exception) {
            initPlayer(player.data?.data?.video_file!!)
            e.printStackTrace()
        }
    }

    private fun onclick() = with(binding) {

        btnTest.setOnClickListener {
            if (key == "audio")
                startActivity(Intent(applicationContext, DictionaryActivity::class.java))
            else
                startActivity(Intent(applicationContext, QuizQuestionsActivity::class.java))
        }

        imgTelegram.setOnClickListener {
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/joinchat/zNpJurI4W-ZkOTdi"))
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(player.data?.data?.telegram_link))
            startActivity(intent)
        }
        videoView.setOnClickListener {
            if (cardVolumeControl.isVisible) {
                cardVolumeControl.visibility = View.GONE
            } else {
                cardVolumeControl.visibility = View.VISIBLE
                imgAudioTimer.start()
            }
        }
        videoViewLandscape.setOnClickListener {
            if (cardVolumeControlLandscape.isVisible) {
                cardVolumeControlLandscape.visibility = View.GONE
            } else {
                cardVolumeControlLandscape.visibility = View.VISIBLE
                imgAudioLandscaperTimer.start()
            }
        }
        cardVolumeControl.setOnClickListener {
            imgAudioTimer.cancel()
            cardVolumeSeekbar.visibility = View.VISIBLE

            seekbarVolume.progress = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            seekbarVolume.max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        }
        cardVolumeControlLandscape.setOnClickListener {
            volumeControlLandscapeTimer.cancel()
            cardVolumeSeekbarLandscape.visibility = View.VISIBLE
            seekbarVolumeLandscape.progress =
                audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            seekbarVolumeLandscape.max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        }
        videoView.setShowNextButton(false)
        videoView.setShowPreviousButton(false)
        seekbarVolume.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0)
                cardVolumeControl.isVisible = false
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                volumeControlTimer.start()
            }
        })
        seekbarVolumeLandscape.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0)
                cardVolumeControlLandscape.isVisible = true
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                volumeControlLandscapeTimer.start()
            }
        })
        /// language audio
        audioMediaPlayer = android.media.MediaPlayer()

        imgAudioPalyAndPause.setOnClickListener {
            if (player.data?.data?.audio_file != null) {
                if (audioMediaPlayer.isPlaying) {
                    audioMediaPlayerPause()
                    isPlayingStatus = false
                    runnableStatusAudio = false
                } else {
                    isPlayingStatus = true
                    runnableStatusAudio = true
                    audioMediaPlayerPlay()
                    setAudioProgress()
                }
            } else {
                Snackbar.make(binding.root.rootView, "Audio fayl topilmadi", Snackbar.LENGTH_LONG)
                    .show()
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
            }

        })

        seekbarVideo.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mediaPlayer.exoPlayer?.seekTo(progress.toLong())
                seekbarVideo.progress = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
        imgLike.setOnClickListener {
            setObServerLike("1")
        }
        imgDislike.setOnClickListener {
            setObServerLike("2")
        }
        imgVideoPlayAndPause.setOnClickListener {
            if (mediaPlayer.exoPlayer?.isPlaying == true) {
                videoPlayerPause()
            } else {
                videoPlayerPlay()
            }
        }
        llVideoPdf.setOnClickListener {
            try {
                val file = File(pdfFilePath)
                if (file.exists()) {

                    val uri = FileProvider.getUriForFile(
                        this@PlayerActivity,
                        "$packageName.provider",
                        file
                    )
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(uri, "application/pdf")
                    intent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TOP.or(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@PlayerActivity, "Fayl yuklanmoqda", Toast.LENGTH_SHORT)
                        .show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@PlayerActivity,
                    "Siz pdf o'qiydigan dastur topilmadi",
                    Toast.LENGTH_SHORT
                ).show()
                e.printStackTrace()
            }
        }
    }

    private fun audioMediaPlayerPlay() = with(binding) {
        videoPlayerPause()
        audioMediaPlayer.start()
        imgAudioPalyAndPause.setImageResource(R.drawable.exo_icon_pause)
        imgAudioPalyAndPause.setBackgroundResource(R.drawable.background_audio)
    }

    private fun audioMediaPlayerPause() = with(binding) {
        audioMediaPlayer.pause()
        imgAudioPalyAndPause.setImageResource(R.drawable.ic_headset)
        imgAudioPalyAndPause.setBackgroundResource(R.drawable.background_audio)
    }

    private fun videoPlayerPlay() = with(binding) {
        imgVideoPlayAndPause.setImageResource(R.drawable.exo_icon_pause)
        imgVideoPlayAndPause.setBackgroundResource(R.drawable.background_video)
        mediaPlayer.startPlayer()
        audioMediaPlayerPause()
    }

    private fun videoPlayerPause() = with(binding) {
        imgVideoPlayAndPause.setImageResource(R.drawable.ic_youtube)
        imgVideoPlayAndPause.setBackgroundResource(R.drawable.background_video)
        mediaPlayer.pausePlayer()
    }

    private fun playAudio() {
        if (player.data?.data?.audio_file != null) {
            val audioUri = Uri.parse(player.data?.data?.audio_file)
            audioMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
            audioMediaPlayer.reset()

            try {
                audioMediaPlayer.setDataSource(this, audioUri)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            audioMediaPlayer.prepareAsync()
            audioMediaPlayer.setOnPreparedListener {
//            audioMediaPlayer.start()
                setAudioProgress()

            }
//        audioMediaPlayer.setOnBufferingUpdateListener { mp, percent ->
//            val ration = percent / 100.0
//            val bufferLevel = mp.duration * ration.toInt()
//            binding.seekbarAudio.secondaryProgress = bufferLevel
//        }
        } else {
            Toast.makeText(this, "Audio fayl topilmadi", Toast.LENGTH_SHORT).show()
        }

    }

    private lateinit var audioMediaPlayer: android.media.MediaPlayer
    private val handler = Handler()
    private var runnableStatusAudio = false
    private var runnableStatusVideo = true
    private var step = 0
    private var currentPositon = 0
    private var runStatus = false

    @SuppressLint("NewApi")
    private fun setAutoScroll() {
//        binding.nested.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
//            runStatus = scrollY >= oldScrollY
////                Toast.makeText(this, "$runStatus $scrollY $oldScrollY", Toast.LENGTH_SHORT).show()
//        }
//        var total_duration = audioMediaPlayer.currentPosition
//        val totalSekund = audioMediaPlayer.duration / 1000
//        val runnable = object : Runnable {
//            override fun run() {
//                if (runStatus) {
//                    if (!audioMediaPlayer.isPlaying) {
//                        runStatus = false
//                        nested.smoothScrollTo(0, 0)
//                    }
//                    binding.apply {
//                        step = nested.getChildAt(0).height / totalSekund
//                        currentPositon = step*audioMediaPlayer.currentPosition/1000
//                        Log.d(TAG, "run: $step $currentPositon")
//                        nested.smoothScrollTo(
//                            0,
//                            currentPositon
//                        )
//                    }
//                    handler.postDelayed(this, 1000)
//                }
//            }
//        }
//        handler.postDelayed(runnable, 1000)
    }

    private fun setAudioProgress() {
        try {
            //get the video duration
            var current_pos = audioMediaPlayer.currentPosition
            val total_duration = audioMediaPlayer.duration
            //display video duration
            binding.tvAudioEndTime.text = timeConversion(total_duration.toLong())
            binding.tvAudioStartTime.text = timeConversion(current_pos.toLong())
            binding.seekbarAudio.max = total_duration
            val runnable = object : Runnable {
                override fun run() {
                    try {
                        if (runnableStatusAudio) {
                            current_pos = audioMediaPlayer.currentPosition
                            binding.tvAudioStartTime.text = timeConversion(current_pos.toLong())
                            binding.seekbarAudio.progress = current_pos
                            handler.postDelayed(this, 1000)
                        }
                    } catch (ed: IllegalStateException) {
                        ed.printStackTrace()
                    }
                }
            }
            handler.postDelayed(runnable, 1000)
            //seekbar change listner
            binding.seekbarAudio.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar,
                    progress: Int,
                    fromUser: Boolean
                ) {
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    current_pos = seekBar.progress
                    audioMediaPlayer.seekTo(current_pos)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setVideoProgress() {
        try {
            //get the video duration
            var current_pos = mediaPlayer.exoPlayer?.currentPosition
            var total_duration = mediaPlayer.exoPlayer?.duration
            //display video duration
            binding.tvVideoEndTime.text = timeConversion(total_duration!!)
            binding.tvVideoStartTime.text = timeConversion(current_pos!!)
            val runnable = object : Runnable {
                override fun run() {
                    try {
                        if (runnableStatusVideo) {
                            if (mediaPlayer.exoPlayer?.isPlaying == false) {
                                binding.imgVideoPlayAndPause.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        this@PlayerActivity,
                                        R.drawable.ic_play_blue
                                    )
                                )
                            } else {
                                if(audioMediaPlayer.isPlaying){
                                    audioMediaPlayerPause()
                                }
                                binding.imgVideoPlayAndPause.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        this@PlayerActivity,
                                        R.drawable.ic_pause_blue
                                    )
                                )
                            }
                            total_duration = mediaPlayer.exoPlayer?.duration
                            current_pos = mediaPlayer.exoPlayer?.currentPosition
                            binding.tvVideoEndTime.text =
                                timeConversion(mediaPlayer.exoPlayer?.duration!!)
                            binding.tvVideoStartTime.text = timeConversion(current_pos!!)
                            binding.seekbarVideo.max = total_duration!!.toInt()
                            binding.seekbarVideo.progress = current_pos!!.toInt()
                            handler.postDelayed(this, 1000)
                        }
                    } catch (ed: IllegalStateException) {
                        ed.printStackTrace()
                    }
                }
            }
            handler.postDelayed(runnable, 1000)
            //seekbar change listner
            binding.seekbarVideo.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar,
                    progress: Int,
                    fromUser: Boolean
                ) {
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    current_pos = seekBar.progress.toLong()
                    mediaPlayer.exoPlayer?.seekTo(seekBar.progress.toLong())
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onBackPressed() {
        if (videoOrintration) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            supportActionBar?.show()
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            normalScreenButton!!.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_fullscreen_close
                )
            )
            binding.videoView.isVisible = true
            binding.videoViewLandscape.isVisible = false
            PlayerView.switchTargetView(
                mediaPlayer.exoPlayer!!,
                binding.videoViewLandscape,
                binding.videoView
            )
        } else {
            setAutoScroll()
            runStatus = false
            runnableStatusAudio = false
            runnableStatusVideo = false
            audioMediaPlayer.stop()
            MediaPlayer.stopPlayer()
            this.finish()
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

    private var imgAudioTimer = object : CountDownTimer(3000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
        }

        override fun onFinish() {
            binding.cardVolumeControl.visibility = View.GONE
        }
    }

    private val volumeControlTimer = object : CountDownTimer(3000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
        }

        override fun onFinish() {
            binding.cardVolumeSeekbar.visibility = View.GONE
        }
    }
    private var imgAudioLandscaperTimer = object : CountDownTimer(3000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
        }

        override fun onFinish() {
            binding.cardVolumeControlLandscape.isVisible = false
        }
    }
    private val volumeControlLandscapeTimer = object : CountDownTimer(3000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
        }

        override fun onFinish() {
            binding.cardVolumeSeekbarLandscape.isVisible = false
        }
    }

    private fun initData() {
        lifecycleScope.launchWhenCreated {
            viewmodel.initLesson(id)
            viewmodel.playerSateFlow.collect {
                when (it) {
                    is PlayerViewModel.PlayerUiSate.Success -> {
                        if (it.status) {
                            dataStatus = false
                            player = it.model
                            val videoFileName =
                                player.data!!.data.video_file!!.substringAfterLast("/")

                            downoloadFile(
                                player.data?.data?.video_file ?: "",
                                videoFileName,
                            )
                            downloadPDF()
                            playAudio()
                            binding.tvTitle.text = player.data?.data?.file_name ?: ""
                            binding.tvLikeCount.text = player.data?.like ?: ""
                            binding.tvDislikeCount.text = player.data?.dislike ?: ""
                            binding.tvWord.text = player.data?.data?.content ?: ""
                            binding.pdfTitle.text = player.data?.data?.file_name ?: ""
                            binding.toolbar.tvTitle.text = player.data?.data?.title ?: ""

                        }
                    }
                    is PlayerViewModel.PlayerUiSate.Error -> {
                        showDialog(it.error)
                    }
                }
            }
        }
    }

    private var pdfFilePath = ""
    private fun downloadPDF() {

        val random = (100..999).random()
        pdfFilePath = "/storage/emulated/0/Download/${player.data?.data?.file_name ?: random}.pdf"
        val downloadId = PRDownloader.download(
            player.data?.data?.text_file,
            "/storage/emulated/0/Download/",
            "${player.data?.data?.file_name ?: random}.pdf"
        )
            .build()
            .setOnStartOrResumeListener { }
            .setOnPauseListener { }
            .setOnProgressListener {
                val procent = it.currentBytes * 100.0 / it.totalBytes
            }
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {

                }

                override fun onError(error: com.downloader.Error) {

                }

                fun onError(error: Error) {
                }
            })
    }

    private fun downoloadFile(url: String, fileName: String) {
        val path = applicationContext.filesDir
        val letDirectory = File(path, ".usc0der")
        letDirectory.mkdirs()
        val paths = letDirectory.absolutePath
        val file = File("$paths/$fileName")
        if (!file.exists()) {
            initPlayer(player.data?.data?.video_file!!)

            val downloadId = PRDownloader.download(url, paths, fileName)
                .build()
                .setOnStartOrResumeListener { }
                .setOnPauseListener { }
                .setOnProgressListener {
                    val procent = it.currentBytes * 100.0 / it.totalBytes
                }
                .start(object : OnDownloadListener {
                    override fun onDownloadComplete() {
                        val model = RoomModel(id, "$paths/$fileName", "")
                        model.extension = ""
                        database.roomModel().insert(model)
                    }

                    override fun onError(error: com.downloader.Error) {
                    }

                    fun onError(error: Error) {
                    }
                })
        } else {
            try {
                val paths = database.roomModel().getObjRegionById(id).path
                player.data?.data?.file_path = paths
                initPlayer(paths)

            } catch (e: Exception) {
                initPlayer(player.data?.data?.video_file!!)
                e.printStackTrace()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        imgAudioTimer.cancel()
        volumeControlTimer.cancel()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        val position = mediaPlayer.exoPlayer?.currentPosition ?: 0
        videoAppDatabase.videoPosition().insert(VideoDbModel(id, position))
//        MediaPlayer.pausePlayer()
    }

    override fun onStop() {
        val position = mediaPlayer.exoPlayer?.currentPosition ?: 0
        videoAppDatabase.videoPosition().insert(VideoDbModel(id, position))
//        audioMediaPlayer.stop()
//        MediaPlayer.stopPlayer()
        super.onStop()
    }

    private fun showDialog(message: String) {
        val bind: DateOffDialogBinding = DateOffDialogBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this)
        dialog.setView(bind.root)
        val builder = dialog.create()
        bind.tvMessage.text = message
        bind.tvTitle.text = "Assalomu alaykum\n${App.mApp.getUserLogin()}"
        bind.tvExit.setOnClickListener {
            finish()
        }
        bind.tvPhone.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_DIAL)
            callIntent.data = Uri.parse("tel:" + "+998997925638")
            startActivity(callIntent)
        }
        bind.tvTelegramBot.setOnClickListener {
            val uri = Uri.parse("https://t.me/ydapp_bot");
            val intent = Intent(Intent.ACTION_VIEW, uri);
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent);
            }
        }
        builder.show()
    }

    override fun onClick(position: Int, id: Int) {
        sharedPref.setCommentId(id)
        commentChatFragment.dismiss()
        replayChatFragment = ReplayChatFragment(this)
        replayChatFragment.show(supportFragmentManager, "TAG")
    }

    override fun onBackItemClick() {
        replayChatFragment.dismiss()
        commentChatFragment = CommentChatFragment(this)
        commentChatFragment.show(supportFragmentManager, "TAG")
    }

    private fun setObServerLike(likeType: String) {
        lifecycleScope.launchWhenStarted {
            val hashMap = HashMap<String, Any>()

            val body = MultipartBody.Builder()
            body.setType(MultipartBody.FORM)
            body.addFormDataPart("item_id", id.toString())
            hashMap["item_id"] = id.toString()
            hashMap["username"] = App.mApp.getUserLogin().toString()
            hashMap["like_type"] = likeType
            likeAndDislikeViewModel.likeAndDislike(hashMap)
            likeAndDislikeViewModel.likeState.collect {
                when (it) {
                    is UIState.Error -> {
                    }
                    is UIState.Success -> {
                        binding.tvLikeCount.text = it.data.like
                        binding.tvDislikeCount.text = it.data.dislike
                    }
                }
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val bind: View = binding.toolbar.root
        if (newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE) {
            landscape()
            bind.isVisible = false
            binding.btnCommentChat.isVisible = false

        } else if (newConfig.orientation === Configuration.ORIENTATION_PORTRAIT) {
            portrait()
            bind.isVisible = true
            binding.btnCommentChat.isVisible = true

        }
    }

//    private fun landscape() {
//        val fullScreen = RelativeLayout.LayoutParams(
//            ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.MATCH_PARENT
//        )
//        binding.videoView.layoutParams = fullScreen
//        window.decorView.systemUiVisibility =
//            (View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
//    }
//
//    private fun portrait() {
//        val layoutParams = binding.videoView.layoutParams
//        layoutParams.height = (300 * this.resources.displayMetrics.density).toInt()
//        binding.videoView.layoutParams = layoutParams
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
//    }

    private var playerViewFullscreen: PlayerView? = null
    private var fullScreenButton: ImageView? = null
    private var normalScreenButton: ImageView? = null
    fun preparePlayer(playerView: PlayerView, forceLandscape: Boolean) {
        (playerView.context as AppCompatActivity).apply {
            playerViewFullscreen = PlayerView(this)
            val layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            playerViewFullscreen!!.layoutParams = layoutParams
            playerViewFullscreen!!.visibility = View.GONE
            playerViewFullscreen!!.setBackgroundColor(Color.BLACK)
            playerViewFullscreen!!.setShowNextButton(false)
            playerViewFullscreen!!.setShowPreviousButton(false)
            (playerView.rootView as ViewGroup).apply { addView(playerViewFullscreen, childCount) }
            fullScreenButton = playerView.findViewById(R.id.exo_fullscreen_icon)
            normalScreenButton = binding.videoViewLandscape.findViewById(R.id.exo_fullscreen_icon)
            fullScreenButton!!.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_fullscreen_open
                )
            )
            normalScreenButton!!.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_fullscreen_close
                )
            )
            fullScreenButton!!.setOnClickListener {
                window.decorView.systemUiVisibility =
                    (View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
                supportActionBar?.hide()
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                playerView.visibility = View.GONE
                binding.videoViewLandscape.visibility = View.VISIBLE
                PlayerView.switchTargetView(
                    mediaPlayer.exoPlayer!!,
                    playerView,
                    playerViewFullscreen
                )
            }
            normalScreenButton!!.setOnClickListener {

                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                supportActionBar?.show()
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                normalScreenButton!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_fullscreen_close
                    )
                )
                playerView.visibility = View.VISIBLE
                binding.videoViewLandscape.visibility = View.GONE
                PlayerView.switchTargetView(
                    mediaPlayer.exoPlayer!!,
                    binding.videoViewLandscape,
                    playerView
                )
            }
            playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT
            playerView.player = mediaPlayer.exoPlayer!!
        }
    }

    fun portrait() {
        videoOrintration = false
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        supportActionBar?.show()
//        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        normalScreenButton!!.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.ic_fullscreen_close
            )
        )
        binding.rlPortrait.isVisible = true
        binding.rlLandscape.isVisible = false
        PlayerView.switchTargetView(
            mediaPlayer.exoPlayer!!,
            binding.videoViewLandscape,
            binding.videoView
        )
    }

    private var videoOrintration = false
    fun landscape() {
        videoOrintration = true
        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        supportActionBar?.hide()
//            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
//        binding.videoView.visibility = View.GONE
//        playerViewFullscreen!!.visibility = View.VISIBLE
        binding.rlPortrait.isVisible = false
        binding.rlLandscape.isVisible = true
        binding.videoViewLandscape.setShowPreviousButton(false)
        binding.videoViewLandscape.setShowNextButton(false)
        PlayerView.switchTargetView(
            mediaPlayer.exoPlayer!!,
            binding.videoView,
            binding.videoViewLandscape
        )
    }
}
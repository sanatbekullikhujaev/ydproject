package com.usc0der.ydprojectnew.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.usc0der.ydprojectnew.R
import com.usc0der.ydprojectnew.adapter.VideosAndAudiosAdapter
import com.usc0der.ydprojectnew.api.network.ApiClient
import com.usc0der.ydprojectnew.api.network.ApiHelper
import com.usc0der.ydprojectnew.api.network.ApiService
import com.usc0der.ydprojectnew.api.network.UIState
import com.usc0der.ydprojectnew.api.viewmodel.VideoAndAudioViewModel
import com.usc0der.ydprojectnew.api.viewmodel.VideoAndAudioViewModelFactory
import com.usc0der.ydprojectnew.app.App
import com.usc0der.ydprojectnew.databinding.ActivityMediaVideoBinding
import com.usc0der.ydprojectnew.databinding.DateOffDialogBinding
import com.usc0der.ydprojectnew.model.HomeModel
import com.usc0der.ydprojectnew.utils.NetworkLiveData
import com.usc0der.ydprojectnew.utils.SharePref
import com.usc0der.ydprojectnew.utils.SharedPref
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MediaVideoActivity : AppCompatActivity(), VideosAndAudiosAdapter.OnItemClick {
    private var _binding: ActivityMediaVideoBinding? = null
    private val binding get() = _binding!!
    private val viewModel by lazy { initViewModel() }
    private val sharePref by lazy { SharePref(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMediaVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressbar.visibility = View.GONE
       binding.tvTitle.text =  intent.extras?.getString("type")
        NetworkLiveData(this).observe(this) {
            if (it) {
                setRecycler()
            } else {
                Toast.makeText(this, "Internet aloqasi yo'q", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setRecycler() {

        lifecycleScope.launch {
            viewModel.initVideo(sharePref.getLanguageId())
            viewModel.videoSateFlow.collect {
                when (it) {
                    is UIState.Success -> {
                        binding.recyclerView.adapter =
                            VideosAndAudiosAdapter(
                                it.data,
                                this@MediaVideoActivity,
                                this@MediaVideoActivity
                            )
                        binding.progressbar.isVisible = false
                    }
                    is UIState.Error -> {
                        showExceptionDialog(it.error)
                        binding.progressbar.isVisible = false
                    }
                    is UIState.Loading -> {
                        binding.progressbar.isVisible = true
                    }
                }
            }
        }
    }

    private fun showExceptionDialog(message: String) {
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


    private fun initViewModel() = ViewModelProvider(
        this, VideoAndAudioViewModelFactory(
            ApiHelper(
                ApiClient.createServiceWithToken(
                    ApiService::class.java, this
                )
            )
        )
    ).get(VideoAndAudioViewModel::class.java)

    override fun itemClick(position: Int, id: Int) {
        sharePref.setVideoOrAudioId(id)
//        NetworkLiveData(requireContext()).observe(viewLifecycleOwner, {
//            if (it) {
        val intent = Intent(this, PlayerActivity::class.java)
        intent.putExtra("key", "video")
        startActivity(intent)
//            } else {
//                Toast.makeText(requireContext(), "Internet aloqasi yo'q", Toast.LENGTH_SHORT).show()
//            }
//        })
    }
}
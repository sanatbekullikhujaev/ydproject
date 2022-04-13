package com.usc0der.ydprojectnew.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.usc0der.ydprojectnew.adapter.VideosAndAudiosAdapter
import com.usc0der.ydprojectnew.api.network.ApiClient
import com.usc0der.ydprojectnew.api.network.ApiHelper
import com.usc0der.ydprojectnew.api.network.ApiService
import com.usc0der.ydprojectnew.api.network.UIState
import com.usc0der.ydprojectnew.api.viewmodel.VideoAndAudioViewModel
import com.usc0der.ydprojectnew.api.viewmodel.ShareViewModel
import com.usc0der.ydprojectnew.api.viewmodel.VideoAndAudioViewModelFactory
import com.usc0der.ydprojectnew.app.App
import com.usc0der.ydprojectnew.databinding.DateOffDialogBinding
import com.usc0der.ydprojectnew.databinding.FragmentVideosBinding
import com.usc0der.ydprojectnew.utils.SharePref
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.usc0der.ydprojectnew.utils.NetworkLiveData

class VideosFragment : Fragment(), VideosAndAudiosAdapter.OnItemClick {
    private var _binding: FragmentVideosBinding? = null
    private val binding get() = _binding!!
    private val viewModel by lazy { initViewModel() }
    private val shareViewModel: ShareViewModel by activityViewModels()
    private lateinit var sharePref: SharePref
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVideosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        sharePref = SharePref(requireContext())
        binding.progressbar.visibility = View.GONE
        NetworkLiveData(requireContext()).observe(viewLifecycleOwner) {
            if (it) {
                setRecycler()
            } else {
                Toast.makeText(requireContext(), "Internet aloqasi yo'q", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setRecycler() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.initVideo(sharePref.getLanguageId())
            viewModel.videoSateFlow.collect {
                when (it) {
                    is UIState.Success -> {
                        binding.recyclerView.adapter =
                            VideosAndAudiosAdapter(
                                it.data,
                                requireContext(),
                                this@VideosFragment
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
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setView(bind.root)
        val builder = dialog.create()
        bind.tvMessage.text = message
        bind.tvTitle.text = "Assalomu alaykum\n${App.mApp.getUserLogin()}"
        bind.tvExit.setOnClickListener {
            requireActivity().finish()
        }
        bind.tvPhone.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_DIAL)
            callIntent.data = Uri.parse("tel:" + "+998997925638")
            startActivity(callIntent)
        }
        bind.tvTelegramBot.setOnClickListener {
            val uri = Uri.parse("https://t.me/ydapp_bot");
            val intent = Intent(Intent.ACTION_VIEW, uri);
            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(intent);
            }
        }
        builder.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initViewModel() = ViewModelProvider(
        requireActivity(), VideoAndAudioViewModelFactory(
            ApiHelper(
                ApiClient.createServiceWithToken(
                    ApiService::class.java, requireContext()
                )
            )
        )
    ).get(VideoAndAudioViewModel::class.java)
    override fun itemClick(position: Int, id: Int) {
        sharePref.setVideoOrAudioId(id)
//        NetworkLiveData(requireContext()).observe(viewLifecycleOwner, {
//            if (it) {
        val intent = Intent(requireContext(), PlayerActivity::class.java)
        intent.putExtra("key", "video")
        startActivity(intent)
//            } else {
//                Toast.makeText(requireContext(), "Internet aloqasi yo'q", Toast.LENGTH_SHORT).show()
//            }
//        })
    }
}

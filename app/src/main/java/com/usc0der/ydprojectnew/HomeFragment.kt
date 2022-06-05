package com.usc0der.ydprojectnew

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar

import com.usc0der.ydprojectnew.adapter.HomeAdapter
import com.usc0der.ydprojectnew.api.network.ApiClient
import com.usc0der.ydprojectnew.api.network.ApiHelper
import com.usc0der.ydprojectnew.api.network.ApiService
import com.usc0der.ydprojectnew.api.network.UIState
import com.usc0der.ydprojectnew.api.viewmodel.HomeViewModel
import com.usc0der.ydprojectnew.api.viewmodel.HomeViewModelFactory
import com.usc0der.ydprojectnew.databinding.FragmentHomeBinding
import com.usc0der.ydprojectnew.model.HomeModel
import com.usc0der.ydprojectnew.ui.MediaActivity
import com.usc0der.ydprojectnew.ui.MediaVideoActivity
import com.usc0der.ydprojectnew.utils.SharePref
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.usc0der.ydprojectnew.utils.NetworkLiveData


class HomeFragment : Fragment(), HomeAdapter.OnItemClick {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    private val viewModel by lazy { initViewModel() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!checkPermission(requireContext())) {
            requestPermission(requireContext())
        }

        NetworkLiveData(requireContext()).observe(viewLifecycleOwner) {
            if (it) {
                setRecycler()
            } else {
                Toast.makeText(requireContext(), "Internet aloqasi yo'q", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val PERMISSION_REQUEST_CODE = 101
    private fun requestPermission(context: Context) {
        val activity = context as Activity
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory("android.intent.category.DEFAULT")
                intent.data = Uri.parse(
                    String.format(
                        "package:%s",
                        context.getApplicationContext().packageName
                    )
                )
                activity.startActivityForResult(intent, 2296)
            } catch (e: Exception) {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                activity.startActivityForResult(intent, 2296)
            }
        } else {
//below android 11
            ActivityCompat.requestPermissions(
                context,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun checkPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val read = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            val write = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            read == PackageManager.PERMISSION_GRANTED && write == PackageManager.PERMISSION_GRANTED
        }
    }


    private fun setRecycler() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.homeSate.collect {
                when (it) {
                    is UIState.Success -> {
                        binding.recyclerView.adapter =
                            HomeAdapter(it.data.list, this@HomeFragment, requireContext())
                        binding.progressRecycler.isVisible = false
                    }
                    is UIState.Error -> {
                        binding.progressRecycler.isVisible = false
                    }
                    is UIState.Loading -> {
                        binding.progressRecycler.isVisible = true
                    }
                }
            }
        }
    }

    private fun initViewModel() = ViewModelProvider(
        this, HomeViewModelFactory(
            ApiHelper(
                ApiClient.createServiceWithToken(
                    ApiService::class.java,
                    requireContext()
                )
            )
        )
    ).get(HomeViewModel::class.java)
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun itemClick(position: Int, id: Int, model: HomeModel) {
        val sharePref = SharePref(requireContext())
        sharePref.setLanguageId(id)
        if (model.block_audio && model.block_video) {
            val intent = Intent(requireContext(), MediaActivity::class.java)
//            intent.putExtra("type", "Audio darslar")
            startActivity(intent)
        } else if (!model.block_audio && model.block_video) {
            val intent = Intent(requireContext(), MediaVideoActivity::class.java)
            intent.putExtra("type", false)
            startActivity(intent)
        } else if (model.block_audio && !model.block_video) {
            val intent = Intent(requireContext(), MediaVideoActivity::class.java)
            intent.putExtra("type", true)
            startActivity(intent)
        } else {
            Snackbar.make(
                requireView(),
                "Admin tomonidan bloklangan",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

}
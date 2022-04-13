package com.usc0der.ydprojectnew.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.usc0der.ydprojectnew.R
import com.usc0der.ydprojectnew.adapter.ReplayAdapter
import com.usc0der.ydprojectnew.api.network.ApiClient
import com.usc0der.ydprojectnew.api.network.ApiHelper
import com.usc0der.ydprojectnew.api.network.ApiService
import com.usc0der.ydprojectnew.api.viewmodel.ReplayViewModel
import com.usc0der.ydprojectnew.api.viewmodel.ReplayViewModelFactory
import com.usc0der.ydprojectnew.app.App
import com.usc0der.ydprojectnew.databinding.AddDialogBinding
import com.usc0der.ydprojectnew.databinding.ContextMenuBottomSheetBinding
import com.usc0der.ydprojectnew.databinding.DeleteAndEditBottomSheetBinding
import com.usc0der.ydprojectnew.databinding.FragmentReplayChatBinding
import com.usc0der.ydprojectnew.model.*
import com.usc0der.ydprojectnew.utils.SharedPref
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ReplayChatFragment(val onItemClick: OnItemClick) : BottomSheetDialogFragment(),
    ReplayAdapter.OnItemClick {

    private var _binding: FragmentReplayChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ReplayViewModel
    private lateinit var pref: SharedPref
    private var list = ArrayList<ReplayObj>()
    private var adapter: ReplayAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentReplayChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pref = SharedPref(requireContext())
        viewModel = ViewModelProvider(
            this, ReplayViewModelFactory(
                ApiHelper(
                    ApiClient.createServiceWithToken(
                        ApiService::class.java,
                        requireContext()
                    )
                )
            )
        ).get(ReplayViewModel::class.java)
        sendReplayCommentObserver()

        binding.imgOnBack.setOnClickListener {
            onItemClick.onBackItemClick()
        }
        binding.rlAddReplay.setOnClickListener {
            showAddReplayDialog()
        }
        initList()

    }

    private fun initList() {
        binding.progressbar.visibility = View.VISIBLE
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.initAllReplay(pref.getCommentId())
            viewModel.replaySateFlow.collect {
                when (it) {
                    is ReplayViewModel.ReplayUiState.AllReplay -> {
                        list = it.data
                        setRecycler()
                        binding.progressbar.visibility = View.GONE
                    }
                }
                binding.progressbar.visibility = View.GONE
            }
        }
    }
    private fun setRecycler(){
        adapter = ReplayAdapter(list, this@ReplayChatFragment)
        binding.recyclerView.adapter = adapter
    }

    private fun showAddReplayDialog() {
        val bind: AddDialogBinding = AddDialogBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
        dialog.setView(bind.root)
        val builder = dialog.create()
        bind.cardOk.setOnClickListener {
            val text = bind.etMessage.text.toString()
            if (!text.isEmpty()) {
                val sendReplay = SendReplay(pref.getCommentId(), App.mApp.getUserLogin().toString(), text)
                viewModel.sendReplay(sendReplay)
                builder.dismiss()
            }
        }
        bind.cardCancel.setOnClickListener {
            builder.dismiss()
        }
        builder.show()
    }


    private fun sendReplayCommentObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.sendReplay.collect {
                when (it) {
                    is ReplayViewModel.ReplayUiState.SendReplay -> {
                        val statusReplay = it.data
                        statusReplay.created_date = getCurrentTime()
                        list.add(ReplayObj(statusReplay))
                        if(list.size == 1){
                            setRecycler()
                        }
                        adapter?.notifyDataSetChanged()
                    }
                    is ReplayViewModel.ReplayUiState.Error -> {
                        toast(it.error)
                    }

                }
            }
        }
    }

    private fun editComment(
        position: Int,
        id: Int,
        update: Update
    ) {
        lifecycleScope.launchWhenCreated {
            viewModel.sendUpdate(id, update)
            viewModel.update.collect {
                when (it) {
                    is ReplayViewModel.ReplayUiState.Update -> {
                        list.set(position, ReplayObj(it.status))
                        adapter?.notifyDataSetChanged()
                    }
                    is ReplayViewModel.ReplayUiState.Error -> {
                        toast(it.error)
                    }

                }
            }
        }
    }

    private fun deleteReplay(position: Int, id: Int) {
        lifecycleScope.launchWhenCreated {
            viewModel.delete(id)
            viewModel.delete.collect {
                when (it) {
                    is ReplayViewModel.ReplayUiState.Delete -> {
                        if (it.status) {
                            toast("O'chirildi")
                            list.removeAt(position)
                            adapter?.notifyDataSetChanged()
                        } else toast("Serverda hatolik yuz berdi")
                    }
                }
            }
        }
    }

    private fun toast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun itemClick(position: Int, id: Int, replayComment: ReplayComment) {
        showContextMenuBottomSheet(position, id, replayComment)
    }

    private fun showContextMenuBottomSheet(position: Int, id: Int, replayComment: ReplayComment) {
        val bind: ContextMenuBottomSheetBinding =
            ContextMenuBottomSheetBinding.inflate(layoutInflater)
        val menuBottomSheet = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        menuBottomSheet.setContentView(bind.root)
        bind.rlEdit.setOnClickListener {
            showEditCommentDialog(position, id, replayComment)
            menuBottomSheet.dismiss()
        }
        bind.rlDelete.setOnClickListener {
            showDeleteBottomSheet(position, id)
            menuBottomSheet.dismiss()

        }
        menuBottomSheet.show()
    }

    private fun showEditCommentDialog(position: Int, id: Int, replayComment: ReplayComment) {
        val bind: AddDialogBinding = AddDialogBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
        dialog.setCancelable(false)
        dialog.setView(bind.root)
        val builder = dialog.create()
        bind.etMessage.setText(replayComment.body)
        bind.cardOk.setOnClickListener {
            val text = bind.etMessage.text.toString()
            if (!text.isEmpty()) {
                val update = Update(text)
                editComment(position, id, update)
                builder.dismiss()
            }
        }
        bind.cardCancel.setOnClickListener {
            builder.dismiss()
        }
        builder.show()
    }

    private fun showDeleteBottomSheet(position: Int, id: Int) {
        val bind: DeleteAndEditBottomSheetBinding =
            DeleteAndEditBottomSheetBinding.inflate(layoutInflater)
        val deleteBottomSheet = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        deleteBottomSheet.setContentView(bind.root)
        deleteBottomSheet.setCancelable(false)
        bind.cardCancel.setOnClickListener {
            deleteBottomSheet.dismiss()
        }
        bind.cardOk.setOnClickListener {
            deleteReplay(position, id)
            deleteBottomSheet.dismiss()
        }
        deleteBottomSheet.show()
    }

    interface OnItemClick {
        fun onBackItemClick()
    }

    @SuppressLint("NewApi")
    fun getCurrentTime(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formatted = current.format(formatter)
        return formatted
    }
}
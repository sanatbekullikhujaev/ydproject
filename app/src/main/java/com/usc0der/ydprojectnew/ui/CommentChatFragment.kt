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
import com.usc0der.ydprojectnew.adapter.CommentAdapter
import com.usc0der.ydprojectnew.api.network.ApiClient
import com.usc0der.ydprojectnew.api.network.ApiHelper
import com.usc0der.ydprojectnew.api.network.ApiService
import com.usc0der.ydprojectnew.api.viewmodel.CommentViewModel
import com.usc0der.ydprojectnew.api.viewmodel.CommentViewModelFactory
import com.usc0der.ydprojectnew.app.App
import com.usc0der.ydprojectnew.databinding.AddDialogBinding
import com.usc0der.ydprojectnew.databinding.ContextMenuBottomSheetBinding
import com.usc0der.ydprojectnew.databinding.DeleteAndEditBottomSheetBinding
import com.usc0der.ydprojectnew.databinding.FragmentCommentChatBinding
import com.usc0der.ydprojectnew.model.Comment
import com.usc0der.ydprojectnew.model.CommentObj
import com.usc0der.ydprojectnew.model.SendComment
import com.usc0der.ydprojectnew.model.Update
import com.usc0der.ydprojectnew.utils.SharePref
import com.usc0der.ydprojectnew.utils.SharedPref
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CommentChatFragment(val onItemClick: OnItemClick) : BottomSheetDialogFragment(),
    CommentAdapter.OnItemClick {

    private var _binding: FragmentCommentChatBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy { initviewModel() }
    private lateinit var sharePref: SharePref
    private lateinit var pref: SharedPref
    private var list = ArrayList<CommentObj>()
    private var adapter: CommentAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCommentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharePref = SharePref(requireContext())
        pref = SharedPref(requireContext())

        initList()
        sendMessageObserver()
        binding.rlAddComment.setOnClickListener {
            showAddCommentDialog()
        }
    }

    private fun initList() {
        binding.progressbar.visibility = View.VISIBLE
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getAllComment.collect {
                when (it) {
                    is CommentViewModel.ChatUiSate.AllComment -> {
                        list = it.data
                        setRecycler()
                        binding.progressbar.visibility = View.GONE
                    }
                    is CommentViewModel.ChatUiSate.Error -> {
                        binding.progressbar.visibility = View.GONE
                        Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun showAddCommentDialog() {
        val bind: AddDialogBinding = AddDialogBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
        dialog.setView(bind.root)
        val builder = dialog.create()
        bind.cardOk.setOnClickListener {
            val text = bind.etMessage.text.toString()
            if (text.isNotEmpty()) {
                val comment = SendComment(
                    App.mApp.getUserLogin().toString(),
                    text,
                    sharePref.getVideoOrAudioId()
                )
                viewModel.sendComment(comment)
                builder.dismiss()
            }
        }
        bind.cardCancel.setOnClickListener {
            builder.dismiss()
        }
        builder.show()
    }

    private fun sendMessageObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.commentSateFlow.collect { it ->
                when (it) {
                    is CommentViewModel.ChatUiSate.SendCommentSuccess -> {
                        val statusComment = it.comment
                        statusComment.created_time = getCurrentTime()

                        val prob = ArrayList<CommentObj>()
                        prob.addAll(list)
                        list.clear()
                        list.add(CommentObj(statusComment, "0"))
                        list.addAll(prob)
                        setRecycler()
                    }
                    is CommentViewModel.ChatUiSate.Error -> {
                        Toast.makeText(requireContext(), "Chatni boshlang", Toast.LENGTH_SHORT)
                            .show()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun editComment(
        position: Int,
        id: Int,
        update: Update
    ) {
        var status = true
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.updateComment(id, update)
            viewModel.updateStateFlow.collect {
                when (it) {
                    is CommentViewModel.ChatUiSate.Update -> {
                        if (status) {
                            list.set(position, CommentObj(it.data, "0"))
                            adapter?.notifyDataSetChanged()
                            status = false
                        }
                    }
                    is CommentViewModel.ChatUiSate.Error -> {
                        toast(it.error)
                    }
                }
            }
        }
    }

    private fun setRecycler() {
        adapter = CommentAdapter(list, this@CommentChatFragment)
        binding.recyclerView.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    interface OnItemClick {
        fun onClick(position: Int, id: Int)
    }

    override fun onClick(position: Int, id: Int) {
        onItemClick.onClick(position, id)
    }

    override fun longClick(position: Int, id: Int, comment: Comment) {
        showContextMenuBottomSheet(position, id, comment)
    }

    private fun showContextMenuBottomSheet(position: Int, id: Int, comment: Comment) {
        val bind: ContextMenuBottomSheetBinding =
            ContextMenuBottomSheetBinding.inflate(layoutInflater)
        val menuBottomSheet = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        menuBottomSheet.setContentView(bind.root)
        bind.rlEdit.setOnClickListener {
            showEditCommentDialog(position, id, comment)
            menuBottomSheet.dismiss()
        }
        bind.rlDelete.setOnClickListener {
            showDeleteBottomSheet(position, id)
            menuBottomSheet.dismiss()

        }
        menuBottomSheet.show()
    }

    private fun showEditCommentDialog(position: Int, id: Int, comment: Comment) {
        val bind: AddDialogBinding = AddDialogBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
        dialog.setCancelable(false)
        dialog.setView(bind.root)
        val builder = dialog.create()
        bind.etMessage.setText(comment.body)
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
            deleteComment(position, id)
            deleteBottomSheet.dismiss()
        }
        deleteBottomSheet.show()
    }

    private fun deleteComment(position: Int, id: Int) {
        lifecycleScope.launchWhenCreated {
            viewModel.deleteComment(id)
            viewModel.deleteStateFlow.collect {
                when (it) {
                    is CommentViewModel.ChatUiSate.Delete -> {
                        if (it.status) {
                            toast("O'chirildi")
                            list.removeAt(position)
//                            setRecycler()
                            adapter?.notifyDataSetChanged()
                        } else toast("Serverda hatolik yuz berdi")
                    }
                }
            }
        }
    }

    private fun toast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    @SuppressLint("NewApi")
    fun getCurrentTime(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return current.format(formatter)
    }

    private fun initviewModel() = ViewModelProvider(
        this, CommentViewModelFactory(
            sharePref.getVideoOrAudioId(),
            ApiHelper(
                ApiClient.createServiceWithToken(
                    ApiService::class.java, requireContext()
                )
            )
        )
    ).get(CommentViewModel::class.java)
}
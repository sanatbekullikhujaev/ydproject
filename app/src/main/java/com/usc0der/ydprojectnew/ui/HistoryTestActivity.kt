package com.usc0der.ydprojectnew.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.usc0der.ydprojectnew.R
import com.usc0der.ydprojectnew.adapter.TestResultAdapter
import com.usc0der.ydprojectnew.api.network.ApiClient
import com.usc0der.ydprojectnew.api.network.ApiHelper
import com.usc0der.ydprojectnew.api.network.ApiService
import com.usc0der.ydprojectnew.api.network.UIState
import com.usc0der.ydprojectnew.api.viewmodel.TestViewModel
import com.usc0der.ydprojectnew.api.viewmodel.TestViewModelFactory
import com.usc0der.ydprojectnew.databinding.ActivityHistoryTestBinding
import com.usc0der.ydprojectnew.databinding.ActivityResultsBinding
import com.usc0der.ydprojectnew.model.HistoryTest
import kotlinx.coroutines.flow.collect

class HistoryTestActivity : AppCompatActivity() {
    private var _binding: ActivityHistoryTestBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TestViewModel
    private val list by lazy { ArrayList<HistoryTest>() }
    private lateinit var adapter: TestResultAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHistoryTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        binding.recyclerTest.layoutManager = LinearLayoutManager(this)
        binding.recyclerTest.setHasFixedSize(true)
        val historyId = intent.getIntExtra("historyId", 0)
        getHistory(historyId)


    }

    private fun getHistory(historyId: Int) {
        lifecycleScope.launchWhenStarted {
            viewModel.getHistoryTest(historyId)
            viewModel.historyTestState.collect {
                when (it) {
                    is UIState.Success -> {
                        binding.progressBar.isVisible = false
                        list.clear()
                        list.addAll(it.data)
                        adapter = TestResultAdapter(list)
                        binding.recyclerTest.adapter = adapter
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
        _binding = null
    }
}
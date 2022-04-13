package com.usc0der.ydprojectnew.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.usc0der.ydprojectnew.databinding.ActivityMediaBinding


class MediaActivity : AppCompatActivity() {


    private var _binding: ActivityMediaBinding?=null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgOnBack.setOnClickListener {
            onBackPressed()
        }
    }
    override fun onBackPressed() {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
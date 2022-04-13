package com.usc0der.ydprojectnew.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.mindev.mindev_pdfviewer.MindevPDFViewer
import com.mindev.mindev_pdfviewer.PdfScope
import com.usc0der.ydprojectnew.databinding.ActivityPdfViewBinding
import com.usc0der.ydprojectnew.utils.SharedPref

class PdfViewActivity : AppCompatActivity() {
    private var _binding:ActivityPdfViewBinding?=null
    private val binding get() = _binding!!
    private lateinit var sharedPref: SharedPref
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPdfViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = SharedPref(this)
        binding.pdfView.initializePDFDownloader(sharedPref.getPDFUrl(), statusListener)
        lifecycle.addObserver(PdfScope())
    }

    override fun onDestroy() {
        binding.pdfView.pdfRendererCore?.clear()
        _binding = null
        super.onDestroy()
    }
    private val statusListener = object : MindevPDFViewer.MindevViewerStatusListener {
        override fun onStartDownload() {
        }

        override fun onPageChanged(position: Int, total: Int) {
            binding.tvPageCount.text = "$position/$total"
        }

        override fun onProgressDownload(currentStatus: Int) {
            binding.tvProgress.text = "$currentStatus %"
        }

        override fun onSuccessDownLoad(path: String) {
            binding.progress.isVisible = false
            binding.tvProgress.isVisible = false
            binding.pdfView.fileInit(path)
        }

        override fun onFail(error: Throwable) {
            binding.progress.isVisible = false
            binding.tvProgress.isVisible = false
        }

        override fun unsupportedDevice() {
        }

    }
}
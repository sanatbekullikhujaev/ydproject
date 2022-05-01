package com.usc0der.ydprojectnew.auth

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.usc0der.ydprojectnew.databinding.ActivityRegistrationBinding
import com.jakewharton.rxbinding4.widget.textChanges
import com.usc0der.ydprojectnew.api.network.ApiClient
import com.usc0der.ydprojectnew.api.network.ApiHelper
import com.usc0der.ydprojectnew.api.network.ApiService
import com.usc0der.ydprojectnew.api.network.UIState
import com.usc0der.ydprojectnew.api.viewmodel.AuthViewModel
import com.usc0der.ydprojectnew.api.viewmodel.AuthViewModelFactory
import com.usc0der.ydprojectnew.app.App
import com.usc0der.ydprojectnew.eski_api.models.RegisterForm
import com.usc0der.ydprojectnew.eski_api.models.RegisterRequest
import com.usc0der.ydprojectnew.utils.SharePref
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.Function3
import kotlinx.coroutines.flow.collect
import com.usc0der.ydprojectnew.utils.NetworkLiveData

class RegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var viewModel: AuthViewModel
    private var deviceId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(
            this, AuthViewModelFactory(
                ApiHelper(
                    ApiClient.createService(
                        ApiService::class.java,
                        this
                    )
                )
            )
        ).get(AuthViewModel::class.java)

        initView()
        val sharePref = SharePref(this)
        if (sharePref.getRegCount() == 4) {
            binding.llException.isVisible = false
        } else if (sharePref.getRegCount() > 0) {
            binding.llException.isVisible = true
            binding.tvException.text =
                "Ro'yxatdan o'tish soni cheklangan.\nLogin parolni saqlab qoying.\nQayta ro'yxatdan o'tish imkoniyati bo'lmaydi!!!\nSizda yana ${sharePref.getRegCount()} marotaba ro'yxatdan o'ta olasiz"
            countDownTimer.start()
        }

    }

    private val cd = CompositeDisposable()
    private fun hideProgressBar() {
        binding.pbLogon.isVisible = false
        binding.clLogonac.isVisible = true
    }

    private fun showProgressBar() {
        binding.pbLogon.isVisible = true
        binding.clLogonac.isVisible = false
    }

    private fun showToast(text: String) {
        Toast.makeText(this@RegistrationActivity, text, Toast.LENGTH_SHORT).show()
    }

    private fun initView() {

        setUpValidate()


        binding.imgBack.setOnClickListener {
//            startLogonActivity()
            finish()
        }
        getDeviceId()
            binding.btnLoginRegister.setOnClickListener {
                val sharePref = SharePref(this)
                if (sharePref.getRegCount() > 0) {
                    NetworkLiveData(this).observe(this, {
                        if (it) {
                            setObserve()
                        } else {
                            Toast.makeText(this, "Internet aloqasi yo'q", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
    }


    private fun setUpValidate() {
        Observable.combineLatest<Boolean, Boolean, Boolean, Boolean>(

            binding.etLoginLoginac.textChanges()
                .skipInitialValue()
                .map { it.length > 3 }.doOnNext {
                    if (!it) {
                        binding.tilLoginLogin.error = "Login kamida 4 belgi bo'lishi kerak"
                    } else {
                        binding.tilLoginLogin.error = null
                    }
                },
            binding.etPasswordLoginac.textChanges().skipInitialValue()
                .map { binding.etPasswordLoginac.text.toString().length > 3 && binding.etPasswordLoginac.text.toString() == binding.etPasswordLoginac2.text.toString() }
                .doOnNext {
                    if (!it) {
                        if (binding.etPasswordLoginac.text.toString() != binding.etPasswordLoginac2.text.toString()) {
                            binding.tilLoginPass2.error = "Parolingiz mos kelmayapti !"
                        } else {
                            binding.tilLoginPass2.error = null
                        }
                        if (binding.etPasswordLoginac.text.toString().length < 4) {
                            binding.tilLoginPass.error = "Parol kamida 4 belgi bo'lish kerak"
                        } else {
                            binding.tilLoginPass.error = null
                        }
                    } else {
                        binding.tilLoginPass.error = null
                    }
                },
            binding.etPasswordLoginac2.textChanges().skipInitialValue()
                .map {
                    it.toString() == binding.etPasswordLoginac.text.toString()
                }
                .doOnNext {
                    if (!it) {
                        binding.tilLoginPass2.error = "Parolingiz mos kelmayapti !"
                    } else {
                        binding.tilLoginPass2.error = null
                        binding.etPasswordLoginac.text = binding.etPasswordLoginac.text
                    }
                },
            Function3 { t1, t2, t3 -> t1 && t2 && t3 })
            .doOnNext { binding.btnLoginRegister.isEnabled = it }
            .subscribe()
            .let { cd.add(it) }
    }


    override fun onStop() {
        super.onStop()
        cd.clear()
    }

    private fun startLogonActivity() {
        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    @SuppressLint("SetTextI18n")
    private fun setObserve() {
        val sharePref = SharePref(this)
        showProgressBar()
        lifecycleScope.launchWhenStarted {
            val reg = RegisterRequest(
                binding.etLoginLoginac.text.toString(),
                binding.etPasswordLoginac.text.toString(),
                "99999",
                "User",
                "$deviceId 21"
            )
            viewModel.reg(RegisterForm(reg))
            viewModel.regState.collect {
                when (it) {
                    is UIState.Success -> {

                        sharePref.setRegCount(it.data.data.count ?: sharePref.getRegCount() - 1)

                        hideProgressBar()
                        App.mApp.saveUserData(
                            binding.etLoginLoginac.text.toString(),
                            binding.etPasswordLoginac.text.toString()
                        )
                        startLogonActivity()
                    }
                    is UIState.LoginExample -> {
                        hideProgressBar()
                        binding.example.visibility = View.VISIBLE
                        if (it.example == "") {
                            showToast("Siz barcha imkoniyatlardan foydalanib bo'ldingiz")
                        } else {
                            showToast("Bunday login mavjud!")
                            binding.example.text =
                                "Quydagi variantlardan foydalaning\n${it.example}"
                        }
                    }
                    is UIState.Error -> {
                        showToast(it.error)
                    }

                }
            }
        }
    }

    private val countDownTimer = object : CountDownTimer(12000, 1000) {
        override fun onTick(millisUntilFinished: Long) {

        }

        override fun onFinish() {
            binding.llException.isVisible = false
        }
    }

    @SuppressLint("HardwareIds")
    private fun getDeviceId() {
        val androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
/*
* getDeviceId() returns the unique device ID.
* For example,the IMEI for GSM and the MEID or ESN for CDMA phones.
*/
/*
* getDeviceId() returns the unique device ID.
* For example,the IMEI for GSM and the MEID or ESN for CDMA phones.
*/
        deviceId = androidId
/*
* getSubscriberId() returns the unique subscriber ID,
* For example, the IMSI for a GSM phone.
*/
/*
* getSubscriberId() returns the unique subscriber ID,
* For example, the IMSI for a GSM phone.
*/


    }

}
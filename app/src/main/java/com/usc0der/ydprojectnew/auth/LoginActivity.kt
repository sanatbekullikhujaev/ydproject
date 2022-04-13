package com.usc0der.ydprojectnew.auth
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.text.Editable
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.firebase.messaging.FirebaseMessaging
import com.usc0der.ydprojectnew.MainActivity
import com.usc0der.ydprojectnew.SimpleTextWatcher
import com.usc0der.ydprojectnew.api.network.*
import com.usc0der.ydprojectnew.api.viewmodel.AuthViewModel
import com.usc0der.ydprojectnew.api.viewmodel.AuthViewModelFactory
import com.usc0der.ydprojectnew.app.App
import com.usc0der.ydprojectnew.databinding.ActivityLoginBinding
import com.usc0der.ydprojectnew.eski_api.models.LoginForm
import com.usc0der.ydprojectnew.eski_api.models.LoginRequest
import com.usc0der.ydprojectnew.utils.SharePref
import com.usc0der.ydprojectnew.utils.SharedPref
import kotlinx.coroutines.flow.collect
import com.usc0der.ydprojectnew.utils.NetworkLiveData


class LoginActivity : AppCompatActivity(), SimpleTextWatcher {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: AuthViewModel
    private var first: Boolean = false
    private val sharedPref by lazy { SharedPref(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayShowTitleEnabled(false)
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
        setObserve()
        initView()
    }

    private fun hideShowView(state: Boolean) {
        binding.consInputLayout.isVisible = state
        binding.btnLoginEnter.isVisible = state
        binding.imgTelegram.isVisible = state
        binding.cons.isVisible = state
        binding.ivPhoneLoginac.isVisible = state
    }

    @SuppressLint("HardwareIds")
    private fun setObserve() {
        binding.btnLoginEnter.setOnClickListener {
            NetworkLiveData(this).observe(this, {

                if (it) {
                    binding.pbLogon.visibility = View.VISIBLE
                    hideShowView(false)

                    val androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)?:""

                    FirebaseMessaging.getInstance().token.addOnCompleteListener { fm ->
                        if (fm.result != null) {
                            sharedPref.device_token = fm.result.toString()
                        }
                        Log.d("tokenssss", fm.result.toString())
                    }

                    lifecycleScope.launchWhenStarted {
                        val login = LoginRequest(
                            binding.etLoginLogina.text.toString(),
                            binding.etPasswordLoginac.text.toString(),
                            "$androidId 20",
                            sharedPref.device_token

                        )
                        viewModel.login(LoginForm(login))
                        viewModel.loginState.collect { it ->
                            when (it) {
                                is UIState.Success -> {
                                    binding.pbLogon.visibility = GONE
                                    TokenManager.getInstance(
                                        applicationContext.getSharedPreferences(
                                            "prefs",
                                            Context.MODE_PRIVATE
                                        )
                                    )?.saveToken(it.data.token)
                                    App.mApp.saveAutoEnter(true)
                                    App.mApp.saveUserData(
                                        binding.etLoginLogina.text.toString(),
                                        binding.etPasswordLoginac.text.toString()
                                    )
                                    SharePref(this@LoginActivity).setRegCount(it.data.count)

                                    startActivity(
                                        Intent(
                                            applicationContext,
                                            MainActivity::class.java
                                        )
                                    )
                                    finishAffinity()

                                }

                                is UIState.LoginExample -> {
                                    binding.pbLogon.visibility = View.GONE
                                    hideShowView(true)
                                    Toast.makeText(
                                        applicationContext,
                                        it.error + it.example,
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                    viewModel.reset()
                                }
                                else -> Unit
                            }
                        }

                    }
                } else {
                    Toast.makeText(this, "Internet aloqasi yo'q", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
    private fun initView() {
        val login: String? = App.mApp.getUserLogin()
        val password: String? = App.mApp.getUserPassword()
        binding.etLoginLogina.addTextChangedListener(this)
        binding.etPasswordLoginac.addTextChangedListener(this)
        binding.etLoginLogina.text = Editable.Factory.getInstance().newEditable(login ?: "")
        binding.etPasswordLoginac.text = Editable.Factory.getInstance().newEditable(password ?: "")

        binding.btnLoginRegister.setOnClickListener {
            startRegisterActivity()
        }

        binding.ivPhoneLoginac.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel: +998997925638"))
            startActivity(callIntent)
        }
        binding.imgTelegram.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/ydapp_bot"))
            startActivity(intent)
        }
//        hideShowRegBtn()
    }

//    private fun hideShowRegBtn() {
//        if (sharedPref.isRegSuccess()) {
//            binding.cons.isVisible = false
//        } else binding.cons.isVisible = sharedPref.isMainSuccess()
//    }


    override fun afterTextChanged(s: Editable?) {
        val login = binding.etLoginLogina.text.toString()
        val password = binding.etPasswordLoginac.text.toString()

        if (login.isNotEmpty() && password.isNotEmpty()) {
            binding.btnLoginEnter.isEnabled = true
        }

    }


    private fun startRegisterActivity() {
        val sharePref = SharePref(this)
        if (sharePref.getRegCount() > 0) {
            binding.llException.isVisible = false
            val intent = Intent(this@LoginActivity, RegistrationActivity::class.java)
            startActivityForResult(intent, 1001)
        } else {
            binding.llException.isVisible = true
            countDownTimer.start()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            binding.cons.visibility = GONE
        }
        val login: String? = App.mApp.getUserLogin()
        val password: String? = App.mApp.getUserPassword()
        binding.etLoginLogina.text = Editable.Factory.getInstance().newEditable(login ?: "")
        binding.etPasswordLoginac.text = Editable.Factory.getInstance().newEditable(password ?: "")
    }

    private val countDownTimer = object : CountDownTimer(7000, 1000) {
        override fun onTick(millisUntilFinished: Long) {

        }

        override fun onFinish() {
            binding.llException.isVisible = false
        }
    }


}
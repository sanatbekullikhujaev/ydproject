package com.usc0der.ydprojectnew.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.usc0der.ydprojectnew.databinding.ActivitySettingsBinding
import com.usc0der.ydprojectnew.MainActivity
import com.usc0der.ydprojectnew.api.network.*
import com.usc0der.ydprojectnew.api.viewmodel.SettingsViewModel
import com.usc0der.ydprojectnew.api.viewmodel.SettingsViewModelFactory
import com.usc0der.ydprojectnew.app.App
import com.usc0der.ydprojectnew.eski_api.models.*
import kotlinx.coroutines.flow.collect
import com.usc0der.ydprojectnew.eski_api.models.ChangeUser

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this, SettingsViewModelFactory(
                ApiHelper(
                    ApiClient.createServiceWithToken(
                        ApiService::class.java,
                        applicationContext
                    )
                )
            )
        ).get(SettingsViewModel::class.java)
        setObserve()
        binding.imgBack.setOnClickListener {
            finish()
        }
    }

    private fun setObserve() {


        binding.btnUpdate.setOnClickListener {
            if (binding.etPassword.text!!.isEmpty()) {
                binding.etPassword.error = "Parol kiritmadingiz"
                binding.etPassword.requestFocus()
            } else if (binding.etPassword.text!!.length < 6) {
                binding.etPassword.error = "Parol kamida 6 belgi bo`lishi kerak"
                binding.etPassword.requestFocus()
            } else if (binding.etPassword.text.toString() != binding.etPasswordConfirm.text.toString()) {
                binding.etPasswordConfirm.error = "Parolingiz mos kelmayabdi"
                binding.etPasswordConfirm.requestFocus()
            } else {
                binding.pbLogon.visibility = View.VISIBLE
                lifecycleScope.launchWhenStarted {
                    val pass = ChangeRequest(
                        binding.etPassword.text.toString()
                    )
                    viewModel.editPassword(ChangeUser(pass))
                    viewModel.editPasswordState.collect { it ->
                        when (it) {
                            is UIState.Success -> {
                                binding.pbLogon.visibility = View.GONE
                                TokenManager.getInstance(
                                    applicationContext.getSharedPreferences(
                                        "prefs",
                                        Context.MODE_PRIVATE
                                    )
                                )?.saveToken(it.data.token)
                                val login: String? = App.mApp.getUserLogin()
                                val password: String = binding.etPassword.text.toString()
                                App.mApp.saveUserData(login, password)
                                startActivity(Intent(applicationContext, MainActivity::class.java))
                                finish()
                            }

                            is UIState.Error -> {
                                binding.pbLogon.visibility = View.GONE
                                Toast.makeText(applicationContext, it.error, Toast.LENGTH_SHORT)
                                    .show()
                            }
                            is UIState.Loading -> {

                            }
                            else -> Unit
                        }
                    }

                }
            }

        }
    }
}
package com.usc0der.ydprojectnew

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.usc0der.ydprojectnew.app.App
import com.usc0der.ydprojectnew.auth.LoginActivity
import com.usc0der.ydprojectnew.utils.SharedPref
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class SplashActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPref
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        sharedPref = SharedPref(this)

//        if (sharedPref.deviceDate == "20220103"){
//            Toast.makeText(applicationContext, "NOOOOOOOOOOOOOOOO", Toast.LENGTH_SHORT).show()
//        }
//        else{
//
//        }

        if (App.mApp.getAutoEnter()) {
            Handler(Looper.getMainLooper()).postDelayed({
                startMainActivity()

            }, 2000)
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                startLoginActivity()
            }, 2000)
        }


//        startMainActivity()

    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun onMessageEvent(message: String?) {
//        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
////        textView.setText(message)
//    }

    private fun startLoginActivity() {
        val intent = Intent(this@SplashActivity, LoginActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    private fun startMainActivity() {
        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }


//    override fun onDestroy() {
//        super.onDestroy()
//        EventBus.getDefault().unregister(this)
//    }
//
//    override fun onStart() {
//        super.onStart()
//        if (!EventBus.getDefault().isRegistered(this))
//            EventBus.getDefault().register(this)
//    }
}
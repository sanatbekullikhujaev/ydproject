package com.usc0der.ydprojectnew.auth

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.usc0der.ydprojectnew.R

class TestActivityActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_activity)

        val filename = "myfile"
        val fileContents = "Hello Javlonbek!"
        applicationContext.openFileOutput(filename, Context.MODE_PRIVATE).use {
            it.write(fileContents.toByteArray())
        }

        applicationContext.openFileInput(filename).bufferedReader().useLines { lines ->
            lines.fold("") { some, text ->
                "$some\n$text"
            }
        }

    }
}
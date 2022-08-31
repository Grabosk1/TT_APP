package com.example.detailslist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
    }

    override fun onDestroy() {
        super.onDestroy()
        Debug.stopMethodTracing()
        finish()
    }
}
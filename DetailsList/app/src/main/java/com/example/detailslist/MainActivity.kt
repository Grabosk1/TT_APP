package com.example.detailslist

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.telephony.SmsManager
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.example.adapters.ViewPagerAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var fusedLocation: FusedLocationProviderClient
    private lateinit var mHandler: Handler

    private var x = String()
    private var y = String()
    private var bool = true
    private val phoneNumber = 609033378
    private var message = String()
    private val awaiting = Runnable {
        bool = true

    }
    val phone_number = 609033378


    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        val tabNames = listOf("MAIN", "EASY", "HARD")
        fusedLocation = LocationServices.getFusedLocationProviderClient(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tabLayout = findViewById<TabLayout>(R.id.tabs)
        val viewPager2 = findViewById<ViewPager2>(R.id.view_pager)

        val sharedPreferences: SharedPreferences = this.getSharedPreferences("shared",Context.MODE_PRIVATE)
        val editor:SharedPreferences.Editor =  sharedPreferences.edit()
        editor.putInt("number", phone_number)

        editor.apply()
        editor.commit()

        val adapter = ViewPagerAdapter(this)
        viewPager2.adapter = adapter
        getLoc()
        TabLayoutMediator(
            tabLayout, viewPager2
        ) { tab, position -> tab.text = tabNames[position] }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.custom_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.Search -> {
                openTourWebPage()
                true
            }
            R.id.SOS -> {
                sosSMS()
                true
            }
            else -> {
                true
            }
        }
    }

    private fun openTourWebPage() {
        val url = "https://www.snowcard.co.uk/blog/ten-best-treks-europe"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    private fun sosSMS() {

        // on below line we are creating two
        // variables for phone and message

        // on the below line we are creating a try and catch block
        getLoc()
        val sharedPreferences: SharedPreferences = this.getSharedPreferences("shared",Context.MODE_PRIVATE)
        val number = sharedPreferences.getInt("number",0)
        try {

            // on below line we are initializing sms manager.
            val smsManager: SmsManager = SmsManager.getDefault()

            smsManager.sendTextMessage(number.toString(), null, message, null, null)

            // on below line we are displaying a toast message for message send,
            Toast.makeText(applicationContext, "Message Sent", Toast.LENGTH_LONG).show()


        } catch (e: Exception) {

            // on catch block we are displaying toast message for error.
            Toast.makeText(
                applicationContext,
                "Please enter all the data.." + e.message.toString(),
                Toast.LENGTH_LONG
            )
                .show()
        }
    }

    private fun getLoc() {

        val task = fusedLocation.lastLocation


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
            return
        }
        val handler = Handler(Looper.getMainLooper())
        fun coordinatesss() {
            task.addOnSuccessListener {
                bool = false
                if (it != null) {
                    x = it.latitude.toString()
                    y = it.longitude.toString()

                } else {
                    x = "--"
                    y = "--"
                }

            }
        }

        coordinatesss()
        message = "SOS! I NEED HELP MY CORDS: X: $x Y: $y"
    }

}
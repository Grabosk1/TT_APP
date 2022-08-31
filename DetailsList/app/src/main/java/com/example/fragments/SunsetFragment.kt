package com.example.fragments

import android.Manifest
import android.content.Intent
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.example.detailslist.MainActivity
import com.example.detailslist.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class SunsetFragment : Fragment(){
    private lateinit var fusedLocation: FusedLocationProviderClient
    private lateinit var sceneView: View
    private lateinit var textView: View
    private lateinit var sunView: View
    private lateinit var tempView: TextView
    private lateinit var lottieView: LottieAnimationView

    private lateinit var heightAnimator: ObjectAnimator
    private lateinit var textAnimator: ObjectAnimator

    private lateinit var mHandler: Handler
    private lateinit var iHandler: Handler

    private val showTemperature = Runnable {
        tempView.isVisible = true

    }

    private val changeActivity = Runnable {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_sunset, container, false)

        sceneView = view

        sunView = view.findViewById(R.id.sun)
        sunView.isInvisible = true


        lottieView = view.findViewById(R.id.animation_view)

        tempView = view.findViewById(R.id.temp_view)
        tempView.isInvisible = true

        fusedLocation = LocationServices.getFusedLocationProviderClient(requireActivity())

        sceneView.setOnClickListener{ startAnimation() }

        return view
    }

    @SuppressLint("SetTextI18n")
    private fun getLocation() {
        val task = fusedLocation.lastLocation


        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101)
            return
        }

        task.addOnSuccessListener{
            if (it != null){
                mHandler = Handler()
                mHandler.postDelayed(showTemperature, 4000)
                tempView.text = "Aktualna lokalizacja: \n " +  "X: " + String.format("%.2f", it.latitude) +
                        "   Y: " + String.format("%.1f", it.longitude)
                tempView.gravity = 0
            }
        }
    }

    private fun startAnimation(){

        iHandler = Handler()
        iHandler.postDelayed(changeActivity, 9300)

        val sunStartX = sceneView.right.toFloat() + 50
        val sunEndX = sceneView.left.toFloat() - 500

        heightAnimator = ObjectAnimator.ofFloat(sunView, "x", sunStartX, sunEndX).setDuration(4800)
        heightAnimator.repeatCount = 1
        heightAnimator.start()



        val animatorSet = AnimatorSet()
        animatorSet.play(heightAnimator)
        animatorSet.start()
        sunView.isVisible = true
        lottieView.playAnimation()
        lottieView.repeatCount = 10
        getLocation()
    }
}
package com.example.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.example.detailslist.R
import com.example.models.StatItem
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso

class DetailFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mExampleList: ArrayList<StatItem>
    private lateinit var mRequestQueue: RequestQueue


    private var name: String? = null
    private var description: String? = null
    private var distance: String? = null
    private var image:String? = null
    private var geoX: Double? = null
    private var geoY: Double? = null
    private var rekordTime: Int = 999999
    private var rekordDate: String? = null
    private var counter: Int = 0
    private var showStats: Boolean = false

    companion object {
        fun newInstance(): DetailFragment {
            return DetailFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mExampleList = ArrayList()
        mRequestQueue = Volley.newRequestQueue(activity)
        if (savedInstanceState != null){
            name = savedInstanceState.getString("name")
            description = savedInstanceState.getString("description")
            distance = savedInstanceState.getString("distance")
            image = savedInstanceState.getString("image")
            geoX = savedInstanceState.getDouble("x")
            geoY = savedInstanceState.getDouble("y")
            showStats = savedInstanceState.getBoolean("showStats")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()
        val collapsingToolbarLayout =
            view?.findViewById<CollapsingToolbarLayout>(R.id.collapsingtoolbar_id)
        collapsingToolbarLayout?.isTitleEnabled = true
        val nameV = view?.findViewById<TextView>(R.id.aa_route_name)
        val descriptionV = view?.findViewById<TextView>(R.id.aa_description)
        val distanceV = view?.findViewById<TextView>(R.id.aa_distance)
        val imgV = view?.findViewById<ImageView>(R.id.aa_photo)
        val mapFragment = childFragmentManager.findFragmentById(R.id.aa_map) as SupportMapFragment

        view?.findViewById<FloatingActionButton?>(R.id.fab_button)?.setOnClickListener {
            //Stoper
            val ft = childFragmentManager.beginTransaction()
            val mFragment = WeatherFragment.newInstance()
            geoX?.let { it1 ->
                geoY?.let { it2 ->
                    mFragment.sendData(
                        it1, it2

                    )
                }
            }
            ft.replace(R.id.weather_container, mFragment)
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            ft.addToBackStack(null)
            ft.commit()
            showStats = true
        }

        nameV?.text = name
        descriptionV?.text = description
        distanceV?.text = "Dystans: $distance km"
        collapsingToolbarLayout?.title = name

        mapFragment.getMapAsync(this)
        Picasso.get().load(image).fit().centerInside().into(imgV)
    }

    fun sendData(n: String, desc: String, dist: String, img: String, x: Double, y:Double){
        name = n
        description = desc
        distance = dist
        image = img
        geoX = x
        geoY = y
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("name", name)
        outState.putString("description", description)
        outState.putString("distance", distance)
        outState.putString("image", image)
        outState.putDouble("x", geoX!!.toDouble())
        outState.putDouble("x", geoY!!.toDouble())
        outState.putBoolean("showStats", showStats)
    }

    override fun onMapReady(gMap: GoogleMap) {
        val pos = LatLng(geoX!!, geoY!!)
        gMap.addMarker(
            MarkerOptions()
                .position(pos)
                .title("Marker")
        )
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 10f))
    }



}
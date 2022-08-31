package com.example.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.adapters.RouteAdapter
import com.example.detailslist.DetailsActivity
import com.example.detailslist.R
import com.example.models.RouteItem
import org.json.JSONArray
import org.json.JSONTokener

class MainBiggerFragment : Fragment() {
    private lateinit var mRecyclerViewBig: RecyclerView
    private lateinit var mExampleAdapterBig: RouteAdapter
    private lateinit var mExampleListBig: ArrayList<RouteItem>
    private lateinit var mRequestQueueBig: RequestQueue

    //Zmienne tabletowe
    private lateinit var mFragmentBig: DetailFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_main_bigger, container, false)
        mRecyclerViewBig = view.findViewById(R.id.recyclerBigger_view)
        mRecyclerViewBig.setHasFixedSize(true)
        mRecyclerViewBig.layoutManager = LinearLayoutManager(activity)
        mExampleListBig = ArrayList()
        mRequestQueueBig = Volley.newRequestQueue(activity)
        parseJSON()

        return view
    }

    private fun parseJSON() {
        val url = "http://192.168.1.2/FinalApp/Api.php"

        val stringReq = StringRequest(
            Request.Method.GET, url,
            { response ->
                val strResp = response.toString()
                val jsonArray = JSONTokener(strResp).nextValue() as JSONArray

                for (i in 0 until jsonArray.length()) {
                    val hit = jsonArray.getJSONObject(i)

                    val id = hit.getInt("id")
                    val name = hit.getString("name")
                    val des = hit.getString("descript")
                    val dis = hit.getInt("distance")
                    val imgUrl = hit.getString("image")
                    val cordX = hit.getString("cordX")
                    val cordY = hit.getString("cordY")
                    val level = hit.getString("level")
                    if (level == "hard"){
                        mExampleListBig.add(RouteItem(id, name, des, dis, imgUrl, cordX.toDouble(), cordY.toDouble()))
                    }

                }
                mExampleAdapterBig = RouteAdapter(requireContext(), mExampleListBig)
                mRecyclerViewBig.adapter = mExampleAdapterBig

                mExampleAdapterBig.setOnItemClickListener(object: RouteAdapter.OnItemClickListener{
                    override fun onItemClick(position: Int) {
                        val isTablet = resources.getBoolean(R.bool.isTablet)
                        if (isTablet){
                            val ft = parentFragmentManager.beginTransaction()
                            mFragmentBig = DetailFragment.newInstance()
                            mFragmentBig.sendData(mExampleListBig[position].mName,
                                mExampleListBig[position].mDescript,
                                mExampleListBig[position].mDistance.toString(),
                                mExampleListBig[position].mImage,
                                mExampleListBig[position].mCordX,
                                mExampleListBig[position].mCordY)
                            ft.replace(R.id.fragment_container, mFragmentBig)
                            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            ft.addToBackStack(null)
                            ft.commit()
                        }
                        else{
                            val intent = Intent(activity, DetailsActivity::class.java)
                            intent.putExtra("name", mExampleListBig[position].mName)
                            intent.putExtra("description", mExampleListBig[position].mDescript)
                            intent.putExtra("distance", mExampleListBig[position].mDistance.toString())
                            intent.putExtra("img", mExampleListBig[position].mImage)
                            intent.putExtra("x", mExampleListBig[position].mCordX.toString())
                            intent.putExtra("y", mExampleListBig[position].mCordY.toString())
                            startActivity(intent)
                        }
                    }
                })
            },
            { Log.d("API", "Coś poszło nie tak...") })
        mRequestQueueBig.add(stringReq)
    }
}